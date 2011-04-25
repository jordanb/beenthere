package com.beenthere.kml;

public class Location extends ObjectNode
{
    protected double longitude;
    private boolean isLongitudeDirty;
    protected double latitude;
    private boolean isLatitudeDirty;
    public static double DEFAULT_ALTITUDE=0;
    protected double altitude = DEFAULT_ALTITUDE;
    private boolean isAltitudeDirty;


    public Location()
    {
        super();
    }

    public Location(Node parent)
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



    public String toKML()
    {
        return toKML(false);
    }
    public String toKML(boolean suppressEnclosingTags)
    {
        String kml="";
        if(!suppressEnclosingTags)
        {
        kml+="<Location";
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
        if(this.altitude!=DEFAULT_ALTITUDE)
        {
            kml+="<altitude>"+this.altitude+"</altitude>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Location>\n";
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
        change+="<Location";
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
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Location>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Location result = (Location)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isLongitudeDirty = false;
        this.isLatitudeDirty = false;
        this.isAltitudeDirty = false;
    }
}
