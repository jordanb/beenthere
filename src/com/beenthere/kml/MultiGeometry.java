package com.beenthere.kml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiGeometry extends Geometry
{
    protected List geometry = new ArrayList();


    public MultiGeometry()
    {
        super();
    }

    public MultiGeometry(Node parent)
    {
        super(parent);
    }

    public Geometry [] getGeometrys()
    {
        Geometry [] array = new Geometry[this.geometry.size()];
        return (Geometry [])this.geometry.toArray(array);
    }

    public void removeGeometry(Geometry value)
    {
        if(value!=null)
        {
            markDeletedNode(value);
            this.geometry.remove(value);
        }
    }

    public void addGeometry(Geometry value)
    {
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
            this.geometry.add(value);
        }
    }

    public Model [] getModels()
    {
        List list = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur instanceof Model)
            {
                 list.add((Model)cur);
            }
        }
        Model [] array = new Model[list.size()];
        return (Model [])list.toArray(array);
    }

    public void addModel(Model value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.geometry.add(value);
            markCreatedNode(value);
        }
    }

    public LinearRing [] getLinearRings()
    {
        List list = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur instanceof LinearRing)
            {
                 list.add((LinearRing)cur);
            }
        }
        LinearRing [] array = new LinearRing[list.size()];
        return (LinearRing [])list.toArray(array);
    }

    public void addLinearRing(LinearRing value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.geometry.add(value);
            markCreatedNode(value);
        }
    }

    public Point [] getPoints()
    {
        List list = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur instanceof Point)
            {
                 list.add((Point)cur);
            }
        }
        Point [] array = new Point[list.size()];
        return (Point [])list.toArray(array);
    }

    public void addPoint(Point value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.geometry.add(value);
            markCreatedNode(value);
        }
    }

    public MultiGeometry [] getMultiGeometrys()
    {
        List list = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur instanceof MultiGeometry)
            {
                 list.add((MultiGeometry)cur);
            }
        }
        MultiGeometry [] array = new MultiGeometry[list.size()];
        return (MultiGeometry [])list.toArray(array);
    }

    public void addMultiGeometry(MultiGeometry value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.geometry.add(value);
            markCreatedNode(value);
        }
    }

    public Polygon [] getPolygons()
    {
        List list = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur instanceof Polygon)
            {
                 list.add((Polygon)cur);
            }
        }
        Polygon [] array = new Polygon[list.size()];
        return (Polygon [])list.toArray(array);
    }

    public void addPolygon(Polygon value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.geometry.add(value);
            markCreatedNode(value);
        }
    }

    public LineString [] getLineStrings()
    {
        List list = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur instanceof LineString)
            {
                 list.add((LineString)cur);
            }
        }
        LineString [] array = new LineString[list.size()];
        return (LineString [])list.toArray(array);
    }

    public void addLineString(LineString value)
    {
        if(value!=null)
        {
            value.setParent(this);
            this.geometry.add(value);
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
        kml+="<MultiGeometry";
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
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            kml+=cur.toKML();
        }
        if(!suppressEnclosingTags)
        {
            kml+="</MultiGeometry>\n";
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
        change+="<MultiGeometry";
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
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            if(cur.isDirty())
            {
                change+=cur.toUpdateKML();
            }
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</MultiGeometry>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        MultiGeometry result = (MultiGeometry)super.clone();
      if(result.geometry!=null)
      {
        result.geometry = new ArrayList();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry element = (Geometry)iter.next();
            Geometry elementClone = (Geometry)element.clone();
            elementClone.setParent(result);
        result.geometry.add(elementClone);
        }
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        for (Iterator iter = this.geometry.iterator(); iter.hasNext();)
        {
            Geometry cur = (Geometry)iter.next();
            cur.setRecursiveNotDirty();
        }
    }
}
