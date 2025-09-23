package com.ablestrategies.logger;

/**
 * Level - For specifying the log message level, essentially the severity.
 */
public enum Level {

    /** Lowest priority - show detailed trace of everything. */
    Trace(1),

    /** Low priority for debugging - provides diagnostic data. */
    Diag(2),

    /** Medium priority - for tracking provenance and flow. */
    Info(3),

    /** High priority - something has gone awry. */
    Warn(4),

    /** High severity - error that must be dealt with. */
    Error(5);

    /** Value of the priority level */
    private final int value;

    /**
     * Ctor.
     * @param value priority level
     */
    Level(int value) {
        this.value = value;
    }

    /**
     * Fetch the priority value.
     * @return numeric priority/severity level.
     */
    public int getValue() {
        return value;
    }

    /**
     * Look up a Level based on its numeric priority level.
     * @param value numeric priority level.
     * @return corresponding Level. Warn if not found.
     * @apiNote static method
     */
    public static Level fromValue(int value) {
        for(Level level : Level.values()) {
            if (level.value == value) {
                return level;
            }
        }
        return Level.Warn;
    }

    /**
     * Look up a Level based on its name
     * @param name Trace, Diag, etc.
     * @return corresponding Level. Warn if not found.
     * @apiNote static method
     */
    public static Level fromName(String name) {
        name = name.toUpperCase().charAt(0) + name.toLowerCase().substring(1);
        try {
            return Level.valueOf(name);
        } catch(IllegalArgumentException iae) {
            return Level.Warn;
        }
    }

    /**
     * ToString override.
     * @return Descriptive String.
     */
    @Override
    public String toString() {
        return "Level[name=" + this.name() + ", value=" + this.getValue() + "]";
    }

}
