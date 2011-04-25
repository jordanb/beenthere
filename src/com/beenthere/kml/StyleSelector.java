package com.beenthere.kml;

abstract public class StyleSelector extends ObjectNode
{


    public StyleSelector()
    {
        super();
    }

    public StyleSelector(Node parent)
    {
        super(parent);
    }



    public Object clone() throws CloneNotSupportedException
    {
        StyleSelector result = (StyleSelector)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
    }
}
