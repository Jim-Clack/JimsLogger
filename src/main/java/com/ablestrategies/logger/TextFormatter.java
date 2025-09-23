package com.ablestrategies.logger;

/**
 * TextFormatter - Convert a LogEvent to a message String.<p/>
 * This makes use of a LogEventStringGetter to access the contents of the LogEvent.<br/>
 * ------------------------------------
 * <br/>
 * Guide to replacement symbols.
 * <br/>
 * All replacement symbols begin with @, followed by (A) or (B) below<br/>
 * <p/> <br/>
 * (A) 1..9  vararg parameter number (one-based) followed by </li>
 * <ul>
 * <li>  s     string </li>
 * <li>  b     boolean as T or F </li>
 * <li>  B     boolean ad True or False </li>
 * <li>  i     integer or long </li>
 * <li>  h     integer or long in hexadecimal </li>
 * <li>  f     floating point or double </li>
 * <li>  d     date and time in local format </li>
 * <li>  D     date and time in UTC format </li>
 * <li>  e     exception message </li>
 * <li>  E     exception message and trace </li>
 * <li>  t     object toString() </li>
 * <li>  o     object/array dump, shallow </li>
 * <li>  O     object/array dump, deep (not yet implemented) </li>
 * </ul> <br/>
 * (B) The following are NOT passed as vararg values </li>
 * <ul>
 * <li>  l     LogLevel value </li>
 * <li>  L     LogLevel name </li>
 * <li>  d     date short local form </li>
 * <li>  D     date long UTC form </li>
 * <li>  t     time short local form </li>
 * <li>  T     time long UTC form </li>
 * <li>  u     date and time short local form </li>
 * <li>  U     date and time UTC form </li>
 * <li>  e     exception message </li>
 * <li>  E     exception message and trace </li>
 * <li>  m     calling method name </li>
 * <li>  c     calling class.method name </li>
 * <li>  p     calling package.class.method name </li>
 * <li>  p     calling package.class.method name (package not abbreviated) </li>
 * <li>  h     thread name </li>
 * <li>  @     two @-signs (@@) are escaped to a single @ </li>
 * </ul>
 * <p/> <br/>
 * Please note that...
 * <ul>
 * <li>If a value is null then it will be replaced with (null)
 * <li>If a value is illegal then it will be replaced with ###
 * <li>If a symbol is corrupt then it will be replaced with ???
 * </ul>
 * @apiNote Class names (m, M, c, and C) include an abbreviated package prefix.
 */
public class TextFormatter {

    /** The gets the substitution values from the LogEvent. */
    private LogEventStringGetter getter;

    /** Default prefix for all messages. */
    private String prefix = "@U [@L] @p: ";

    /**
     * Ctor.
     * @param prefix The message prefix, or null to accept the default.
     */
    public TextFormatter(String prefix) {
        if(prefix != null) {
            this.prefix = prefix;
        }
    }

    /**
     * Perfrom the conversion - format a LogEvent as a String.
     * @param logEvent To be formatted as a string message.
     * @return The resultant textual message.
     */
    public String format(LogEvent logEvent) {
        getter = new LogEventStringGetter(logEvent);
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

    /**
     * Substitute a replacment symbol.
     * @param getter The source of LogEvent data.
     * @param symbol The symbol to replace.
     * @param argNum An argument number, must be 0 for non-argument values.
     * @return The substituted value.
     */
    private String expandSymbol(LogEventStringGetter getter, char symbol, int argNum) {
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
    private String expandMessageArg(LogEventStringGetter getter, char symbol, int argNum) {
        return switch (symbol) {
            case 's' -> getter.getStringArgumentAsString(argNum);
            case 'b' -> getter.getBooleanArgumentAsString(argNum).substring(0, 1);
            case 'B' -> getter.getBooleanArgumentAsString(argNum);
            case 'i' -> getter.getLongArgumentAsString(argNum);
            case 'h' -> getter.getHexArgumentAsString(argNum);
            case 'f' -> getter.getDoubleArgumentAsString(argNum);
            case 'd' -> getter.getLocalDateTimeArgumentAsString(argNum);
            case 'D' -> getter.getUtcDateTimeArgumentAsString(argNum);
            case 'e' -> getter.getExceptionMessageArgumentAsString(argNum);
            case 'E' -> getter.getExceptionStackDumpArgumentAsString(argNum);
            case 't' -> getter.getToStringArgumentAsString(argNum);
            case 'o' -> getter.getObjectArgumentAsString(argNum, 0);
            case 'O' -> getter.getObjectArgumentAsString(argNum, 1);
            default -> "###";
        };
    }

    /**
     * Perform symbol replacement on Event values.
     * @param getter access to the LogEvent.
     * @param symbol symbol from message
     * @return replacement string
     */
    private String expandEventValue(LogEventStringGetter getter, char symbol) {
        return switch (symbol) {
            case 'l' -> Long.toString(getter.getLevel().getValue());
            case 'L' -> getter.getLevel().name();
            case 'd' -> getter.getTimestampLocalDateAsString();
            case 'D' -> getter.getTimestampUtcDateAsString();
            case 't' -> getter.getTimestampLocalTimeAsString();
            case 'T' -> getter.getTimestampUtcTimeAsString();
            case 'u' -> getter.getTimestampLocalDateTimeAsString();
            case 'U' -> getter.getTimestampUtcDateTimeAsString();
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
