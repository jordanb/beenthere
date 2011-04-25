package com.beenthere.kml;


abstract public class Geometry extends ObjectNode
{


    public Geometry()
    {
        super();
    }

    public Geometry(Node parent)
    {
        super(parent);
    }



    public Object clone() throws CloneNotSupportedException
    {
        Geometry result = (Geometry)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
    }
}
