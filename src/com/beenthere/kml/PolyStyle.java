package com.beenthere.kml;

public class PolyStyle extends ColorStyle
{
    public static boolean DEFAULT_FILL=true;
    protected boolean fill = DEFAULT_FILL;
    private boolean isFillDirty;
    public static boolean DEFAULT_OUTLINE=true;
    protected boolean outline = DEFAULT_OUTLINE;
    private boolean isOutlineDirty;


    public PolyStyle()
    {
        super();
    }

    public PolyStyle(Node parent)
    {
        super(parent);
    }

    public boolean getFill()
    {
        return this.fill;
    }

    public void setFill(boolean value)
    {
        this.fill = value;
        this.isFillDirty = true;
        setDirty();
    }

    public boolean getOutline()
    {
        return this.outline;
    }

    public void setOutline(boolean value)
    {
        this.outline = value;
        this.isOutlineDirty = true;
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
        kml+="<PolyStyle";
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
        if(this.fill!=DEFAULT_FILL)
        {
            kml+="<fill>"+this.fill+"</fill>\n";
        }
        if(this.outline!=DEFAULT_OUTLINE)
        {
            kml+="<outline>"+this.outline+"</outline>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</PolyStyle>\n";
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
        change+="<PolyStyle";
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
        if(this.isFillDirty)
        {
            change+="<fill>"+this.fill+"</fill>\n";
            this.isFillDirty = false;
        }
        if(this.isOutlineDirty)
        {
            change+="<outline>"+this.outline+"</outline>\n";
            this.isOutlineDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</PolyStyle>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        PolyStyle result = (PolyStyle)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isFillDirty = false;
        this.isOutlineDirty = false;
    }
}
