package com.beenthere.kml;

public class vec2 extends Node
{


    public vec2()
    {
        super();
    }

    public vec2(Node parent)
    {
        super(parent);
    }



    public Object clone() throws CloneNotSupportedException
    {
        vec2 result = (vec2)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
    }
}
