package com.beenthere.kml;



public class GroundOverlay extends Overlay
{
    protected double altitude;
    private boolean isAltitudeDirty;
    protected String altitudeMode;
    private boolean isAltitudeModeDirty;
    protected LatLonBox latLonBox;


    public GroundOverlay()
    {
        super();
    }

    public GroundOverlay(Node parent)
    {
        super(parent);
    }

    public double getAltitude()
    {
        return this.altitude;
    }

    public void setAltitude(double value)
    {
        this.altitude = value;
        this.isAltitudeDirty = true;
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

    public LatLonBox getLatLonBox()
    {
        return this.latLonBox;
    }

    public void addLatLonBox(LatLonBox value)
    {
        if(this.latLonBox!=null)
        {
            markDeletedNode(this.latLonBox);
        }
        this.latLonBox = value;
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
        kml+="<GroundOverlay";
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
        kml+="<altitude>"+this.altitude+"</altitude>\n";
      if(this.altitudeMode!=null)
      {
            kml+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
      }
        if(this.latLonBox!=null)
        {
            kml+=this.latLonBox.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</GroundOverlay>\n";
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
        change+="<GroundOverlay";
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
        if(this.isAltitudeDirty)
        {
            change+="<altitude>"+this.altitude+"</altitude>\n";
            this.isAltitudeDirty = false;
        }
        if(this.altitudeMode!=null && this.isAltitudeModeDirty)
        {
            change+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
            this.isAltitudeModeDirty = false;
        }
        if(this.latLonBox!=null && this.latLonBox.isDirty())
        {
            change+=this.latLonBox.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</GroundOverlay>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        GroundOverlay result = (GroundOverlay)super.clone();
      if(result.latLonBox!=null)
      {
        result.latLonBox = (LatLonBox)this.latLonBox.clone();
        result.latLonBox.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isAltitudeDirty = false;
        this.isAltitudeModeDirty = false;
        if(this.latLonBox!=null && this.latLonBox.isDirty())
        {
            this.latLonBox.setRecursiveNotDirty();
        }
    }
}
