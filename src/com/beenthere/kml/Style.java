package com.beenthere.kml;

public class Style extends StyleSelector
{
    protected IconStyle iconStyle;
    protected LabelStyle labelStyle;
    protected LineStyle lineStyle;
    protected PolyStyle polyStyle;
    protected BalloonStyle balloonStyle;
    protected ListStyle listStyle;


    public Style()
    {
        super();
    }

    public Style(Node parent)
    {
        super(parent);
    }

    public IconStyle getIconStyle()
    {
        return this.iconStyle;
    }

    public void addIconStyle(IconStyle value)
    {
        if(this.iconStyle!=null)
        {
            markDeletedNode(this.iconStyle);
        }
        this.iconStyle = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public LabelStyle getLabelStyle()
    {
        return this.labelStyle;
    }

    public void addLabelStyle(LabelStyle value)
    {
        if(this.labelStyle!=null)
        {
            markDeletedNode(this.labelStyle);
        }
        this.labelStyle = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public LineStyle getLineStyle()
    {
        return this.lineStyle;
    }

    public void addLineStyle(LineStyle value)
    {
        if(this.lineStyle!=null)
        {
            markDeletedNode(this.lineStyle);
        }
        this.lineStyle = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public PolyStyle getPolyStyle()
    {
        return this.polyStyle;
    }

    public void addPolyStyle(PolyStyle value)
    {
        if(this.polyStyle!=null)
        {
            markDeletedNode(this.polyStyle);
        }
        this.polyStyle = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public BalloonStyle getBalloonStyle()
    {
        return this.balloonStyle;
    }

    public void addBalloonStyle(BalloonStyle value)
    {
        if(this.balloonStyle!=null)
        {
            markDeletedNode(this.balloonStyle);
        }
        this.balloonStyle = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }

    public ListStyle getListStyle()
    {
        return this.listStyle;
    }

    public void addListStyle(ListStyle value)
    {
        if(this.listStyle!=null)
        {
            markDeletedNode(this.listStyle);
        }
        this.listStyle = value;
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
        kml+="<Style";
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
        if(this.iconStyle!=null)
        {
            kml+=this.iconStyle.toKML();
        }
        if(this.labelStyle!=null)
        {
            kml+=this.labelStyle.toKML();
        }
        if(this.lineStyle!=null)
        {
            kml+=this.lineStyle.toKML();
        }
        if(this.polyStyle!=null)
        {
            kml+=this.polyStyle.toKML();
        }
        if(this.balloonStyle!=null)
        {
            kml+=this.balloonStyle.toKML();
        }
        if(this.listStyle!=null)
        {
            kml+=this.listStyle.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</Style>\n";
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
        change+="<Style";
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
        if(this.iconStyle!=null && this.iconStyle.isDirty())
        {
            change+=this.iconStyle.toUpdateKML();
        }
        if(this.labelStyle!=null && this.labelStyle.isDirty())
        {
            change+=this.labelStyle.toUpdateKML();
        }
        if(this.lineStyle!=null && this.lineStyle.isDirty())
        {
            change+=this.lineStyle.toUpdateKML();
        }
        if(this.polyStyle!=null && this.polyStyle.isDirty())
        {
            change+=this.polyStyle.toUpdateKML();
        }
        if(this.balloonStyle!=null && this.balloonStyle.isDirty())
        {
            change+=this.balloonStyle.toUpdateKML();
        }
        if(this.listStyle!=null && this.listStyle.isDirty())
        {
            change+=this.listStyle.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</Style>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        Style result = (Style)super.clone();
      if(result.iconStyle!=null)
      {
        result.iconStyle = (IconStyle)this.iconStyle.clone();
        result.iconStyle.setParent(result);
      }
      if(result.labelStyle!=null)
      {
        result.labelStyle = (LabelStyle)this.labelStyle.clone();
        result.labelStyle.setParent(result);
      }
      if(result.lineStyle!=null)
      {
        result.lineStyle = (LineStyle)this.lineStyle.clone();
        result.lineStyle.setParent(result);
      }
      if(result.polyStyle!=null)
      {
        result.polyStyle = (PolyStyle)this.polyStyle.clone();
        result.polyStyle.setParent(result);
      }
      if(result.balloonStyle!=null)
      {
        result.balloonStyle = (BalloonStyle)this.balloonStyle.clone();
        result.balloonStyle.setParent(result);
      }
      if(result.listStyle!=null)
      {
        result.listStyle = (ListStyle)this.listStyle.clone();
        result.listStyle.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.iconStyle!=null && this.iconStyle.isDirty())
        {
            this.iconStyle.setRecursiveNotDirty();
        }
        if(this.labelStyle!=null && this.labelStyle.isDirty())
        {
            this.labelStyle.setRecursiveNotDirty();
        }
        if(this.lineStyle!=null && this.lineStyle.isDirty())
        {
            this.lineStyle.setRecursiveNotDirty();
        }
        if(this.polyStyle!=null && this.polyStyle.isDirty())
        {
            this.polyStyle.setRecursiveNotDirty();
        }
        if(this.balloonStyle!=null && this.balloonStyle.isDirty())
        {
            this.balloonStyle.setRecursiveNotDirty();
        }
        if(this.listStyle!=null && this.listStyle.isDirty())
        {
            this.listStyle.setRecursiveNotDirty();
        }
    }
}
