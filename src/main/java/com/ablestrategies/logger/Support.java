package com.ablestrategies.logger;

/**
 * Static support methods.
 */
class Support {

    public static int MAX_ABBREV_LGT = 16;

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
        if(dottedString.length() > MAX_ABBREV_LGT*2) {
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
     */
    static String assembleCallerPath(LogEventGetter getter, boolean showPackage, boolean showClass, boolean abbreviate) {
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

}
