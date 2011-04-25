package com.beenthere.kml;

public class LookAt extends ObjectNode
{
    protected double longitude;
    private boolean isLongitudeDirty;
    protected double latitude;
    private boolean isLatitudeDirty;
    protected double altitude;
    private boolean isAltitudeDirty;
    protected double range;
    private boolean isRangeDirty;
    protected double tilt;
    private boolean isTiltDirty;
    protected double heading;
    private boolean isHeadingDirty;
    protected String altitudeMode;
    private boolean isAltitudeModeDirty;


    public LookAt()
    {
        super();
    }

    public LookAt(Node parent)
    {
        super(parent);
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public void setLongitude(double value)
    {
        this.longitude = value;
        this.isLongitudeDirty = true;
        setDirty();
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public void setLatitude(double value)
    {
        this.latitude = value;
        this.isLatitudeDirty = true;
        setDirty();
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

    public double getRange()
    {
        return this.range;
    }

    public void setRange(double value)
    {
        this.range = value;
        this.isRangeDirty = true;
        setDirty();
    }

    public double getTilt()
    {
        return this.tilt;
    }

    public void setTilt(double value)
    {
        this.tilt = value;
        this.isTiltDirty = true;
        setDirty();
    }

    public double getHeading()
    {
        return this.heading;
    }

    public void setHeading(double value)
    {
        this.heading = value;
        this.isHeadingDirty = true;
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



    public String toKML()
    {
        return toKML(false);
    }
    public String toKML(boolean suppressEnclosingTags)
    {
        String kml="";
        if(!suppressEnclosingTags)
        {
        kml+="<LookAt";
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
        kml+="<longitude>"+this.longitude+"</longitude>\n";
        kml+="<latitude>"+this.latitude+"</latitude>\n";
        kml+="<altitude>"+this.altitude+"</altitude>\n";
        kml+="<range>"+this.range+"</range>\n";
        kml+="<tilt>"+this.tilt+"</tilt>\n";
        kml+="<heading>"+this.heading+"</heading>\n";
      if(this.altitudeMode!=null)
      {
            kml+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</LookAt>\n";
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
        change+="<LookAt";
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
        if(this.isLongitudeDirty)
        {
            change+="<longitude>"+this.longitude+"</longitude>\n";
            this.isLongitudeDirty = false;
        }
        if(this.isLatitudeDirty)
        {
            change+="<latitude>"+this.latitude+"</latitude>\n";
            this.isLatitudeDirty = false;
        }
        if(this.isAltitudeDirty)
        {
            change+="<altitude>"+this.altitude+"</altitude>\n";
            this.isAltitudeDirty = false;
        }
        if(this.isRangeDirty)
        {
            change+="<range>"+this.range+"</range>\n";
            this.isRangeDirty = false;
        }
        if(this.isTiltDirty)
        {
            change+="<tilt>"+this.tilt+"</tilt>\n";
            this.isTiltDirty = false;
        }
        if(this.isHeadingDirty)
        {
            change+="<heading>"+this.heading+"</heading>\n";
            this.isHeadingDirty = false;
        }
        if(this.altitudeMode!=null && this.isAltitudeModeDirty)
        {
            change+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
            this.isAltitudeModeDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</LookAt>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        LookAt result = (LookAt)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isLongitudeDirty = false;
        this.isLatitudeDirty = false;
        this.isAltitudeDirty = false;
        this.isRangeDirty = false;
        this.isTiltDirty = false;
        this.isHeadingDirty = false;
        this.isAltitudeModeDirty = false;
    }
}
