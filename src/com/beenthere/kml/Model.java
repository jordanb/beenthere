package com.beenthere.kml;

public class Model extends Geometry
{
    protected String altitudeMode;
    private boolean isAltitudeModeDirty;
    protected Location location;
    protected Orientation orientation;
    protected Scale scale;
    protected Link link;


    public Model()
    {
        super();
    }

    public Model(Node parent)
    {
        super(parent);
    }

    public String getAltitudeMode()
    {
        return this.altitudeMode;
    }

    public void setAltitudeMode(String value)
    {
        this.altitudeMode = value;
        this.isAltitudeModeDirty = true;
        setDirty();
    }

    public Location getLocation()
    {
        return this.location;
    }

    public void addLocation(Location value)
    {
        if(this.location!=null)
        {
            markDeletedNode(this.location);
        }
        this.location = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Orientation getOrientation()
    {
        return this.orientation;
    }

    public void addOrientation(Orientation value)
    {
        if(this.orientation!=null)
        {
            markDeletedNode(this.orientation);
        }
        this.orientation = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Scale getScale()
    {
        return this.scale;
    }

    public void addScale(Scale value)
    {
        if(this.scale!=null)
        {
            markDeletedNode(this.scale);
        }
        this.scale = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Link getLink()
    {
        return this.link;
    }

    public void addLink(Link value)
    {
        if(this.link!=null)
        {
            markDeletedNode(this.link);
        }
        this.link = value;
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
        kml+="<Model";
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
      if(this.altitudeMode!=null)
      {
            kml+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
      }
        if(this.location!=null)
        {
            kml+=this.location.toKML();
        }
        if(this.orientation!=null)
        {
            kml+=this.orientation.toKML();
        }
        if(this.scale!=null)
        {
            kml+=this.scale.toKML();
        }
        if(this.link!=null)
        {
            kml+=this.link.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Model>\n";
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
        change+="<Model";
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
        if(this.altitudeMode!=null && this.isAltitudeModeDirty)
        {
            change+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
            this.isAltitudeModeDirty = false;
        }
        if(this.location!=null && this.location.isDirty())
        {
            change+=this.location.toUpdateKML();
        }
        if(this.orientation!=null && this.orientation.isDirty())
        {
            change+=this.orientation.toUpdateKML();
        }
        if(this.scale!=null && this.scale.isDirty())
        {
            change+=this.scale.toUpdateKML();
        }
        if(this.link!=null && this.link.isDirty())
        {
            change+=this.link.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Model>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Model result = (Model)super.clone();
      if(result.location!=null)
      {
        result.location = (Location)this.location.clone();
        result.location.setParent(result);
      }
      if(result.orientation!=null)
      {
        result.orientation = (Orientation)this.orientation.clone();
        result.orientation.setParent(result);
      }
      if(result.scale!=null)
      {
        result.scale = (Scale)this.scale.clone();
        result.scale.setParent(result);
      }
      if(result.link!=null)
      {
        result.link = (Link)this.link.clone();
        result.link.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isAltitudeModeDirty = false;
        if(this.location!=null && this.location.isDirty())
        {
            this.location.setRecursiveNotDirty();
        }
        if(this.orientation!=null && this.orientation.isDirty())
        {
            this.orientation.setRecursiveNotDirty();
        }
        if(this.scale!=null && this.scale.isDirty())
        {
            this.scale.setRecursiveNotDirty();
        }
        if(this.link!=null && this.link.isDirty())
        {
            this.link.setRecursiveNotDirty();
        }
    }
}
