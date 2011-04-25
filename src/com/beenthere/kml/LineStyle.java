package com.beenthere.kml;

public class LineStyle extends ColorStyle
{
    public static float DEFAULT_WIDTH=1;
    protected float width = DEFAULT_WIDTH;
    private boolean isWidthDirty;


    public LineStyle()
    {
        super();
    }

    public LineStyle(Node parent)
    {
        super(parent);
    }

    public float getWidth()
    {
        return this.width;
    }

    public void setWidth(float value)
    {
        this.width = value;
        this.isWidthDirty = true;
        setDirty();
    }



    public String toKML()
    {
        return toKML(false);
    }
    public String toKML(boolean suppressEnclosingTags)
    {
        String kml="";
        if(!suppressEnclosingTags)
        {
        kml+="<LineStyle";
        if(this.id!=null)
        {
            kml+=" id=\""+this.getId()+"\"";
        }
        if(this.targetId!=null)
        {
            kml+=" targetId=\""+this.getTargetId()+"\"";
        }
        kml+=">\n";
        }
        kml+=super.toKML(true);
        if(this.width!=DEFAULT_WIDTH)
        {
            kml+="<width>"+this.width+"</width>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</LineStyle>\n";
        }
        return kml;
    }
    public String toUpdateKML()
    {
        return toUpdateKML(false);
    }
    public String toUpdateKML(boolean suppressEnclosingTags)
    {
        if(!isDirty())
        {
            return "";
        }
        String change = "";
        boolean isPrimDirty = isPrimitiveDirty(); // need to track it after object is setNotDirty
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="<LineStyle";
        if(this.id!=null)
        {
            change+=" id=\""+this.getId()+"\"";
        }
        if(this.targetId!=null)
        {
            change+=" targetId=\""+this.getTargetId()+"\"";
        }
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.isWidthDirty)
        {
            change+="<width>"+this.width+"</width>\n";
            this.isWidthDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</LineStyle>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        LineStyle result = (LineStyle)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isWidthDirty = false;
    }
}
