package dev.rygen.intersectionlightcontroller.enums;

public enum LightColor {
    GREEN(0),
    YELLOW(1),
    RED(2);

    private final int code;

    LightColor(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public LightColor next() {
        // Cycle through colors: GREEN → YELLOW → RED → GREEN
        return values()[(code + 1) % values().length];
    }

    public boolean isValidTransition(LightColor to) {
        // Valid transitions: GREEN→YELLOW, YELLOW→RED, RED→GREEN
        return to.code == (this.code + 1) % values().length;
    }
}
