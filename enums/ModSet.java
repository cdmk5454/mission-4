package org.example.enums;

public enum ModSet {
    MAIN(0),
    UPDATE(2),
    DELETE(3),
    EXIT(4),
    REGISTER(5),
    VIEW(9);

    private final int value;

    ModSet(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
