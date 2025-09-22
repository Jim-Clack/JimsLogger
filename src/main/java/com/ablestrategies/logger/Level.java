package com.ablestrategies.logger;

public enum Level {
    Trace(1),
    Diag(2),
    Info(3),
    Warn(4),
    Error(5);

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Level fromValue(int value) {
        for(Level level : Level.values()) {
            if (level.value == value) {
                return level;
            }
        }
        return Level.Warn;
    }

    public static Level fromName(String name) {
        name = name.toUpperCase().charAt(0) + name.toLowerCase().substring(1);
        try {
            return Level.valueOf(name);
        } catch(IllegalArgumentException iae) {
            return Level.Warn;
        }
    }

    @Override
    public String toString() {
        return "LogLevel[name=" + this.name() + ", value=" + this.getValue() + "]";
    }

}
