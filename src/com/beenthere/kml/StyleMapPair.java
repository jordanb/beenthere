package com.beenthere.kml;

public class StyleMapPair extends Node
{
    protected String key;
    private boolean isKeyDirty;
    protected String styleUrl;
    private boolean isStyleUrlDirty;


    public StyleMapPair()
    {
        super();
    }

    public StyleMapPair(Node parent)
    {
        super(parent);
    }

    public String getKey()
    {
        return this.key;
    }

    public void setKey(String value)
    {
        this.key = value;
        this.isKeyDirty = true;
        setDirty();
    }

    public String getStyleUrl()
    {
        return this.styleUrl;
    }

    public void setStyleUrl(String value)
    {
        this.styleUrl = value;
        this.isStyleUrlDirty = true;
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
        kml+="<StyleMapPair";
        kml+=">\n";
        }
        kml+=super.toKML(true);
      if(this.key!=null)
      {
            kml+="<key>"+SpecialCaseFormatter.toKMLString(this.key)+"</key>\n";
      }
      if(this.styleUrl!=null)
      {
            kml+="<styleUrl>"+SpecialCaseFormatter.toKMLString(this.styleUrl)+"</styleUrl>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</StyleMapPair>\n";
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
        change+="<StyleMapPair";
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.key!=null && this.isKeyDirty)
        {
            change+="<key>"+SpecialCaseFormatter.toKMLString(this.key)+"</key>\n";
            this.isKeyDirty = false;
        }
        if(this.styleUrl!=null && this.isStyleUrlDirty)
        {
            change+="<styleUrl>"+SpecialCaseFormatter.toKMLString(this.styleUrl)+"</styleUrl>\n";
            this.isStyleUrlDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</StyleMapPair>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        StyleMapPair result = (StyleMapPair)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isKeyDirty = false;
        this.isStyleUrlDirty = false;
    }
}
