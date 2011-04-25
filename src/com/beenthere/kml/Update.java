package com.beenthere.kml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Update extends Node
{
    protected String targetHref;
    private boolean isTargetHrefDirty;
    protected Create create;
    protected Delete delete;
    protected List change = new ArrayList();
    protected Replace replace;


    public Update()
    {
        super();
    }

    public Update(Node parent)
    {
        super(parent);
    }

    public String getTargetHref()
    {
        return this.targetHref;
    }

    public void setTargetHref(String value)
    {
        this.targetHref = value;
        this.isTargetHrefDirty = true;
        setDirty();
    }

    public Create getCreate()
    {
        return this.create;
    }

    public void addCreate(Create value)
    {
        if(this.create!=null)
        {
            markDeletedNode(this.create);
        }
        this.create = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Delete getDelete()
    {
        return this.delete;
    }

    public void addDelete(Delete value)
    {
        if(this.delete!=null)
        {
            markDeletedNode(this.delete);
        }
        this.delete = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public Change [] getChanges()
    {
        Change [] array = new Change[this.change.size()];
        return (Change [])this.change.toArray(array);
    }

    public void removeChange(Change value)
    {
        if(value!=null)
        {
            markDeletedNode(value);
            this.change.remove(value);
        }
    }

    public void addChange(Change value)
    {
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
            this.change.add(value);
        }
    }

    public Replace getReplace()
    {
        return this.replace;
    }

    public void addReplace(Replace value)
    {
        if(this.replace!=null)
        {
            markDeletedNode(this.replace);
        }
        this.replace = value;
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
        kml+="<Update";
        kml+=">\n";
        }
        kml+=super.toKML(true);
      if(this.targetHref!=null)
      {
            kml+="<targetHref>"+SpecialCaseFormatter.toKMLString(this.targetHref)+"</targetHref>\n";
      }
        if(this.create!=null)
        {
            kml+=this.create.toKML();
        }
        if(this.delete!=null)
        {
            kml+=this.delete.toKML();
        }
        for (Iterator iter = this.change.iterator(); iter.hasNext();)
        {
            Change cur = (Change)iter.next();
            kml+=cur.toKML();
        }
        if(this.replace!=null)
        {
            kml+=this.replace.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Update>\n";
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
        change+="<Update";
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.targetHref!=null && this.isTargetHrefDirty)
        {
            change+="<targetHref>"+SpecialCaseFormatter.toKMLString(this.targetHref)+"</targetHref>\n";
            this.isTargetHrefDirty = false;
        }
        if(this.create!=null && this.create.isDirty())
        {
            change+=this.create.toUpdateKML();
        }
        if(this.delete!=null && this.delete.isDirty())
        {
            change+=this.delete.toUpdateKML();
        }
        for (Iterator iter = this.change.iterator(); iter.hasNext();)
        {
            Change cur = (Change)iter.next();
            if(cur.isDirty())
            {
                change+=cur.toUpdateKML();
            }
        }
        if(this.replace!=null && this.replace.isDirty())
        {
            change+=this.replace.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Update>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Update result = (Update)super.clone();
      if(result.create!=null)
      {
        result.create = (Create)this.create.clone();
        result.create.setParent(result);
      }
      if(result.delete!=null)
      {
        result.delete = (Delete)this.delete.clone();
        result.delete.setParent(result);
      }
      if(result.change!=null)
      {
        result.change = new ArrayList();
        for (Iterator iter = this.change.iterator(); iter.hasNext();)
        {
            Change element = (Change)iter.next();
            Change elementClone = (Change)element.clone();
            elementClone.setParent(result);
        result.change.add(elementClone);
        }
      }
      if(result.replace!=null)
      {
        result.replace = (Replace)this.replace.clone();
        result.replace.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isTargetHrefDirty = false;
        if(this.create!=null && this.create.isDirty())
        {
            this.create.setRecursiveNotDirty();
        }
        if(this.delete!=null && this.delete.isDirty())
        {
            this.delete.setRecursiveNotDirty();
        }
        for (Iterator iter = this.change.iterator(); iter.hasNext();)
        {
            Change cur = (Change)iter.next();
            cur.setRecursiveNotDirty();
        }
        if(this.replace!=null && this.replace.isDirty())
        {
            this.replace.setRecursiveNotDirty();
        }
    }
}
