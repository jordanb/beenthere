package com.beenthere.kml;

public class NetworkLink extends Feature
{
    protected boolean refreshVisibility;
    private boolean isRefreshVisibilityDirty;
    public static boolean DEFAULT_FLYTOVIEW=false;
    protected boolean flyToView = DEFAULT_FLYTOVIEW;
    private boolean isFlyToViewDirty;
    protected Link link;
    protected Link url;


    public NetworkLink()
    {
        super();
    }

    public NetworkLink(Node parent)
    {
        super(parent);
    }

    public boolean getRefreshVisibility()
    {
        return this.refreshVisibility;
    }

    public void setRefreshVisibility(boolean value)
    {
        this.refreshVisibility = value;
        this.isRefreshVisibilityDirty = true;
        setDirty();
    }

    public boolean getFlyToView()
    {
        return this.flyToView;
    }

    public void setFlyToView(boolean value)
    {
        this.flyToView = value;
        this.isFlyToViewDirty = true;
        setDirty();
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

    public Link getUrl()
    {
        return this.url;
    }

    public void addUrl(Link value)
    {
        if(this.url!=null)
        {
            markDeletedNode(this.url);
        }
        this.url = value;
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
        kml+="<NetworkLink";
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
        kml+="<refreshVisibility>"+this.refreshVisibility+"</refreshVisibility>\n";
        if(this.flyToView!=DEFAULT_FLYTOVIEW)
        {
            kml+="<flyToView>"+this.flyToView+"</flyToView>\n";
        }
        if(this.link!=null)
        {
            kml+=this.link.toKML();
        }
        if(this.url!=null)
        {
            kml+=this.url.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</NetworkLink>\n";
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
        change+="<NetworkLink";
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
        if(this.isRefreshVisibilityDirty)
        {
            change+="<refreshVisibility>"+this.refreshVisibility+"</refreshVisibility>\n";
            this.isRefreshVisibilityDirty = false;
        }
        if(this.isFlyToViewDirty)
        {
            change+="<flyToView>"+this.flyToView+"</flyToView>\n";
            this.isFlyToViewDirty = false;
        }
        if(this.link!=null && this.link.isDirty())
        {
            change+=this.link.toUpdateKML();
        }
        if(this.url!=null && this.url.isDirty())
        {
            change+=this.url.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</NetworkLink>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        NetworkLink result = (NetworkLink)super.clone();
      if(result.link!=null)
      {
        result.link = (Link)this.link.clone();
        result.link.setParent(result);
      }
      if(result.url!=null)
      {
        result.url = (Link)this.url.clone();
        result.url.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isRefreshVisibilityDirty = false;
        this.isFlyToViewDirty = false;
        if(this.link!=null && this.link.isDirty())
        {
            this.link.setRecursiveNotDirty();
        }
        if(this.url!=null && this.url.isDirty())
        {
            this.url.setRecursiveNotDirty();
        }
    }
}
