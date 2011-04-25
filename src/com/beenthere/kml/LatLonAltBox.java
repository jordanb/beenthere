package com.beenthere.kml;

public class LatLonAltBox extends LatLonBox
{
    public static double DEFAULT_MINALTITUDE=0;
    protected double minAltitude = DEFAULT_MINALTITUDE;
    private boolean isMinAltitudeDirty;
    public static double DEFAULT_MAXALTITUDE=0;
    protected double maxAltitude = DEFAULT_MAXALTITUDE;
    private boolean isMaxAltitudeDirty;
    protected String altitudeMode;
    private boolean isAltitudeModeDirty;


    public LatLonAltBox()
    {
        super();
    }

    public LatLonAltBox(Node parent)
    {
        super(parent);
    }

    public double getMinAltitude()
    {
        return this.minAltitude;
    }

    public void setMinAltitude(double value)
    {
        this.minAltitude = value;
        this.isMinAltitudeDirty = true;
        setDirty();
    }

    public double getMaxAltitude()
    {
        return this.maxAltitude;
    }

    public void setMaxAltitude(double value)
    {
        this.maxAltitude = value;
        this.isMaxAltitudeDirty = true;
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
        kml+="<LatLonAltBox";
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
        if(this.minAltitude!=DEFAULT_MINALTITUDE)
        {
            kml+="<minAltitude>"+this.minAltitude+"</minAltitude>\n";
        }
        if(this.maxAltitude!=DEFAULT_MAXALTITUDE)
        {
            kml+="<maxAltitude>"+this.maxAltitude+"</maxAltitude>\n";
        }
      if(this.altitudeMode!=null)
      {
            kml+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</LatLonAltBox>\n";
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
        change+="<LatLonAltBox";
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
        if(this.isMinAltitudeDirty)
        {
            change+="<minAltitude>"+this.minAltitude+"</minAltitude>\n";
            this.isMinAltitudeDirty = false;
        }
        if(this.isMaxAltitudeDirty)
        {
            change+="<maxAltitude>"+this.maxAltitude+"</maxAltitude>\n";
            this.isMaxAltitudeDirty = false;
        }
        if(this.altitudeMode!=null && this.isAltitudeModeDirty)
        {
            change+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
            this.isAltitudeModeDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</LatLonAltBox>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        LatLonAltBox result = (LatLonAltBox)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isMinAltitudeDirty = false;
        this.isMaxAltitudeDirty = false;
        this.isAltitudeModeDirty = false;
    }
}
