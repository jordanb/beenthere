package com.beenthere.kml;


public class Create extends Node
{
    protected Folder folder;


    public Create()
    {
        super();
    }

    public Create(Node parent)
    {
        super(parent);
    }

    public Folder getFolder()
    {
        return this.folder;
    }

    public void addFolder(Folder value)
    {
        if(this.folder!=null)
        {
            markDeletedNode(this.folder);
        }
        this.folder = value;
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
        kml+="<Create";
        kml+=">\n";
        }
        kml+=super.toKML(true);
        if(this.folder!=null)
        {
            kml+=this.folder.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Create>\n";
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
        change+="<Create";
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.folder!=null && this.folder.isDirty())
        {
            change+=this.folder.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Create>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Create result = (Create)super.clone();
      if(result.folder!=null)
      {
        result.folder = (Folder)this.folder.clone();
        result.folder.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.folder!=null && this.folder.isDirty())
        {
            this.folder.setRecursiveNotDirty();
        }
    }
}
