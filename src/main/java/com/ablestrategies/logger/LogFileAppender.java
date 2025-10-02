package com.ablestrategies.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.SortedMap;

/**
 * LogfileAppender - Log writer that outputs events in textual form to rolling logfiles
 * <p/><br/>
 * Additional Configuration Settings...<ul>
 *  <li> jlogger.logfile.prefix   "@t @c [@L]: "  See TextFormatter.java </li>
 *  <li> jlogger.logfile.name     "jlog.log"      </li>
 *  <li> jlogger.logfile.kmaxsize "100"           </li>
 *  <li> jlogger.logfile.backups  "10"            </li>
 * </ul>
 **/
public class LogFileAppender implements IAppender {

    private static final String FILE_TEMPLATE = "%s%03d%s";

    private static final int FLUSH_EVERY = 50;

    /** We need this to format LogEvents into textual messages. */
    private final TextFormatter textFormatter;

    private long currentFileSize;
    private FileWriter writer;

    private String rootFilename;
    private final long maxFileSize;
    private final long maxBackups;

    private int writesSinceFlush = 0;
    private boolean shutdownInProgress = false;

    /**
     * Ctor. (required)
     * @param configuration Source of settings.
     */
    @SuppressWarnings("unused")
    public LogFileAppender(IConfiguration configuration) {
        String prefix = configuration.getString("jlogger.logfile.prefix", "@t @c [@L]: ");
        rootFilename = configuration.getString("jlogger.logfile.name", "jlog.log").trim();
        maxFileSize = configuration.getLong("jlogger.logfile.kfilesize", 100) * 1024;
        maxBackups = Math.min(configuration.getLong("jlogger.logfile.backups", 10), 500);
        rootFilename = rootFilename.replace("\\", "/");
        if(rootFilename.indexOf(".") < 1 || rootFilename.indexOf(".") < rootFilename.lastIndexOf("/")) {
            rootFilename = rootFilename + ".log";
        }
        textFormatter = new TextFormatter(prefix);
        rolloverAndOpenLogFile();
    }

    /**
     * Output a LogEvent to the log.
     * @param logEvent To be formatted and written.
     */
    public void append(LogEvent logEvent) {
        String message  = textFormatter.format(logEvent) + System.lineSeparator();
        if(currentFileSize + message.length() > maxFileSize) {
            rolloverAndOpenLogFile();
        }
        currentFileSize += message.length();
        try {
            writer.write(message);
            if(++writesSinceFlush >= FLUSH_EVERY) {
                writer.flush();
                writesSinceFlush = 0;
            }
        } catch (IOException e) {
            Support.handleLoggerError(true, "LogfileAppender cannot write to file", e);
        }
    }

    /**
     * Get a list of all logfile/backups names, sorted newest one first.
     */
    private void rolloverAndOpenLogFile() {
        if(shutdownInProgress) {
            return;
        }
        if(writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                Support.handleLoggerError(false, "LogFileAppender: Trouble closing log", e);
            }
            writer = null;
        }
        String filename = rolloverBackups();
        String header = "###LogFile### " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try {
            writer = new FileWriter(filename);
            writer.write(header + System.lineSeparator());
            System.out.println("LogfileAppender writing to: " + filename);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        currentFileSize = 0;
        if(shutdownInProgress) {
            Support.handleLoggerError(false, "LogFileAppender - Shutdown may have lost events.", null);
        }
    }

    /**
     * Handle rollover of logfile backups.
     * @return new "latest" filename,
     */
    private String rolloverBackups() {
        SortedMap<Long, File> sortedFilesMap = new java.util.TreeMap<>();
        String filenameWithoutSuffix = rootFilename.substring(0, rootFilename.lastIndexOf("."));
        String filenameSuffix = rootFilename.substring(rootFilename.lastIndexOf("."));
        boolean success1 = getFilesSortedByAge(filenameWithoutSuffix, filenameSuffix, sortedFilesMap);
        boolean success2 = rolloverBackups(sortedFilesMap, filenameWithoutSuffix, filenameSuffix);
        if(!success1 || !success2) {
            Support.handleLoggerError(true, "LogFileAppender - Error: A backup file(s) may be locked or read-only.", null);
        }
        return String.format(FILE_TEMPLATE, filenameWithoutSuffix, maxBackups - 1, filenameSuffix);
    }

    /**
     * Get sorted log/backup files, renaming each with an "x" prefix as we go.
     * @param filenameWithoutSuffix path and filename, no suffix.
     * @param filenameSuffix suffix, preceded by a dot.
     * @param sortedFilesMap (out) To be populated with sorted files by this method.
     * @return Success, false if there is a problem renaming files.
     */
    private boolean getFilesSortedByAge(String filenameWithoutSuffix, String filenameSuffix, SortedMap<Long, File> sortedFilesMap) {
        boolean success = true;
        for(int backupNumber = 0; backupNumber < maxBackups; backupNumber++) {
            String filename = String.format(FILE_TEMPLATE, filenameWithoutSuffix, backupNumber, filenameSuffix);
            long lastModified = backupNumber;
            File theFile = new File(filename);
            if(theFile.exists()) {
                lastModified =  theFile.lastModified();
                File renameTo = new File("x" + filename);
                renameTo.delete(); // just in case
                if(backupNumber != 0) {
                    success &= theFile.renameTo(renameTo);
                    theFile = renameTo; // because the file gets renamed, but theFile does not reflect that
                }
            }
            sortedFilesMap.put(lastModified, theFile);
        }
        return success;
    }

    /**
     * Rename log/backup files based on their age. i.e. jlog000.log ... jlog009.log.
     * @param sortedFilesMap As returned by getFilesSortedByAge.
     * @param filenameWithoutSuffix path and filename, no suffix.
     * @param filenameSuffix suffix, preceded by a dot.
     * @return Success, false if there is a problem renaming files.
     */
    private boolean rolloverBackups(SortedMap<Long, File> sortedFilesMap, String filenameWithoutSuffix, String filenameSuffix) {
        boolean success = true;
        File[] sortedFiles = sortedFilesMap.sequencedValues().toArray(new File[0]);
        for(int fileNumber = 1; fileNumber < maxBackups; fileNumber++) {
            File theFile = sortedFiles[fileNumber];
            String newFilename = String.format("%s%03d%s", filenameWithoutSuffix, fileNumber - 1, filenameSuffix);
            if(theFile.exists()) {
                File renameTo = new File(newFilename);
                renameTo.delete(); // just in case
                success &= theFile.renameTo(new File(newFilename));
            }
        }
        return success;
    }

    /**
     * Notification that the app is shutting down, giving time to flush before close() is called.
     */
    public void notifyShutdown() {
        shutdownInProgress = true;
    }

    /**
     * This will be called after the last log message has been written.
     */
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            // at this point, we can only ignore it.
        }
    }

}
