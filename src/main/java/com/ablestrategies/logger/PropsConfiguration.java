package com.ablestrategies.logger;

/**
 * PropsConfiguration - An IConfiguration that retrieves settings from the System.properties.
 * @apiNote You can set the properties from an XML file, a properties file, or the command-line.
 */
public class PropsConfiguration implements IConfiguration {

    /**
     * Get a String property.
     * @param key Lookup key - name of the Property.
     * @param defaultValue default value to be used if the key or value is missing
     * @return The configured value or, if not found, the defaultValue.
     */
    public String getString(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    /**
     * Get an Integer property.
     * @param key Lookup key - name of the Property.
     * @param defaultValue default value to be used if the key or value is missing
     * @return The configured value or, if not found, the defaultValue.
     */
    public int getInteger(String key, int defaultValue) {
        String stringValue = System.getProperty(key, "" + defaultValue);
        try {
            return Integer.parseInt(stringValue);
        }  catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
