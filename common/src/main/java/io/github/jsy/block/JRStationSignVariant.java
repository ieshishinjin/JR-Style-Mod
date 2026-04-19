package io.github.jsy.block;

import net.minecraft.util.StringRepresentable;

public enum JRStationSignVariant implements StringRepresentable {
    HANGING("hanging"),
    POLE("pole");

    private final String name;

    JRStationSignVariant(String name) {
        this.name = name;
    }

    public static JRStationSignVariant fromName(String name) {
        for (JRStationSignVariant value : values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return HANGING;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
