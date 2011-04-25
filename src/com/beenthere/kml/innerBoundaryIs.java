package com.beenthere.kml;


public class innerBoundaryIs extends boundary
{


    public innerBoundaryIs()
    {
        super();
    }

    public innerBoundaryIs(Node parent)
    {
        super(parent);
    }



    public Object clone() throws CloneNotSupportedException
    {
        innerBoundaryIs result = (innerBoundaryIs)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
    }
}
