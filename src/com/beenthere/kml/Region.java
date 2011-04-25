package com.beenthere.kml;

public class Region extends ObjectNode
{
    protected LatLonAltBox latLonAltBox;
    protected Lod lod;


    public Region()
    {
        super();
    }

    public Region(Node parent)
    {
        super(parent);
    }

    public LatLonAltBox getLatLonAltBox()
    {
        return this.latLonAltBox;
    }

    public void addLatLonAltBox(LatLonAltBox value)
    {
        if(this.latLonAltBox!=null)
        {
            markDeletedNode(this.latLonAltBox);
        }
        this.latLonAltBox = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Lod getLod()
    {
        return this.lod;
    }

    public void addLod(Lod value)
    {
        if(this.lod!=null)
        {
            markDeletedNode(this.lod);
        }
        this.lod = value;
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
        kml+="<Region";
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
        if(this.latLonAltBox!=null)
        {
            kml+=this.latLonAltBox.toKML();
        }
        if(this.lod!=null)
        {
            kml+=this.lod.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Region>\n";
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
        change+="<Region";
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
        if(this.latLonAltBox!=null && this.latLonAltBox.isDirty())
        {
            change+=this.latLonAltBox.toUpdateKML();
        }
        if(this.lod!=null && this.lod.isDirty())
        {
            change+=this.lod.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Region>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Region result = (Region)super.clone();
      if(result.latLonAltBox!=null)
      {
        result.latLonAltBox = (LatLonAltBox)this.latLonAltBox.clone();
        result.latLonAltBox.setParent(result);
      }
      if(result.lod!=null)
      {
        result.lod = (Lod)this.lod.clone();
        result.lod.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.latLonAltBox!=null && this.latLonAltBox.isDirty())
        {
            this.latLonAltBox.setRecursiveNotDirty();
        }
        if(this.lod!=null && this.lod.isDirty())
        {
            this.lod.setRecursiveNotDirty();
        }
    }
}
