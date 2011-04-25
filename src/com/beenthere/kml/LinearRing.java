package com.beenthere.kml;

public class LinearRing extends Geometry
{
    public static boolean DEFAULT_EXTRUDE=false;
    protected boolean extrude = DEFAULT_EXTRUDE;
    private boolean isExtrudeDirty;
    public static boolean DEFAULT_TESSELLATE=false;
    protected boolean tessellate = DEFAULT_TESSELLATE;
    private boolean isTessellateDirty;
    protected String altitudeMode;
    private boolean isAltitudeModeDirty;
//    protected String coordinates;
    private boolean isCoordinatesDirty;
    protected double [] coordinates;


    public LinearRing()
    {
        super();
    }

    public LinearRing(Node parent)
    {
        super(parent);
    }

    public boolean getExtrude()
    {
        return this.extrude;
    }

    public void setExtrude(boolean value)
    {
        this.extrude = value;
        this.isExtrudeDirty = true;
        setDirty();
    }

    public boolean getTessellate()
    {
        return this.tessellate;
    }

    public void setTessellate(boolean value)
    {
        this.tessellate = value;
        this.isTessellateDirty = true;
        setDirty();
    }

    public String getAltitudeMode()
    {
        return this.altitudeMode;
    }

    public void setAltitudeMode(String value)
    {
        this.altitudeMode = value;
        this.isAltitudeModeDirty = true;
        setDirty();
    }

	public String getCoordinates()
	    {
		String coordStr = "";
		for (int i = 0; i < this.coordinates.length; i++)
		{	    
		    coordStr+= this.coordinates[i];
		    if(i<(this.coordinates.length-1))
		    {
			if(i%3==2) 
			{
			    coordStr+=" ";
			}
			else
			{
			    coordStr+=",";
			}
		    }
		}

	        return coordStr;
	    }

	    public void setCoordinates(String value)
	    {	
		String [] coords = nonEmpty(value.split("[, ]"));
	        double [] newCoords = new double[coords.length];
	        for (int i = 0; i < coords.length; i++)
		{
	            try
	            {
	        	newCoords[i] = Double.parseDouble(coords[i]);
	            }
	            catch(NumberFormatException nfe)
	            {
	        	System.err.println("Error parsing double ("+coords[i]+") in Point.updateNumericalCoordinates "+nfe);        	
	            }
		}
	        setNumericalCoordinates(newCoords);
	    }

	    private String[] nonEmpty(String[] strings)
	    {
		int numNonEmpty = 0;
		for (int i = 0; i < strings.length; i++)
		{
		    if(strings[i].trim().length()!=0)
		    {
			numNonEmpty++;
		    }
		}
		String [] nonEmpties = new String[numNonEmpty];
		int pos = 0;
		for (int i = 0; i < strings.length; i++)
		{
		    if(strings[i].trim().length()!=0)
		    {
	    	    	nonEmpties[pos] = strings[i];
	    	    	pos++;
		    }
		}
		return nonEmpties;
	    }

	    /**
	     * @return the numericalCoordinates
	     */
	    public double [] getNumericalCoordinates()
	    {
	        return this.coordinates;
	    }

	    /**
	     * @param numericalCoordinates the numericalCoordinates to set
	     */
	    public void setNumericalCoordinates(double [] numericalCoordinates)
	    {
	        this.coordinates = numericalCoordinates;
		this.isCoordinatesDirty = true;
		setDirty();
	    }

	    public void addNumericalCoordinate(double numericalCoordinate)
	    {
		double [] newCoords = new double[this.coordinates.length+1];
		System.arraycopy(this.coordinates, 0, newCoords, 0, this.coordinates.length);
		newCoords[this.coordinates.length] = numericalCoordinate;
		this.isCoordinatesDirty = true;
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
        kml+="<LinearRing";
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
        if(this.extrude!=DEFAULT_EXTRUDE)
        {
            kml+="<extrude>"+this.extrude+"</extrude>\n";
        }
        if(this.tessellate!=DEFAULT_TESSELLATE)
        {
            kml+="<tessellate>"+this.tessellate+"</tessellate>\n";
        }
      if(this.altitudeMode!=null)
      {
            kml+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
      }
      if(this.coordinates!=null)
      {
            kml+="<coordinates>"+SpecialCaseFormatter.toKMLString(this.getCoordinates())+"</coordinates>\n";
      }
        if(!suppressEnclosingTags)
        {
            kml+="</LinearRing>\n";
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
        change+="<LinearRing";
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
        if(this.isExtrudeDirty)
        {
            change+="<extrude>"+this.extrude+"</extrude>\n";
            this.isExtrudeDirty = false;
        }
        if(this.isTessellateDirty)
        {
            change+="<tessellate>"+this.tessellate+"</tessellate>\n";
            this.isTessellateDirty = false;
        }
        if(this.altitudeMode!=null && this.isAltitudeModeDirty)
        {
            change+="<altitudeMode>"+SpecialCaseFormatter.toKMLString(this.altitudeMode)+"</altitudeMode>\n";
            this.isAltitudeModeDirty = false;
        }
        if(this.coordinates!=null && this.isCoordinatesDirty)
        {
            change+="<coordinates>"+SpecialCaseFormatter.toKMLString(this.getCoordinates())+"</coordinates>\n";
            this.isCoordinatesDirty = false;
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</LinearRing>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        LinearRing result = (LinearRing)super.clone();
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        this.isExtrudeDirty = false;
        this.isTessellateDirty = false;
        this.isAltitudeModeDirty = false;
        this.isCoordinatesDirty = false;
    }
}
