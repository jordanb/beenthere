package com.beenthere.kml;

public class Link extends ObjectNode
{
    protected String href;
    private boolean isHrefDirty;
    protected String refreshMode;
    private boolean isRefreshModeDirty;
    protected float refreshInterval;
    private boolean isRefreshIntervalDirty;
    protected String viewRefreshMode;
    private boolean isViewRefreshModeDirty;
    protected float viewRefreshTime;
    private boolean isViewRefreshTimeDirty;
    protected float viewBoundScale;
    private boolean isViewBoundScaleDirty;
    protected String viewFormat;
    private boolean isViewFormatDirty;
    protected String httpQuery;
    private boolean isHttpQueryDirty;


    public Link()
    {
        super();
    }

    public Link(Node parent)
    {
        super(parent);
    }

    public String getHref()
    {
        return this.href;
    }

    public void setHref(String value)
    {
        this.href = value;
        this.isHrefDirty = true;
        setDirty();
    }

    public String getRefreshMode()
    {
        return this.refreshMode;
    }

    public void setRefreshMode(String value)
    {
        this.refreshMode = value;
        this.isRefreshModeDirty = true;
        setDirty();
    }

    public float getRefreshInterval()
    {
        return this.refreshInterval;
    }

    public void setRefreshInterval(float value)
    {
        this.refreshInterval = value;
        this.isRefreshIntervalDirty = true;
        setDirty();
    }

    public String getViewRefreshMode()
    {
        return this.viewRefreshMode;
    }

    public void setViewRefreshMode(String value)
    {
        this.viewRefreshMode = value;
        this.isViewRefreshModeDirty = true;
        setDirty();
    }

    public float getViewRefreshTime()
    {
        return this.viewRefreshTime;
    }

    public void setViewRefreshTime(float value)
    {
        this.viewRefreshTime = value;
        this.isViewRefreshTimeDirty = true;
        setDirty();
    }

    public float getViewBoundScale()
    {
        return this.viewBoundScale;
    }

    public void setViewBoundScale(float value)
    {
        this.viewBoundScale = value;
        this.isViewBoundScaleDirty = true;
        setDirty();
    }

    public String getViewFormat()
    {
        return this.viewFormat;
    }

    public void setViewFormat(String value)
    {
        this.viewFormat = value;
        this.isViewFormatDirty = true;
        setDirty();
    }

    public String getHttpQuery()
    {
        return this.httpQuery;
    }

    public void setHttpQuery(String value)
    {
        this.httpQuery = value;
        this.isHttpQueryDirty = true;
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
        kml+="<Link";
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
      if(this.href!=null)
      {
            kml+="<href>"+SpecialCaseFormatter.toKMLString(this.href)+"</href>\n";
      }
      if(this.refreshMode!=null)
      {
            kml+="<refreshMode>"+SpecialCaseFormatter.toKMLString(this.refreshMode)+"</refreshMode>\n";
      }
        kml+="<refreshInterval>"+this.refreshInterval+"</refreshInterval>\n";
      if(this.viewRefreshMode!=null)
      {
            kml+="<viewRefreshMode>"+SpecialCaseFormatter.toKMLString(this.viewRefreshMode)+"</viewRefreshMode>\n";
      }
        kml+="<viewRefreshTime>"+this.viewRefreshTime+"</viewRefreshTime>\n";
        kml+="<viewBoundScale>"+this.viewBoundScale+"</viewBoundScale>\n";
      if(this.viewFormat!=null)
      {
            kml+="<viewFormat>"+SpecialCaseFormatter.toKMLString(this.viewFormat)+"</viewFormat>\n";
      }
      if(this.httpQuery!=null)
      {
            kml+="<httpQuery>"+SpecialCaseFormatter.toKMLString(this.httpQuery)+"</httpQuery>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</Link>\n";
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
        change+="<Link";
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
        if(this.href!=null && this.isHrefDirty)
        {
            change+="<href>"+SpecialCaseFormatter.toKMLString(this.href)+"</href>\n";
            this.isHrefDirty = false;
        }
        if(this.refreshMode!=null && this.isRefreshModeDirty)
        {
            change+="<refreshMode>"+SpecialCaseFormatter.toKMLString(this.refreshMode)+"</refreshMode>\n";
            this.isRefreshModeDirty = false;
        }
        if(this.isRefreshIntervalDirty)
        {
            change+="<refreshInterval>"+this.refreshInterval+"</refreshInterval>\n";
            this.isRefreshIntervalDirty = false;
        }
        if(this.viewRefreshMode!=null && this.isViewRefreshModeDirty)
        {
            change+="<viewRefreshMode>"+SpecialCaseFormatter.toKMLString(this.viewRefreshMode)+"</viewRefreshMode>\n";
            this.isViewRefreshModeDirty = false;
        }
        if(this.isViewRefreshTimeDirty)
        {
            change+="<viewRefreshTime>"+this.viewRefreshTime+"</viewRefreshTime>\n";
            this.isViewRefreshTimeDirty = false;
        }
        if(this.isViewBoundScaleDirty)
        {
            change+="<viewBoundScale>"+this.viewBoundScale+"</viewBoundScale>\n";
            this.isViewBoundScaleDirty = false;
        }
        if(this.viewFormat!=null && this.isViewFormatDirty)
        {
            change+="<viewFormat>"+SpecialCaseFormatter.toKMLString(this.viewFormat)+"</viewFormat>\n";
            this.isViewFormatDirty = false;
        }
        if(this.httpQuery!=null && this.isHttpQueryDirty)
        {
            change+="<httpQuery>"+SpecialCaseFormatter.toKMLString(this.httpQuery)+"</httpQuery>\n";
            this.isHttpQueryDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Link>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Link result = (Link)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isHrefDirty = false;
        this.isRefreshModeDirty = false;
        this.isRefreshIntervalDirty = false;
        this.isViewRefreshModeDirty = false;
        this.isViewRefreshTimeDirty = false;
        this.isViewBoundScaleDirty = false;
        this.isViewFormatDirty = false;
        this.isHttpQueryDirty = false;
    }
}
