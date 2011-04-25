package com.beenthere.kml;

public class Orientation extends ObjectNode
{
    protected double heading;
    private boolean isHeadingDirty;
    protected double tilt;
    private boolean isTiltDirty;
    protected double roll;
    private boolean isRollDirty;


    public Orientation()
    {
        super();
    }

    public Orientation(Node parent)
    {
        super(parent);
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

    public double getRoll()
    {
        return this.roll;
    }

    public void setRoll(double value)
    {
        this.roll = value;
        this.isRollDirty = true;
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
        kml+="<Orientation";
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
        kml+="<heading>"+this.heading+"</heading>\n";
        kml+="<tilt>"+this.tilt+"</tilt>\n";
        kml+="<roll>"+this.roll+"</roll>\n";
        if(!suppressEnclosingTags)
        {
            kml+="</Orientation>\n";
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
        change+="<Orientation";
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
        if(this.isHeadingDirty)
        {
            change+="<heading>"+this.heading+"</heading>\n";
            this.isHeadingDirty = false;
        }
        if(this.isTiltDirty)
        {
            change+="<tilt>"+this.tilt+"</tilt>\n";
            this.isTiltDirty = false;
        }
        if(this.isRollDirty)
        {
            change+="<roll>"+this.roll+"</roll>\n";
            this.isRollDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Orientation>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Orientation result = (Orientation)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isHeadingDirty = false;
        this.isTiltDirty = false;
        this.isRollDirty = false;
    }
}
