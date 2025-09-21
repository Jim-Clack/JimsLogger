package com.ablestrategies.logger;

/**
 * All replacement symbols begin with @, followed by either
 * A>>> 1..9  vararg parameter number (one-based) followed by
 *   s     string
 *   b     boolean as T or F
 *   B     boolean ad True or False
 *   i     integer or long
 *   h     integer or long in hexadecimal
 *   f     floating point or double in shortest form
 *   F     floating point or double in exponential notation
 *   p     pointer/handle to object/array
 *   o     object/array dump, shallow
 *   O     object/array dump, deep (not yet implemented)
 * B>>> The following are not passed as vararg parameters
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
 *   M     calling class.method name, with line number if DEBUG
 *   c     calling class name
 *   C     calling class.method name, with line number if DEBUG
 *   p     calling package.class name
 *   P     calling package.class.method name, w line number if DEBUG
 *   h     thread number
 *   H     thread name
 *   @     two @-signs (@@) are escaped to a single @
 * If a value is null then it will be replaced with (null)
 * If a value is illegal then it will be replaced with ###
 * If a symbol is corrupt then it will be replaced with ???
 * Class names (m, M, c, and C) include an abbreviated package prefix.
 */
public class TextFormatter {

    private String prefix = "@X [@L] @C: ";

    public TextFormatter(String prefix) {
        this.prefix = prefix;
    }

    public String format(Event logEvent) {
        return logEvent.toString(); // TODO
    }

    private String getClassName(Event logEvent, boolean abbreviatePackage, boolean showLineNumbers) {

        return logEvent.getClassName();
    }
}
