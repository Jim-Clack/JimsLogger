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
 *   f     floating point or double
 *   d     date and time in local format
 *   D     date and time in UTC format
 *   e     exception message
 *   E     exception message and trace
 *   t     object toString()
 *   o     object/array dump, shallow
 *   O     object/array dump, deep (not yet implemented)
 * <p>
 * B) The following are NOT passed as vararg values
 *   l     LogLevel value
 *   L     LogLevel name
 *   d     date short local form
 *   D     date long UTC form
 *   t     time short local form
 *   T     time long UTC form
 *   u     date and time short local form
 *   U     date and time UTC form
 *   e     exception message
 *   E     exception message and trace
 *   m     calling method name
 *   c     calling class.method name
 *   p     calling package.class.method name
 *   p     calling package.class.method name (package not abbreviated)
 *   h     thread name
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

    private String prefix = "@U [@L] @p: ";

    public TextFormatter(String prefix) {
        this.prefix = prefix;
    }

    public String format(LogEvent logEvent) {
        getter = new LogEventGetter(logEvent);
        String message = prefix + getter.getMessage();
        StringBuilder buffer = new StringBuilder();
        int messageLgt = message.length();
        int messagePos = 0;
        int argNum = 0;
        while(messagePos < messageLgt) {
            if (message.charAt(messagePos) == '@') {
                char ch = message.charAt(++messagePos);
                if(Character.isDigit(ch)) {
                    argNum = ch - '0';
                    ch = message.charAt(++messagePos);
                }
                buffer.append(expandSymbol(getter, message.charAt(messagePos), argNum));
            } else {
                buffer.append(message.charAt(messagePos));
            }
            messagePos++;
        }
        return buffer.toString();
    }

    private String expandSymbol(LogEventGetter getter, char symbol, int argNum) {
        if(argNum > 0) {
            return expandMessageArg(getter, symbol, argNum);
        } else {
            return expandEventValue(getter, symbol);
        }
    }

    /**
     * Perform symbol replacement from varargs argument list.
     * @param getter access to the LogEvent.
     * @param symbol symbol from message
     * @param argNum one-based argument number
     * @return replacement string
     */
    private String expandMessageArg(LogEventGetter getter, char symbol, int argNum) {
        return switch (symbol) {
            case 's' -> getter.getArgumentAsString(argNum);
            case 'b' -> getter.getArgumentAsBoolean(argNum).substring(0, 1);
            case 'B' -> getter.getArgumentAsBoolean(argNum);
            case 'i' -> getter.getArgumentAsLong(argNum);
            case 'h' -> getter.getArgumentAsHex(argNum);
            case 'f' -> getter.getArgumentAsDouble(argNum);
            case 'd' -> getter.getArgumentAsLocalDateTime(argNum);
            case 'D' -> getter.getArgumentAsUTCDateTime(argNum);
            case 'e' -> getter.getArgumentAsExceptionMessage(argNum);
            case 'E' -> getter.getArgumentAsExceptionStackDump(argNum);
            case 't' -> getter.getArgumentAsPointer(argNum);
            case 'o' -> getter.getArgumentAsObjectOrArray(argNum);
            case 'O' -> getter.getArgumentAsObjectOrArray(argNum); // TODO
            default -> "###";
        };
    }

    /**
     * Perform symbol replacement on Event values.
     * @param getter access to the LogEvent.
     * @param symbol symbol from message
     * @return replacement string
     */
    private String expandEventValue(LogEventGetter getter, char symbol) {
        return switch (symbol) {
            case 'l' -> Long.toString(getter.getLevel().getValue());
            case 'L' -> getter.getLevel().name();
            case 'd' -> getter.getTimestampLocalDate();
            case 'D' -> getter.getTimestampUTCDate();
            case 't' -> getter.getTimestampLocalTime();
            case 'T' -> getter.getTimestampUTCTime();
            case 'u' -> getter.getTimestampLocalDateTime();
            case 'U' -> getter.getTimestampUTCDateTime();
            case 'e' -> getter.getThrowableMessage();
            case 'E' -> getter.getThrowableStackDump();
            case 'm' -> Support.assembleCallerPath(getter, false, false, true);
            case 'c' -> Support.assembleCallerPath(getter, false, true, true);
            case 'p' -> Support.assembleCallerPath(getter, true, true, true);
            case 'P' -> Support.assembleCallerPath(getter, true, true, false);
            case 'h' -> getter.getThreadName();
            case '@' -> "@";
            default -> "###";
        };
    }

}
