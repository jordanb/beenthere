package com.beenthere.kml;

abstract public class TimePrimitive extends ObjectNode {

    public TimePrimitive() {
        super();
    }

    public TimePrimitive(Node parent) {
        super(parent);
    }

    public Object clone() throws CloneNotSupportedException {
        TimePrimitive result = (TimePrimitive) super.clone();
        return result;
    }
    
    public void setRecursiveNotDirty() {
        super.setRecursiveNotDirty();
    }
}
