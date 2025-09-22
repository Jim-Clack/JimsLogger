package com.ablestrategies.logger;

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * Guide to replacement symbols.
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * All replacement symbols begin with @, followed by either...
 * <p>
 * A) 1..9  vararg parameter number (one-based) followed by
 *   s     string
 *   b     boolean as T or F
 *   B     boolean ad True or False
 *   i     integer or long
 *   h     integer or long in hexadecimal
 *   f     floating point or double in regular form
 *   F     floating point or double in exponential notation
 *   d     date and time in local format
 *   D     date and time in UTC format
 *   p     pointer/handle to object/array
 *   o     object/array dump, shallow
 *   O     object/array dump, deep (not yet implemented)
 * <p>
 * B) The following are not passed as vararg values
 *   l     LogLevel value
 *   L     LogLevel name
 *   d     date short local form
 *   D     date long UTC form
 *   t     time short local form
 *   T     time long UTC form
 *   x     date and time short local form
 *   X     date and time UTC form
 *   e     exception message
 *   E     exception message and trace
 *   m     calling method name
 *   M     calling method name, with line number if DEBUG
 *   c     calling class name
 *   C     calling class.method name, with line number if DEBUG
 *   p     calling package.class name
 *   P     calling package.class.method name, w line number if DEBUG
 *   h     thread number
 *   H     thread name
 *   @     two @-signs (@@) are escaped to a single @
 * <p>
 * If a value is null then it will be replaced with (null)
 * If a value is illegal then it will be replaced with ###
 * If a symbol is corrupt then it will be replaced with ???
 * Class names (m, M, c, and C) include an abbreviated package prefix.
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
public class TextFormatter {

    private LogEventGetter getter;

    private String prefix = "@X [@L] @C: ";

    public TextFormatter(String prefix) {
        this.prefix = prefix;
    }

    public String format(LogEvent logEvent) {
        getter = new LogEventGetter(logEvent);
        return logEvent.toString(); // TODO
    }

    private String assembleCallerPath(LogEvent logEvent, boolean abbreviated,
                                     boolean showPackage, boolean showClass, boolean showLineNumbers) {
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
        if(abbreviated) {
            packageName = Support.abbreviate(packageName);
        }
        String methodName = getter.getMethodName();
        result = packageName + result + "."  + methodName;
        if(!showClass) {
            result = methodName;
        }
        if(showLineNumbers) {
            // TODO
        }
        return result;
    }

}
