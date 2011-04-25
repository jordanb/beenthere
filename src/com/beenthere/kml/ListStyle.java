package com.beenthere.kml;

public class ListStyle extends ObjectNode
{
    protected String listItemType;
    private boolean isListItemTypeDirty;
//    protected java.awt.Color bgColor;
    private boolean isBgColorDirty;
    protected ItemIcon itemIcon;


    public ListStyle()
    {
        super();
    }

    public ListStyle(Node parent)
    {
        super(parent);
    }

    public String getListItemType()
    {
        return this.listItemType;
    }

    public void setListItemType(String value)
    {
        this.listItemType = value;
        this.isListItemTypeDirty = true;
        setDirty();
    }

//    public java.awt.Color getBgColor()
//    {
//        return this.bgColor;
//    }

    public void setBgColor(String hexValue)
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
//        this.bgColor = newCol;
        this.isBgColorDirty = true;
        setDirty();
    }

//    public void setBgColor(java.awt.Color aColor)
//    {
//        this.bgColor = aColor;
//        this.isBgColorDirty = true;
//        setDirty();
//    }

    public ItemIcon getItemIcon()
    {
        return this.itemIcon;
    }

    public void addItemIcon(ItemIcon value)
    {
        if(this.itemIcon!=null)
        {
            markDeletedNode(this.itemIcon);
        }
        this.itemIcon = value;
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
        kml+="<ListStyle";
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
      if(this.listItemType!=null)
      {
            kml+="<listItemType>"+SpecialCaseFormatter.toKMLString(this.listItemType)+"</listItemType>\n";
      }
//      if(this.bgColor!=null)
//      {
//            kml+="<bgColor>"+SpecialCaseFormatter.toKMLString(this.bgColor)+"</bgColor>\n";
//      }
        if(this.itemIcon!=null)
        {
            kml+=this.itemIcon.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</ListStyle>\n";
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
        change+="<ListStyle";
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
        if(this.listItemType!=null && this.isListItemTypeDirty)
        {
            change+="<listItemType>"+SpecialCaseFormatter.toKMLString(this.listItemType)+"</listItemType>\n";
            this.isListItemTypeDirty = false;
        }
//        if(this.bgColor!=null && this.isBgColorDirty)
//        {
//            change+="<bgColor>"+SpecialCaseFormatter.toKMLString(this.bgColor)+"</bgColor>\n";
//            this.isBgColorDirty = false;
//        }
        if(this.itemIcon!=null && this.itemIcon.isDirty())
        {
            change+=this.itemIcon.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</ListStyle>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        ListStyle result = (ListStyle)super.clone();
      if(result.itemIcon!=null)
      {
        result.itemIcon = (ItemIcon)this.itemIcon.clone();
        result.itemIcon.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isListItemTypeDirty = false;
        this.isBgColorDirty = false;
        if(this.itemIcon!=null && this.itemIcon.isDirty())
        {
            this.itemIcon.setRecursiveNotDirty();
        }
    }
}
