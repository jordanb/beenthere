package com.beenthere.kml;

public class IconStyle extends ColorStyle
{
    public static float DEFAULT_SCALE=1;
    protected float scale = DEFAULT_SCALE;
    private boolean isScaleDirty;
    public static float DEFAULT_HEADING=0;
    protected float heading = DEFAULT_HEADING;
    private boolean isHeadingDirty;
    protected Icon icon;
    protected vec2 hotSpot;


    public IconStyle()
    {
        super();
    }

    public IconStyle(Node parent)
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

    public float getHeading()
    {
        return this.heading;
    }

    public void setHeading(float value)
    {
        this.heading = value;
        this.isHeadingDirty = true;
        setDirty();
    }

    public Icon getIcon()
    {
        return this.icon;
    }

    public void addIcon(Icon value)
    {
        if(this.icon!=null)
        {
            markDeletedNode(this.icon);
        }
        this.icon = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public vec2 getHotSpot()
    {
        return this.hotSpot;
    }

    public void addHotSpot(vec2 value)
    {
        if(this.hotSpot!=null)
        {
            markDeletedNode(this.hotSpot);
        }
        this.hotSpot = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
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
        kml+="<IconStyle";
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
        if(this.heading!=DEFAULT_HEADING)
        {
            kml+="<heading>"+this.heading+"</heading>\n";
        }
        if(this.icon!=null)
        {
            kml+=this.icon.toKML();
        }
        if(this.hotSpot!=null)
        {
            kml+=this.hotSpot.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</IconStyle>\n";
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
        change+="<IconStyle";
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
        if(this.isHeadingDirty)
        {
            change+="<heading>"+this.heading+"</heading>\n";
            this.isHeadingDirty = false;
        }
        if(this.icon!=null && this.icon.isDirty())
        {
            change+=this.icon.toUpdateKML();
        }
        if(this.hotSpot!=null && this.hotSpot.isDirty())
        {
            change+=this.hotSpot.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</IconStyle>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        IconStyle result = (IconStyle)super.clone();
      if(result.icon!=null)
      {
        result.icon = (Icon)this.icon.clone();
        result.icon.setParent(result);
      }
      if(result.hotSpot!=null)
      {
        result.hotSpot = (vec2)this.hotSpot.clone();
        result.hotSpot.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isScaleDirty = false;
        this.isHeadingDirty = false;
        if(this.icon!=null && this.icon.isDirty())
        {
            this.icon.setRecursiveNotDirty();
        }
        if(this.hotSpot!=null && this.hotSpot.isDirty())
        {
            this.hotSpot.setRecursiveNotDirty();
        }
    }
}
