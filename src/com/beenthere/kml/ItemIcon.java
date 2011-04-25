package com.beenthere.kml;

public class ItemIcon extends ObjectNode
{
    protected String state;
    private boolean isStateDirty;
    protected String href;
    private boolean isHrefDirty;


    public ItemIcon()
    {
        super();
    }

    public ItemIcon(Node parent)
    {
        super(parent);
    }

    public String getState()
    {
        return this.state;
    }

    public void setState(String value)
    {
        this.state = value;
        this.isStateDirty = true;
        setDirty();
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
        kml+="<ItemIcon";
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
      if(this.state!=null)
      {
            kml+="<state>"+SpecialCaseFormatter.toKMLString(this.state)+"</state>\n";
      }
      if(this.href!=null)
      {
            kml+="<href>"+SpecialCaseFormatter.toKMLString(this.href)+"</href>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</ItemIcon>\n";
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
        change+="<ItemIcon";
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
        if(this.state!=null && this.isStateDirty)
        {
            change+="<state>"+SpecialCaseFormatter.toKMLString(this.state)+"</state>\n";
            this.isStateDirty = false;
        }
        if(this.href!=null && this.isHrefDirty)
        {
            change+="<href>"+SpecialCaseFormatter.toKMLString(this.href)+"</href>\n";
            this.isHrefDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</ItemIcon>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        ItemIcon result = (ItemIcon)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isStateDirty = false;
        this.isHrefDirty = false;
    }
}
