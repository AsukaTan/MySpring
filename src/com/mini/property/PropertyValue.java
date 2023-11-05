package com.mini.property;

public class PropertyValue {
    private final String name;
    private final Object value;
    private final String type;
    private final boolean isRef;
    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.isRef = isRef;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return this.type;
    }

    public boolean getIsRef() {
        return isRef;
    }
}