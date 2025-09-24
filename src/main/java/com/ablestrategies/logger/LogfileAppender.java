package com.ablestrategies.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LogfileAppender - Log writer that outputs events in textual form to rolling logfiles
 * <p/><br/>
 * Additional Configuration Settings...<ul>
 *  <li> jlogger.logfile.prefix   "@X [@L] @C: "        See TextFormatter.java </li>
 *  <li> jlogger.logfile.name     "jlog"                See FileAppender.java </li>
 *  <li> jlogger.logfile.kmaxsize "100"                 See FileAppender.java </li>
 *  <li> jlogger.logfile.backups  "10"                  See FileAppender.java </li>
 * </ul>
 **/
public class LogfileAppender implements IAppender {

    /** We need this to format LogEvents into textual messages. */
    private final TextFormatter textFormatter;

    private long currentFileSize;
    private FileWriter writer;

    private final String rootFilename;
    private final long maxFileSize;
    private final long maxBackups;

    /**
     * Ctor. (required)
     * @param configuration Source of settings.
     */
    public LogfileAppender(IConfiguration configuration) {
        String prefix = configuration.getString("jlogger.logfile.prefix", "@t @c [@L]: ");
        rootFilename = configuration.getString("jlogger.logfile.name", "jlog").trim();
        maxFileSize = configuration.getLong("jlogger.logfile.kfilesize", 100) * 1024;
        maxBackups = configuration.getLong("jlogger.logfile.backups", 10);
        textFormatter = new TextFormatter(prefix);
        openLogfile();
    }

    /**
     * Output a LogEvent to the log.
     * @param logEvent To be formatted and written.
     */
    public void append(LogEvent logEvent) {
        String message  = textFormatter.format(logEvent) + System.lineSeparator();
        if(currentFileSize + message.length() > maxFileSize) {
            handleRollover();
        }
        currentFileSize += message.length();
        try {
            writer.write(message);
        } catch (IOException e) {
            Support.handleLoggerError(true, "LogfileAppender cannot write to file", e);
        }
    }

    /**
     * Handle rolling backups.
     */
    private void handleRollover() {
        // TODO finish code herein
    }

    /**
     * Open a new writer - overwriting the oldest backup.
     */
    private void openLogfile() {
        String filename = filenamesSortNewestFirst()[0];
        String header = filename + " " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try {
            if(writer != null) {
                writer.close();
            }
            writer = new FileWriter(filename);
            writer.write(header + System.lineSeparator());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Get a list of all logfile/backups names, sorted newest one first.
     * @return The lust of filenames with the newest one first.
     */
    private String[] filenamesSortNewestFirst() {
        String filename = rootFilename + 1 + ".log";
        return new String[] { filename };
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
