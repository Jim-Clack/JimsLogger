package com.ablestrategies.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Support - Static support methods.
 */
class Support {

    /** Package names get abbreviated to this length. */
    static int MAX_ABBREV_LGT = 16;

    /**
     * Get the Caller's stack trace element.
     * @return Most recent stack trace element not in the Logger package.
     * @apiNote [static] method
     */
    static StackTraceElement getCallerStackTraceElement() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int elementIndex =  1; // skip over stackDump()
        // skip over Logger internal methods...
        while(elementIndex < stackTrace.length-1
                && stackTrace[elementIndex].getClassName().startsWith("com.ablestrategies.logger")) {
            elementIndex++;
        }
        return stackTrace[elementIndex];
    }

    /**
     * Get a man-readable version of an exception stack trace.
     * @param throwable The exception.
     * @return String containing the stack trace.
     * @apiNote [static] method
     */
    static String getStackTraceAsString(Throwable throwable) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream printStream = new PrintStream(buffer)) {
            throwable.printStackTrace(printStream);
        }
        return buffer.toString();
    }

    /**
     * Abbreviate a dotted string, typically a package name,
     * @param dottedString String to be abbreviated. If not empty, it should end with a dot.
     * @return Abbreviated version, still ending with the original dot.
     * @apiNote [static] method
     */
    static String abbreviate(String dottedString) {
        if(dottedString.length() > MAX_ABBREV_LGT*2) { // if really long, strip out some vowels
            dottedString = dottedString.replaceAll("([^.])[aeiou]", "$1");
        }
        int nextDotPosition = dottedString.indexOf(".");
        while(dottedString.length() > MAX_ABBREV_LGT) {
            if(nextDotPosition > 0) {
                dottedString = dottedString.substring(0, nextDotPosition - 1) + dottedString.substring(nextDotPosition);
            }
            nextDotPosition = dottedString.indexOf(".", nextDotPosition + 1);
            if(nextDotPosition < 0) {
                nextDotPosition = dottedString.indexOf(".");
            }
        }
        return dottedString;
    }

    /**
     * Assemble a String containing the caller's "package.class.method" from a LogEventGetter.
     * @param getter where to find the caller data
     * @param showPackage true to prepend the package name
     * @param showClass true to include the class name
     * @param abbreviate true to abbreviate the package name
     * @return the desired path as a dot-delimited string
     * @apiNote [static] method
     */
    static String assembleCallerPath(LogEventStringGetter getter, boolean showPackage, boolean showClass, boolean abbreviate) {
        String result = getter.getClassName();
        String packageName = "";
        int lastDotPosition = result.lastIndexOf(".");
        if(lastDotPosition > 0) {
            packageName =  result.substring(0, lastDotPosition + 1);
            result = result.substring(lastDotPosition + 1);
        }
        if(!showPackage) {
            packageName = "";
        }
        if(abbreviate) {
            packageName = Support.abbreviate(packageName);
        }
        String methodName = getter.getMethodName();
        result = packageName + result + "."  + methodName;
        if(!showClass) {
            result = methodName;
        }
        return result;
    }

    /**
     * Serious internal errors are dealt with here.
     * @param critical True if a notification must be made immediately.
     * @param message Description of the problem.
     * @param throwable Optional exception, or pass null.
     * @apiNote Property "jlogger.error.handling" may be set to...
     *   "silent", "console", "syserror", or "exception"
     *   (default is "syserror")
     */
    public static void handleLoggerError(boolean critical, String message, Throwable throwable) {
        // This must be a property, regardless of the IConfiguration implementation
        String handling = System.getProperty("jlogger.error.handling", "syserror");
        if(handling.equals("silent") && !critical) {
            return;
        }
        PrintStream stream = System.err;
        if(handling.equals("console")) {
            stream = System.out;
        }
        if(critical) {
            stream.println("\n-----------\nCRITICAL" + message);
        }
        stream.println("JLogger internal error: " + message);
        if(throwable != null) {
            stream.println(Support.getStackTraceAsString(throwable));
        }
        if(handling.equals("exception")) {
            throw new RuntimeException("Logger Exception", throwable);
        }
    }

}
