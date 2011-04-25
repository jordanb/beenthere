package com.beenthere.kml;

public class ScreenOverlay extends Overlay
{
    protected vec2 overlayXY;
    protected vec2 screenXY;
    protected vec2 rotationXY;
    protected vec2 size;
    public static float DEFAULT_ROTATION=0;
    protected float rotation = DEFAULT_ROTATION;
    private boolean isRotationDirty;


    public ScreenOverlay()
    {
        super();
    }

    public ScreenOverlay(Node parent)
    {
        super(parent);
    }

    public vec2 getOverlayXY()
    {
        return this.overlayXY;
    }

    public void addOverlayXY(vec2 value)
    {
        if(this.overlayXY!=null)
        {
            markDeletedNode(this.overlayXY);
        }
        this.overlayXY = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public vec2 getScreenXY()
    {
        return this.screenXY;
    }

    public void addScreenXY(vec2 value)
    {
        if(this.screenXY!=null)
        {
            markDeletedNode(this.screenXY);
        }
        this.screenXY = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public vec2 getRotationXY()
    {
        return this.rotationXY;
    }

    public void addRotationXY(vec2 value)
    {
        if(this.rotationXY!=null)
        {
            markDeletedNode(this.rotationXY);
        }
        this.rotationXY = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public vec2 getSize()
    {
        return this.size;
    }

    public void addSize(vec2 value)
    {
        if(this.size!=null)
        {
            markDeletedNode(this.size);
        }
        this.size = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public float getRotation()
    {
        return this.rotation;
    }

    public void setRotation(float value)
    {
        this.rotation = value;
        this.isRotationDirty = true;
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
        kml+="<ScreenOverlay";
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
        if(this.overlayXY!=null)
        {
            kml+=this.overlayXY.toKML();
        }
        if(this.screenXY!=null)
        {
            kml+=this.screenXY.toKML();
        }
        if(this.rotationXY!=null)
        {
            kml+=this.rotationXY.toKML();
        }
        if(this.size!=null)
        {
            kml+=this.size.toKML();
        }
        if(this.rotation!=DEFAULT_ROTATION)
        {
            kml+="<rotation>"+this.rotation+"</rotation>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</ScreenOverlay>\n";
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
        change+="<ScreenOverlay";
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
        if(this.overlayXY!=null && this.overlayXY.isDirty())
        {
            change+=this.overlayXY.toUpdateKML();
        }
        if(this.screenXY!=null && this.screenXY.isDirty())
        {
            change+=this.screenXY.toUpdateKML();
        }
        if(this.rotationXY!=null && this.rotationXY.isDirty())
        {
            change+=this.rotationXY.toUpdateKML();
        }
        if(this.size!=null && this.size.isDirty())
        {
            change+=this.size.toUpdateKML();
        }
        if(this.isRotationDirty)
        {
            change+="<rotation>"+this.rotation+"</rotation>\n";
            this.isRotationDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</ScreenOverlay>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        ScreenOverlay result = (ScreenOverlay)super.clone();
      if(result.overlayXY!=null)
      {
        result.overlayXY = (vec2)this.overlayXY.clone();
        result.overlayXY.setParent(result);
      }
      if(result.screenXY!=null)
      {
        result.screenXY = (vec2)this.screenXY.clone();
        result.screenXY.setParent(result);
      }
      if(result.rotationXY!=null)
      {
        result.rotationXY = (vec2)this.rotationXY.clone();
        result.rotationXY.setParent(result);
      }
      if(result.size!=null)
      {
        result.size = (vec2)this.size.clone();
        result.size.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.overlayXY!=null && this.overlayXY.isDirty())
        {
            this.overlayXY.setRecursiveNotDirty();
        }
        if(this.screenXY!=null && this.screenXY.isDirty())
        {
            this.screenXY.setRecursiveNotDirty();
        }
        if(this.rotationXY!=null && this.rotationXY.isDirty())
        {
            this.rotationXY.setRecursiveNotDirty();
        }
        if(this.size!=null && this.size.isDirty())
        {
            this.size.setRecursiveNotDirty();
        }
        this.isRotationDirty = false;
    }
}
