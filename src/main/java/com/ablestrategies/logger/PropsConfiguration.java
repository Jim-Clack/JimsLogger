package com.ablestrategies.logger;

public class PropsConfiguration implements IConfiguration {

    public String getString(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    public int getInteger(String key, int defaultValue) {
        String stringValue = System.getProperty(key, "" + defaultValue);
        try {
            return Integer.parseInt(stringValue);
        }  catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
