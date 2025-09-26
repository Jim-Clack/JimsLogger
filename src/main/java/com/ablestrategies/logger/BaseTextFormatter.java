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

    protected String getCachedResultIfPresent(
            BaseTextFormatter formatterImpl, String prefix, String settings, LogEvent event) {
        formatterSignature = formatterImpl.getClass().getName() + ":" + prefix + ":" + settings;
        return event.getFormattersCache(formatterSignature);
    }

    protected void cacheResult(LogEvent event, String result) {
        event.putFormattersCache(formatterSignature, result);
    }

}
