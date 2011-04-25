package com.beenthere.kml;

abstract public class Overlay extends Feature
{
    //protected java.awt.Color color;
    private boolean isColorDirty;
    protected int drawOrder;
    private boolean isDrawOrderDirty;
    protected Icon icon;


    public Overlay()
    {
        super();
    }

    public Overlay(Node parent)
    {
        super(parent);
    }

//    public java.awt.Color getColor()
//    {
//        return this.color;
//    }

    public void setColor(String hexValue)
    {
        if(hexValue.length()!=8)
        {
             return;
        }
        int alpha = Integer.valueOf(hexValue.substring(0, 2), 16).intValue();
        int r = Integer.valueOf(hexValue.substring(2, 4), 16).intValue();
        int g = Integer.valueOf(hexValue.substring(4, 6), 16).intValue();
        int b = Integer.valueOf(hexValue.substring(6, 8), 16).intValue();
//        java.awt.Color newCol = new java.awt.Color(r, g, b, alpha);
//        this.color = newCol;
        this.isColorDirty = true;
        setDirty();
    }

//    public void setColor(java.awt.Color aColor)
//    {
//        this.color = aColor;
//        this.isColorDirty = true;
//        setDirty();
//    }

    public int getDrawOrder()
    {
        return this.drawOrder;
    }

    public void setDrawOrder(int value)
    {
        this.drawOrder = value;
        this.isDrawOrderDirty = true;
        setDirty();
    }

    public Icon getIcon()
    {
        return this.icon;
    }

    public void addIcon(Icon value)
    {
        if(this.icon!=null)
        {
            markDeletedNode(this.icon);
        }
        this.icon = value;
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
        kml+=super.toKML(true);
//      if(this.color!=null)
//      {
//            kml+="<color>"+SpecialCaseFormatter.toKMLString(this.color)+"</color>\n";
//      }
        kml+="<drawOrder>"+this.drawOrder+"</drawOrder>\n";
        if(this.icon!=null)
        {
            kml+=this.icon.toKML();
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
        change+=super.toUpdateKML(true);
//        if(this.color!=null && this.isColorDirty)
//        {
//            change+="<color>"+SpecialCaseFormatter.toKMLString(this.color)+"</color>\n";
//            this.isColorDirty = false;
//        }
        if(this.isDrawOrderDirty)
        {
            change+="<drawOrder>"+this.drawOrder+"</drawOrder>\n";
            this.isDrawOrderDirty = false;
        }
        if(this.icon!=null && this.icon.isDirty())
        {
            change+=this.icon.toUpdateKML();
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Overlay result = (Overlay)super.clone();
      if(result.icon!=null)
      {
        result.icon = (Icon)this.icon.clone();
        result.icon.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isColorDirty = false;
        this.isDrawOrderDirty = false;
        if(this.icon!=null && this.icon.isDirty())
        {
            this.icon.setRecursiveNotDirty();
        }
    }
}
