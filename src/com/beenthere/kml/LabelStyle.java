package com.beenthere.kml;

public class LabelStyle extends ColorStyle
{
    public static float DEFAULT_SCALE=1;
    protected float scale = DEFAULT_SCALE;
    private boolean isScaleDirty;


    public LabelStyle()
    {
        super();
    }

    public LabelStyle(Node parent)
    {
        super(parent);
    }

    public float getScale()
    {
        return this.scale;
    }

    public void setScale(float value)
    {
        this.scale = value;
        this.isScaleDirty = true;
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
        kml+="<LabelStyle";
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
        if(this.scale!=DEFAULT_SCALE)
        {
            kml+="<scale>"+this.scale+"</scale>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</LabelStyle>\n";
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
        change+="<LabelStyle";
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
        if(this.isScaleDirty)
        {
            change+="<scale>"+this.scale+"</scale>\n";
            this.isScaleDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</LabelStyle>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        LabelStyle result = (LabelStyle)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isScaleDirty = false;
    }
}
