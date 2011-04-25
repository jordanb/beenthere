package com.beenthere.kml;

public class StyleMap extends StyleSelector
{
    protected StyleMapPair pair;


    public StyleMap()
    {
        super();
    }

    public StyleMap(Node parent)
    {
        super(parent);
    }

    public StyleMapPair getPair()
    {
        return this.pair;
    }

    public void addPair(StyleMapPair value)
    {
        if(this.pair!=null)
        {
            markDeletedNode(this.pair);
        }
        this.pair = value;
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
        kml+="<StyleMap";
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
        if(this.pair!=null)
        {
            kml+=this.pair.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</StyleMap>\n";
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
        change+="<StyleMap";
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
        if(this.pair!=null && this.pair.isDirty())
        {
            change+=this.pair.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</StyleMap>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        StyleMap result = (StyleMap)super.clone();
      if(result.pair!=null)
      {
        result.pair = (StyleMapPair)this.pair.clone();
        result.pair.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.pair!=null && this.pair.isDirty())
        {
            this.pair.setRecursiveNotDirty();
        }
    }
}
