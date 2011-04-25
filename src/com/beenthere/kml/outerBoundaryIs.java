package com.beenthere.kml;

public class outerBoundaryIs extends boundary
{


    public outerBoundaryIs()
    {
        super();
    }

    public outerBoundaryIs(Node parent)
    {
        super(parent);
    }



    public Object clone() throws CloneNotSupportedException
    {
        outerBoundaryIs result = (outerBoundaryIs)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
    }
}
