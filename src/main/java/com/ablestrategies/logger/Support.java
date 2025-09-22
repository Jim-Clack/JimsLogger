package com.ablestrategies.logger;

/**
 * Static support methods.
 */
class Support {

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
     * Abbreviate a dotted string, typically a package name,
     * @param dottedString String to be abbreviated. If not empty, it should end with a dot.
     * @return Abbreviated version, still ending with the original dot.
     */
    static String abbreviate(String dottedString) {
        int nextDotPosition = dottedString.indexOf(".");
        while(dottedString.length() > 16) {
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

}
