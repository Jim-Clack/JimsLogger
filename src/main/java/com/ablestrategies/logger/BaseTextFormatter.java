package com.ablestrategies.logger;

/**
 * In order to improve performance, the same formatted message will be re-used by multiple
 * formatters when appropriate. Formatters that intend to to this should have the following
 * first and last lines in their format() method:
 * <code>
 *   public String format(LogEvent event) {
 *       String cache = getCachedResultIfPresent(this, prefix, recursionDepth.toString(), event);
 *       if(cache != null) {
 *           return cache;
 *       }
 *       ...
 *       // other content of format() method.
 *       ...
 *       cacheResult(event, result);
 *   }
 * </code>
 */
public class BaseTextFormatter {

    /** TextFormatter class name ":" prefix ":" format settings. */
    private String formatterSignature;

    /**
     * Get a previously cached message, already formatted.
     * @param formatterImpl The XxxTextFormatter class.
     * @param prefix The prefix for messages.
     * @param settings Any other settings that affect the message format, as concatenated strings.
     * @param event The event that might have a cached message.
     * @return The cached message, already formatted, or null if there was none.
     */
    protected String getCachedResultIfPresent(
            BaseTextFormatter formatterImpl, String prefix, String settings, LogEvent event) {
        formatterSignature = formatterImpl.getClass().getName() + ":" + prefix + ":" + settings;
        return event.getFormattersCache(formatterSignature);
    }

    /**
     * Cache a formatted message to an event.
     * @param event The event that it will be cached to.
     * @param result The formatted message to be cached/
     */
    protected void cacheResult(LogEvent event, String result) {
        event.putFormattersCache(formatterSignature, result);
    }

}
