package com.ablestrategies.logger;

/**
 * KeyValuePair - generic "pair" of values.
 * @param <K> key - typically for lookup
 * @param <V> value - typically the data
 */
public class KeyValuePair <K, V> {

    /** The key - the first item in the pair. */
    private final K key;

    /** The value - the second item in the pair. */
    private final V value;

    /**
     * Ctor.
     * @param key First value, often a key.
     * @param value Second value, often just data.
     */
    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the first item.
     * @return The key.
     */
    public K getKey() {
        return key;
    }

    /**
     * Get the second item.
     * @return The value.
     */
    public V getValue() {
        return value;
    }

}
