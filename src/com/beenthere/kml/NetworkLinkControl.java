package com.beenthere.kml;

public class NetworkLinkControl extends Node
{
    public static float DEFAULT_MINREFRESHPERIOD=0;
    protected float minRefreshPeriod = DEFAULT_MINREFRESHPERIOD;
    private boolean isMinRefreshPeriodDirty;
    protected String cookie;
    private boolean isCookieDirty;
    protected String message;
    private boolean isMessageDirty;
    protected String linkName;
    private boolean isLinkNameDirty;
    protected String linkDescription;
    private boolean isLinkDescriptionDirty;
    protected String linkSnippet;
    private boolean isLinkSnippetDirty;
    protected LongDate expires;
    private boolean isExpiresDirty;
    protected Update update;
    protected LookAt lookAt;


    public NetworkLinkControl()
    {
        super();
    }

    public NetworkLinkControl(Node parent)
    {
        super(parent);
    }

    public float getMinRefreshPeriod()
    {
        return this.minRefreshPeriod;
    }

    public void setMinRefreshPeriod(float value)
    {
        this.minRefreshPeriod = value;
        this.isMinRefreshPeriodDirty = true;
        setDirty();
    }

    public String getCookie()
    {
        return this.cookie;
    }

    public void setCookie(String value)
    {
        this.cookie = value;
        this.isCookieDirty = true;
        setDirty();
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String value)
    {
        this.message = value;
        this.isMessageDirty = true;
        setDirty();
    }

    public String getLinkName()
    {
        return this.linkName;
    }

    public void setLinkName(String value)
    {
        this.linkName = value;
        this.isLinkNameDirty = true;
        setDirty();
    }

    public String getLinkDescription()
    {
        return this.linkDescription;
    }

    public void setLinkDescription(String value)
    {
        this.linkDescription = value;
        this.isLinkDescriptionDirty = true;
        setDirty();
    }

    public String getLinkSnippet()
    {
        return this.linkSnippet;
    }

    public void setLinkSnippet(String value)
    {
        this.linkSnippet = value;
        this.isLinkSnippetDirty = true;
        setDirty();
    }

    public LongDate getExpires()
    {
        return this.expires;
    }

    public void setExpires(String date)
    {
        setExpires(new LongDate(date));
    }

    public void setExpires(LongDate aDate)
    {
        this.expires = aDate;
        this.isExpiresDirty = true;
        setDirty();
    }

    public Update getUpdate()
    {
        return this.update;
    }

    public void addUpdate(Update value)
    {
        if(this.update!=null)
        {
            markDeletedNode(this.update);
        }
        this.update = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public LookAt getLookAt()
    {
        return this.lookAt;
    }

    public void addLookAt(LookAt value)
    {
        if(this.lookAt!=null)
        {
            markDeletedNode(this.lookAt);
        }
        this.lookAt = value;
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
        kml+="<NetworkLinkControl";
        kml+=">\n";
        }
        kml+=super.toKML(true);
        if(this.minRefreshPeriod!=DEFAULT_MINREFRESHPERIOD)
        {
            kml+="<minRefreshPeriod>"+this.minRefreshPeriod+"</minRefreshPeriod>\n";
        }
      if(this.cookie!=null)
      {
            kml+="<cookie>"+SpecialCaseFormatter.toKMLString(this.cookie)+"</cookie>\n";
      }
      if(this.message!=null)
      {
            kml+="<message>"+SpecialCaseFormatter.toKMLString(this.message)+"</message>\n";
      }
      if(this.linkName!=null)
      {
            kml+="<linkName>"+SpecialCaseFormatter.toKMLString(this.linkName)+"</linkName>\n";
      }
      if(this.linkDescription!=null)
      {
            kml+="<linkDescription>"+SpecialCaseFormatter.toKMLString(this.linkDescription)+"</linkDescription>\n";
      }
      if(this.linkSnippet!=null)
      {
            kml+="<linkSnippet>"+SpecialCaseFormatter.toKMLString(this.linkSnippet)+"</linkSnippet>\n";
      }
      if(this.expires!=null)
      {
            kml+="<expires>"+SpecialCaseFormatter.toKMLString(this.expires)+"</expires>\n";
      }
        if(this.update!=null)
        {
            kml+=this.update.toKML();
        }
        if(this.lookAt!=null)
        {
            kml+=this.lookAt.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</NetworkLinkControl>\n";
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
        change+="<NetworkLinkControl";
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.isMinRefreshPeriodDirty)
        {
            change+="<minRefreshPeriod>"+this.minRefreshPeriod+"</minRefreshPeriod>\n";
            this.isMinRefreshPeriodDirty = false;
        }
        if(this.cookie!=null && this.isCookieDirty)
        {
            change+="<cookie>"+SpecialCaseFormatter.toKMLString(this.cookie)+"</cookie>\n";
            this.isCookieDirty = false;
        }
        if(this.message!=null && this.isMessageDirty)
        {
            change+="<message>"+SpecialCaseFormatter.toKMLString(this.message)+"</message>\n";
            this.isMessageDirty = false;
        }
        if(this.linkName!=null && this.isLinkNameDirty)
        {
            change+="<linkName>"+SpecialCaseFormatter.toKMLString(this.linkName)+"</linkName>\n";
            this.isLinkNameDirty = false;
        }
        if(this.linkDescription!=null && this.isLinkDescriptionDirty)
        {
            change+="<linkDescription>"+SpecialCaseFormatter.toKMLString(this.linkDescription)+"</linkDescription>\n";
            this.isLinkDescriptionDirty = false;
        }
        if(this.linkSnippet!=null && this.isLinkSnippetDirty)
        {
            change+="<linkSnippet>"+SpecialCaseFormatter.toKMLString(this.linkSnippet)+"</linkSnippet>\n";
            this.isLinkSnippetDirty = false;
        }
        if(this.expires!=null && this.isExpiresDirty)
        {
            change+="<expires>"+SpecialCaseFormatter.toKMLString(this.expires)+"</expires>\n";
            this.isExpiresDirty = false;
        }
        if(this.update!=null && this.update.isDirty())
        {
            change+=this.update.toUpdateKML();
        }
        if(this.lookAt!=null && this.lookAt.isDirty())
        {
            change+=this.lookAt.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</NetworkLinkControl>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        NetworkLinkControl result = (NetworkLinkControl)super.clone();
      if(result.update!=null)
      {
        result.update = (Update)this.update.clone();
        result.update.setParent(result);
      }
      if(result.lookAt!=null)
      {
        result.lookAt = (LookAt)this.lookAt.clone();
        result.lookAt.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isMinRefreshPeriodDirty = false;
        this.isCookieDirty = false;
        this.isMessageDirty = false;
        this.isLinkNameDirty = false;
        this.isLinkDescriptionDirty = false;
        this.isLinkSnippetDirty = false;
        this.isExpiresDirty = false;
        if(this.update!=null && this.update.isDirty())
        {
            this.update.setRecursiveNotDirty();
        }
        if(this.lookAt!=null && this.lookAt.isDirty())
        {
            this.lookAt.setRecursiveNotDirty();
        }
    }
}
