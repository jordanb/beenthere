package com.beenthere.kml;


public class IconStyleIcon extends ObjectNode
{
    protected String href;
    private boolean isHrefDirty;


    public IconStyleIcon()
    {
        super();
    }

    public IconStyleIcon(Node parent)
    {
        super(parent);
    }

    public String getHref()
    {
        return this.href;
    }

    public void setHref(String value)
    {
        this.href = value;
        this.isHrefDirty = true;
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
        kml+="<IconStyleIcon";
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
      if(this.href!=null)
      {
            kml+="<href>"+SpecialCaseFormatter.toKMLString(this.href)+"</href>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</IconStyleIcon>\n";
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
        change+="<IconStyleIcon";
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
        if(this.href!=null && this.isHrefDirty)
        {
            change+="<href>"+SpecialCaseFormatter.toKMLString(this.href)+"</href>\n";
            this.isHrefDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</IconStyleIcon>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        IconStyleIcon result = (IconStyleIcon)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isHrefDirty = false;
    }
}
