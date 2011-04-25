package com.beenthere.kml;

public class Metadata extends Node
{

    public Metadata()
    {
        super();
    }

    public Metadata(Node parent)
    {
        super(parent);
    }



    public Object clone() throws CloneNotSupportedException
    {
        Metadata result = (Metadata)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
    }
}
