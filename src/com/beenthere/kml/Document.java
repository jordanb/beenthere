package com.beenthere.kml;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Document extends Container
{
    protected List feature = new ArrayList();


    public Document()
    {
        super();
    }

    public Document(Node parent)
    {
        super(parent);
    }

    public Feature [] getFeatures()
    {
        Feature [] array = new Feature[this.feature.size()];
        return (Feature [])this.feature.toArray(array);
    }

    public void removeFeature(Feature value)
    {
        if(value!=null)
        {
            markDeletedNode(value);
            this.feature.remove(value);
        }
    }

    public void addFeature(Feature value)
    {
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
            this.feature.add(value);
        }
    }

    public Document [] getDocuments()
    {
        List list = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur instanceof Document)
            {
                 list.add((Document)cur);
            }
        }
        Document [] array = new Document[list.size()];
        return (Document [])list.toArray(array);
    }

    public void addDocument(Document value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.feature.add(value);
            markCreatedNode(value);
        }
    }

    public Folder [] getFolders()
    {
        List list = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur instanceof Folder)
            {
                 list.add((Folder)cur);
            }
        }
        Folder [] array = new Folder[list.size()];
        return (Folder [])list.toArray(array);
    }

    public void addFolder(Folder value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.feature.add(value);
            markCreatedNode(value);
        }
    }

    public NetworkLink [] getNetworkLinks()
    {
        List list = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur instanceof NetworkLink)
            {
                 list.add((NetworkLink)cur);
            }
        }
        NetworkLink [] array = new NetworkLink[list.size()];
        return (NetworkLink [])list.toArray(array);
    }

    public void addNetworkLink(NetworkLink value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.feature.add(value);
            markCreatedNode(value);
        }
    }

    public GroundOverlay [] getGroundOverlays()
    {
        List list = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur instanceof GroundOverlay)
            {
                 list.add((GroundOverlay)cur);
            }
        }
        GroundOverlay [] array = new GroundOverlay[list.size()];
        return (GroundOverlay [])list.toArray(array);
    }

    public void addGroundOverlay(GroundOverlay value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.feature.add(value);
            markCreatedNode(value);
        }
    }

    public ScreenOverlay [] getScreenOverlays()
    {
        List list = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur instanceof ScreenOverlay)
            {
                 list.add((ScreenOverlay)cur);
            }
        }
        ScreenOverlay [] array = new ScreenOverlay[list.size()];
        return (ScreenOverlay [])list.toArray(array);
    }

    public void addScreenOverlay(ScreenOverlay value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.feature.add(value);
            markCreatedNode(value);
        }
    }

    public Placemark [] getPlacemarks()
    {
        List list = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur instanceof Placemark)
            {
                 list.add((Placemark)cur);
            }
        }
        Placemark [] array = new Placemark[list.size()];
        return (Placemark [])list.toArray(array);
    }

    public void addPlacemark(Placemark value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.feature.add(value);
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
        kml+="<Document";
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
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            kml+=cur.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Document>\n";
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
        change+="<Document";
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
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            if(cur.isDirty())
            {
                change+=cur.toUpdateKML();
            }
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Document>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Document result = (Document)super.clone();
      if(result.feature!=null)
      {
        result.feature = new ArrayList();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature element = (Feature)iter.next();
            Feature elementClone = (Feature)element.clone();
            elementClone.setParent(result);
        result.feature.add(elementClone);
        }
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        for (Iterator iter = this.feature.iterator(); iter.hasNext();)
        {
            Feature cur = (Feature)iter.next();
            cur.setRecursiveNotDirty();
        }
    }
}
