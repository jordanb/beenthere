package com.beenthere.kml;

public class Scale extends ObjectNode
{
    public static double DEFAULT_X=1.0;
    protected double x = DEFAULT_X;
    private boolean isXDirty;
    public static double DEFAULT_Y=1.0;
    protected double y = DEFAULT_Y;
    private boolean isYDirty;
    public static double DEFAULT_Z=1.0;
    protected double z = DEFAULT_Z;
    private boolean isZDirty;


    public Scale()
    {
        super();
    }

    public Scale(Node parent)
    {
        super(parent);
    }

    public double getX()
    {
        return this.x;
    }

    public void setX(double value)
    {
        this.x = value;
        this.isXDirty = true;
        setDirty();
    }

    public double getY()
    {
        return this.y;
    }

    public void setY(double value)
    {
        this.y = value;
        this.isYDirty = true;
        setDirty();
    }

    public double getZ()
    {
        return this.z;
    }

    public void setZ(double value)
    {
        this.z = value;
        this.isZDirty = true;
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
        kml+="<Scale";
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
        if(this.x!=DEFAULT_X)
        {
            kml+="<x>"+this.x+"</x>\n";
        }
        if(this.y!=DEFAULT_Y)
        {
            kml+="<y>"+this.y+"</y>\n";
        }
        if(this.z!=DEFAULT_Z)
        {
            kml+="<z>"+this.z+"</z>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Scale>\n";
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
        change+="<Scale";
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
        if(this.isXDirty)
        {
            change+="<x>"+this.x+"</x>\n";
            this.isXDirty = false;
        }
        if(this.isYDirty)
        {
            change+="<y>"+this.y+"</y>\n";
            this.isYDirty = false;
        }
        if(this.isZDirty)
        {
            change+="<z>"+this.z+"</z>\n";
            this.isZDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Scale>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Scale result = (Scale)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isXDirty = false;
        this.isYDirty = false;
        this.isZDirty = false;
    }
}
