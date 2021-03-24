package com.diff.entities.enums;

public enum Result {
    DIFFERENT_SIZE("Different content size"),
    EQUAL("Equal content"),
    NOT_EQUAL("Different content");

    private final String name;

    Result(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
