package com.diff.entities.enums;

public enum Side {
    LEFT("left"),
    RIGHT("right");

    private final String side;

    Side(String side) {
        this.side = side;
    }

    public String getSide() {
        return side;
    }

    public static Side getFromSide(String side) {
        if (!side.isEmpty()) {
            for (Side sideEnum : values()) {
                if (side.equalsIgnoreCase(sideEnum.getSide())) {
                    return sideEnum;
                }
            }
        }
        return null;
    }
}
