package com.beenthere.kml;

public class Replace extends Node
{
    protected Feature feature;


    public Replace()
    {
        super();
    }

    public Replace(Node parent)
    {
        super(parent);
    }

    public Feature getFeature()
    {
        return this.feature;
    }

    public void addFeature(Feature value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Document getDocument()
    {
        if(this.feature instanceof Document)
        {
            return (Document)this.feature;
        }
        return null;
    }

    public void addDocument(Document value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Folder getFolder()
    {
        if(this.feature instanceof Folder)
        {
            return (Folder)this.feature;
        }
        return null;
    }

    public void addFolder(Folder value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public NetworkLink getNetworkLink()
    {
        if(this.feature instanceof NetworkLink)
        {
            return (NetworkLink)this.feature;
        }
        return null;
    }

    public void addNetworkLink(NetworkLink value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public GroundOverlay getGroundOverlay()
    {
        if(this.feature instanceof GroundOverlay)
        {
            return (GroundOverlay)this.feature;
        }
        return null;
    }

    public void addGroundOverlay(GroundOverlay value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public ScreenOverlay getScreenOverlay()
    {
        if(this.feature instanceof ScreenOverlay)
        {
            return (ScreenOverlay)this.feature;
        }
        return null;
    }

    public void addScreenOverlay(ScreenOverlay value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Placemark getPlacemark()
    {
        if(this.feature instanceof Placemark)
        {
            return (Placemark)this.feature;
        }
        return null;
    }

    public void addPlacemark(Placemark value)
    {
        if(this.feature!=null)
        {
            markDeletedNode(this.feature);
        }
        this.feature = value;
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
        kml+="<Replace";
        kml+=">\n";
        }
        kml+=super.toKML(true);
        if(this.feature!=null)
        {
            kml+=this.feature.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Replace>\n";
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
        change+="<Replace";
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.feature!=null && this.feature.isDirty())
        {
            change+=this.feature.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Replace>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Replace result = (Replace)super.clone();
      if(result.feature!=null)
      {
        result.feature = (Feature)this.feature.clone();
        result.feature.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.feature!=null && this.feature.isDirty())
        {
            this.feature.setRecursiveNotDirty();
        }
    }
}
