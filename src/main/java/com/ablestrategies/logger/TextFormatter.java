package com.ablestrategies.logger;

/**
 * TextFormatter - Convert a LogEvent to a message String.
 * <br/><br/>
 * This makes use of a LogEventStringGetter to access the contents of the LogEvent.
 * <h4> Guide to replacement symbols. </h4>
 * All replacement symbols begin with @, followed by (A) or (B) below<br/>
 * <p/> <br/>
 * (A) 1..9  vararg parameter number (one-based) followed by </li>
 * <ul>
 * <li>  s     string </li>
 * <li>  b     boolean as T or F </li>
 * <li>  B     boolean as true or false </li>
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
 * (B) The following are from the LogEvent, NOT vararg arg values </li>
 * <ul>
 * <li>  l     LogLevel value </li>
 * <li>  L     LogLevel name </li>
 * <li>  d     timestamp date short local form </li>
 * <li>  D     timestamp date long UTC form </li>
 * <li>  t     timestamp time short local form </li>
 * <li>  T     timestamp time long UTC form </li>
 * <li>  u     timestamp ate and time short local form </li>
 * <li>  U     timestamp date and time UTC form </li>
 * <li>  n     timestamp nanoseconds </li>
 * <li>  e     exception message </li>
 * <li>  E     exception message and trace </li>
 * <li>  m     calling method name </li>
 * <li>  c     calling class.method name </li>
 * <li>  p     calling package.class.method name </li>
 * <li>  p     calling package.class.method name (package not abbreviated) </li>
 * <li>  h     calling thread name </li>
 * <li>  @     two @-signs (@@) are escaped to a single @ </li>
 * </ul> <br/>
 * Or you can use {} to substitute the "next argument" from varargs. </li>
 * <ul>
 * <li>  {} Substitutes the next argument from varargs, whatever type it is. </li>
 * <li>  do not mix this symbol with the other (@) vararg symbols in a message. </li>
 * <li>  however, you may mix it with other (@) non-vararg symbols. </li>
 * <li>  or you can mix the two if you put the arg number in braces: {1}, {2}, .... </li>
 * <li>  two "{" symbols "{{" are escaped to a single left brace "{". </li>
 * </ul> <br/>
 * <h4> Please note that... </h4>
 * <ul>
 * <li>If a value is null then it will be replaced with (null)
 * <li>If a value is illegal then it will be replaced with ###
 * <li>If a symbol is corrupt then it will be replaced with ???
 * </ul>
 * @apiNote Class names (m, M, c, and C) include an abbreviated package prefix.
 */
public class TextFormatter extends BaseTextFormatter implements ITextFormatter {

    /** Default prefix for all messages. */
    private final String prefix;

    /** For counting {} symbols in a message. */
    private int bracesArgNum;

    /**
     * Ctor.
     * @param prefix The message prefix.
     */
    public TextFormatter(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Perform the conversion - format a LogEvent as a String.
     * @param logEvent To be formatted as a string message.
     * @return The resultant textual message.
     */
    public String format(LogEvent logEvent) {
        String cache = getCachedResultIfPresent(this, prefix, "dontCare", logEvent);
        if(cache != null) {
            return cache;
        }
        LogEventStringGetter getter = new LogEventStringGetter(logEvent);
        String message = prefix + getter.getMessage();
        StringBuilder buffer = new StringBuilder();
        int messageLgt = message.length();
        int messagePos = 0;
        bracesArgNum = 1;
        while(messagePos < messageLgt) {
            if(message.charAt(messagePos) == '{') {
                messagePos = substituteBraces(message, messagePos, buffer, getter);
            } else if (message.charAt(messagePos) == '@') {
                messagePos = substituteAtSign(message, messagePos, buffer, getter);
            } else {
                buffer.append(message.charAt(messagePos));
            }
            messagePos++;
        }
        String result = buffer.toString();
        cacheResult(logEvent, result);
        return result;
    }

    /**
     * Handle an {} replacement symbol.
     * @param message Source of replacement symbol
     * @param messagePos Location of "{"} in message
     * @param buffer To update/append to.
     * @param getter Source of substitution data and arguments.
     * @return updated messagePos.
     * @apiNote Also modifies object state - member variable bracesArgNum.
     */
    private int substituteBraces(String message, int messagePos, StringBuilder buffer, LogEventStringGetter getter) {
        char ch = message.charAt(++messagePos);
        if(ch == '{') {
            buffer.append("{");
            return messagePos;
        }
        if(Character.isDigit(ch)) {
            bracesArgNum = ch - '0';
            messagePos++;
        }
        if(message.charAt(messagePos) != '}') {
            Support.handleLoggerError(false, "TextFormatter - Bad replacement symbol (" + message + ")", null);
            return messagePos;
        }
        buffer.append(getter.getAnyTypeArgumentAsString(bracesArgNum));
        ++bracesArgNum;
        return messagePos;
    }

    /**
     * Handle an @-sign replacement symbol.
     * @param message Source of replacement symbol
     * @param messagePos Location of @-sign in message
     * @param buffer To update/append to.
     * @param getter Source of substitution data and arguments.
     * @return updated messagePos.
     */
    private int substituteAtSign(String message, int messagePos, StringBuilder buffer, LogEventStringGetter getter) {
        int argNum = 0;
        char ch = message.charAt(++messagePos);
        if (Character.isDigit(ch)) {
            argNum = ch - '0';
        }
        if(argNum > 0) {
            buffer.append(expandMessageArg(getter, message.charAt(++messagePos), argNum));
        } else {
            buffer.append(expandEventValue(getter, message.charAt(++messagePos)));
        }
        return messagePos;
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
            case 'b' -> getter.getBooleanArgumentAsString(argNum).substring(0, 1).toUpperCase();
            case 'B' -> getter.getBooleanArgumentAsString(argNum);
            case 'i' -> getter.getLongArgumentAsString(argNum);
            case 'h' -> getter.getHexArgumentAsString(argNum);
            case 'f' -> getter.getDoubleArgumentAsString(argNum);
            case 'd' -> getter.getLocalDateTimeArgumentAsString(argNum);
            case 'D' -> getter.getUtcDateTimeArgumentAsString(argNum);
            case 'e' -> getter.getExceptionMessageArgumentAsString(argNum);
            case 'E' -> getter.getExceptionStackDumpArgumentAsString(argNum);
            case 't' -> getter.getToStringArgumentAsString(argNum);
            case 'o' -> getter.getObjectArgumentAsString(argNum, 1);
            case 'O' -> getter.getObjectArgumentAsString(argNum, 3);
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
            case 'n' -> getter.getTimestampNanosAsString();
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
