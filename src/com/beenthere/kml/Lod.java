package com.beenthere.kml;

public class Lod extends ObjectNode
{
    public static float DEFAULT_MINLODPIXELS=0;
    protected float minLodPixels = DEFAULT_MINLODPIXELS;
    private boolean isMinLodPixelsDirty;
    protected float maxLodPixels;
    private boolean isMaxLodPixelsDirty;
    public static float DEFAULT_MINFADEEXTENT=0;
    protected float minFadeExtent = DEFAULT_MINFADEEXTENT;
    private boolean isMinFadeExtentDirty;
    public static float DEFAULT_MAXFADEEXTENT=0;
    protected float maxFadeExtent = DEFAULT_MAXFADEEXTENT;
    private boolean isMaxFadeExtentDirty;


    public Lod()
    {
        super();
    }

    public Lod(Node parent)
    {
        super(parent);
    }

    public float getMinLodPixels()
    {
        return this.minLodPixels;
    }

    public void setMinLodPixels(float value)
    {
        this.minLodPixels = value;
        this.isMinLodPixelsDirty = true;
        setDirty();
    }

    public float getMaxLodPixels()
    {
        return this.maxLodPixels;
    }

    public void setMaxLodPixels(float value)
    {
        this.maxLodPixels = value;
        this.isMaxLodPixelsDirty = true;
        setDirty();
    }

    public float getMinFadeExtent()
    {
        return this.minFadeExtent;
    }

    public void setMinFadeExtent(float value)
    {
        this.minFadeExtent = value;
        this.isMinFadeExtentDirty = true;
        setDirty();
    }

    public float getMaxFadeExtent()
    {
        return this.maxFadeExtent;
    }

    public void setMaxFadeExtent(float value)
    {
        this.maxFadeExtent = value;
        this.isMaxFadeExtentDirty = true;
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
        kml+="<Lod";
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
        if(this.minLodPixels!=DEFAULT_MINLODPIXELS)
        {
            kml+="<minLodPixels>"+this.minLodPixels+"</minLodPixels>\n";
        }
        kml+="<maxLodPixels>"+this.maxLodPixels+"</maxLodPixels>\n";
        if(this.minFadeExtent!=DEFAULT_MINFADEEXTENT)
        {
            kml+="<minFadeExtent>"+this.minFadeExtent+"</minFadeExtent>\n";
        }
        if(this.maxFadeExtent!=DEFAULT_MAXFADEEXTENT)
        {
            kml+="<maxFadeExtent>"+this.maxFadeExtent+"</maxFadeExtent>\n";
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Lod>\n";
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
        change+="<Lod";
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
        if(this.isMinLodPixelsDirty)
        {
            change+="<minLodPixels>"+this.minLodPixels+"</minLodPixels>\n";
            this.isMinLodPixelsDirty = false;
        }
        if(this.isMaxLodPixelsDirty)
        {
            change+="<maxLodPixels>"+this.maxLodPixels+"</maxLodPixels>\n";
            this.isMaxLodPixelsDirty = false;
        }
        if(this.isMinFadeExtentDirty)
        {
            change+="<minFadeExtent>"+this.minFadeExtent+"</minFadeExtent>\n";
            this.isMinFadeExtentDirty = false;
        }
        if(this.isMaxFadeExtentDirty)
        {
            change+="<maxFadeExtent>"+this.maxFadeExtent+"</maxFadeExtent>\n";
            this.isMaxFadeExtentDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Lod>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Lod result = (Lod)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isMinLodPixelsDirty = false;
        this.isMaxLodPixelsDirty = false;
        this.isMinFadeExtentDirty = false;
        this.isMaxFadeExtentDirty = false;
    }
}
