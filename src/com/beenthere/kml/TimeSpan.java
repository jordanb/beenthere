package com.beenthere.kml;

public class TimeSpan extends TimePrimitive
{
    protected LongDate begin;
    private boolean isBeginDirty;
    protected LongDate end;
    private boolean isEndDirty;


    public TimeSpan()
    {
        super();
    }

    public TimeSpan(Node parent)
    {
        super(parent);
    }

    public LongDate getBegin()
    {
        return this.begin;
    }

    public void setBegin(String date)
    {
        setBegin(new LongDate(date));
    }

    public void setBegin(LongDate aDate)
    {
        this.begin = aDate;
        this.isBeginDirty = true;
        setDirty();
    }

    public LongDate getEnd()
    {
        return this.end;
    }

    public void setEnd(String date)
    {
        setEnd(new LongDate(date));
    }

    public void setEnd(LongDate aDate)
    {
        this.end = aDate;
        this.isEndDirty = true;
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
        kml+="<TimeSpan";
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
      if(this.begin!=null)
      {
            kml+="<begin>"+SpecialCaseFormatter.toKMLString(this.begin)+"</begin>\n";
      }
      if(this.end!=null)
      {
            kml+="<end>"+SpecialCaseFormatter.toKMLString(this.end)+"</end>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</TimeSpan>\n";
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
        change+="<TimeSpan";
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
        if(this.begin!=null && this.isBeginDirty)
        {
            change+="<begin>"+SpecialCaseFormatter.toKMLString(this.begin)+"</begin>\n";
            this.isBeginDirty = false;
        }
        if(this.end!=null && this.isEndDirty)
        {
            change+="<end>"+SpecialCaseFormatter.toKMLString(this.end)+"</end>\n";
            this.isEndDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</TimeSpan>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        TimeSpan result = (TimeSpan)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isBeginDirty = false;
        this.isEndDirty = false;
    }
}
