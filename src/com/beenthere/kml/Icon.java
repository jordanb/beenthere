package com.beenthere.kml;


public class Icon extends Link
{
    protected int x;
    private boolean isXDirty;
    protected int y;
    private boolean isYDirty;
    protected int w;
    private boolean isWDirty;
    protected int h;
    private boolean isHDirty;


    public Icon()
    {
        super();
    }

    public Icon(Node parent)
    {
        super(parent);
    }

    public int getX()
    {
        return this.x;
    }

    public void setX(int value)
    {
        this.x = value;
        this.isXDirty = true;
        setDirty();
    }

    public int getY()
    {
        return this.y;
    }

    public void setY(int value)
    {
        this.y = value;
        this.isYDirty = true;
        setDirty();
    }

    public int getW()
    {
        return this.w;
    }

    public void setW(int value)
    {
        this.w = value;
        this.isWDirty = true;
        setDirty();
    }

    public int getH()
    {
        return this.h;
    }

    public void setH(int value)
    {
        this.h = value;
        this.isHDirty = true;
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
        kml+="<Icon";
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
        kml+="<x>"+this.x+"</x>\n";
        kml+="<y>"+this.y+"</y>\n";
        kml+="<w>"+this.w+"</w>\n";
        kml+="<h>"+this.h+"</h>\n";
        if(!suppressEnclosingTags)
        {
            kml+="</Icon>\n";
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
        change+="<Icon";
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
        if(this.isWDirty)
        {
            change+="<w>"+this.w+"</w>\n";
            this.isWDirty = false;
        }
        if(this.isHDirty)
        {
            change+="<h>"+this.h+"</h>\n";
            this.isHDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Icon>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Icon result = (Icon)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isXDirty = false;
        this.isYDirty = false;
        this.isWDirty = false;
        this.isHDirty = false;
    }
}
