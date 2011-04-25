package com.beenthere.kml;

public class Polygon extends Geometry
{
    public static boolean DEFAULT_EXTRUDE=false;
    protected boolean extrude = DEFAULT_EXTRUDE;
    private boolean isExtrudeDirty;
    public static boolean DEFAULT_TESSELLATE=false;
    protected boolean tessellate = DEFAULT_TESSELLATE;
    private boolean isTessellateDirty;
    protected String altitudeMode;
    private boolean isAltitudeModeDirty;
    protected boundary outerBoundaryIs;
    protected boundary innerBoundaryIs;


    public Polygon()
    {
        super();
    }

    public Polygon(Node parent)
    {
        super(parent);
    }

    public boolean getExtrude()
    {
        return this.extrude;
    }

    public void setExtrude(boolean value)
    {
        this.extrude = value;
        this.isExtrudeDirty = true;
        setDirty();
    }

    public boolean getTessellate()
    {
        return this.tessellate;
    }

    public void setTessellate(boolean value)
    {
        this.tessellate = value;
        this.isTessellateDirty = true;
        setDirty();
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

    public boundary getOuterBoundaryIs()
    {
        return this.outerBoundaryIs;
    }

    public void addOuterBoundaryIs(boundary value)
    {
        if(this.outerBoundaryIs!=null)
        {
            markDeletedNode(this.outerBoundaryIs);
        }
        this.outerBoundaryIs = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public boundary getInnerBoundaryIs()
    {
        return this.innerBoundaryIs;
    }

    public void addInnerBoundaryIs(boundary value)
    {
        if(this.innerBoundaryIs!=null)
        {
            markDeletedNode(this.innerBoundaryIs);
        }
        this.innerBoundaryIs = value;
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
        kml+="<Polygon";
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
        if(this.extrude!=DEFAULT_EXTRUDE)
        {
            kml+="<extrude>"+this.extrude+"</extrude>\n";
        }
        if(this.tessellate!=DEFAULT_TESSELLATE)
        {
            kml+="<tessellate>"+this.tessellate+"</tessellate>\n";
        }
      if(this.altitudeMode!=null)
      {
            kml+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
      }
        if(this.outerBoundaryIs!=null)
        {
            kml+=this.outerBoundaryIs.toKML();
        }
        if(this.innerBoundaryIs!=null)
        {
            kml+=this.innerBoundaryIs.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Polygon>\n";
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
        change+="<Polygon";
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
        if(this.isExtrudeDirty)
        {
            change+="<extrude>"+this.extrude+"</extrude>\n";
            this.isExtrudeDirty = false;
        }
        if(this.isTessellateDirty)
        {
            change+="<tessellate>"+this.tessellate+"</tessellate>\n";
            this.isTessellateDirty = false;
        }
        if(this.altitudeMode!=null && this.isAltitudeModeDirty)
        {
            change+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
            this.isAltitudeModeDirty = false;
        }
        if(this.outerBoundaryIs!=null && this.outerBoundaryIs.isDirty())
        {
            change+=this.outerBoundaryIs.toUpdateKML();
        }
        if(this.innerBoundaryIs!=null && this.innerBoundaryIs.isDirty())
        {
            change+=this.innerBoundaryIs.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Polygon>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Polygon result = (Polygon)super.clone();
      if(result.outerBoundaryIs!=null)
      {
        result.outerBoundaryIs = (boundary)this.outerBoundaryIs.clone();
        result.outerBoundaryIs.setParent(result);
      }
      if(result.innerBoundaryIs!=null)
      {
        result.innerBoundaryIs = (boundary)this.innerBoundaryIs.clone();
        result.innerBoundaryIs.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isExtrudeDirty = false;
        this.isTessellateDirty = false;
        this.isAltitudeModeDirty = false;
        if(this.outerBoundaryIs!=null && this.outerBoundaryIs.isDirty())
        {
            this.outerBoundaryIs.setRecursiveNotDirty();
        }
        if(this.innerBoundaryIs!=null && this.innerBoundaryIs.isDirty())
        {
            this.innerBoundaryIs.setRecursiveNotDirty();
        }
    }
}
