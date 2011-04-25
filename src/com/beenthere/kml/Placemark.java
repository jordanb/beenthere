package com.beenthere.kml;

public class Placemark extends Feature {
	
	protected Geometry geometry;

	public Placemark() {
		super();
	}

	public Placemark(Node parent) {
		super(parent);
	}
	
	public Node handleTag(String tag, String text) {
		if (tag.equals("Point")) {
			Point point = new Point();
			this.addPoint(point);
			return point;
		}
		return super.handleTag(tag, text);
	}

	public Geometry getGeometry() {
		return this.geometry;
	}

	public void addGeometry(Geometry value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Model getModel() {
		if (this.geometry instanceof Model) {
			return (Model) this.geometry;
		}
		return null;
	}

	public void addModel(Model value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public LinearRing getLinearRing() {
		if (this.geometry instanceof LinearRing) {
			return (LinearRing) this.geometry;
		}
		return null;
	}

	public void addLinearRing(LinearRing value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Point getPoint() {
		if (this.geometry instanceof Point) {
			return (Point) this.geometry;
		}
		return null;
	}

	public void addPoint(Point value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public MultiGeometry getMultiGeometry() {
		if (this.geometry instanceof MultiGeometry) {
			return (MultiGeometry) this.geometry;
		}
		return null;
	}

	public void addMultiGeometry(MultiGeometry value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Polygon getPolygon() {
		if (this.geometry instanceof Polygon) {
			return (Polygon) this.geometry;
		}
		return null;
	}

	public void addPolygon(Polygon value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public LineString getLineString() {
		if (this.geometry instanceof LineString) {
			return (LineString) this.geometry;
		}
		return null;
	}

	public void addLineString(LineString value) {
		if (this.geometry != null) {
			markDeletedNode(this.geometry);
		}
		this.geometry = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public String toKML() {
		return toKML(false);
	}

	public String toKML(boolean suppressEnclosingTags) {
		String kml = "";
		if (!suppressEnclosingTags) {
			kml += "<Placemark";
			if (this.id != null) {
				kml += " id=\"" + this.getId() + "\"";
			}
			if (this.targetId != null) {
				kml += " targetId=\"" + this.getTargetId() + "\"";
			}
			kml += ">\n";
		}
		kml += super.toKML(true);
		if (this.geometry != null) {
			kml += this.geometry.toKML();
		}
		if (!suppressEnclosingTags) {
			kml += "</Placemark>\n";
		}
		return kml;
	}

	public String toUpdateKML() {
		return toUpdateKML(false);
	}

	public String toUpdateKML(boolean suppressEnclosingTags) {
		if (!isDirty()) {
			return "";
		}
		String change = "";
		boolean isPrimDirty = isPrimitiveDirty(); // need to track it after
													// object is setNotDirty
		if (isPrimDirty && !suppressEnclosingTags) {
			change += "<Placemark";
			if (this.id != null) {
				change += " id=\"" + this.getId() + "\"";
			}
			if (this.targetId != null) {
				change += " targetId=\"" + this.getTargetId() + "\"";
			}
			change += ">\n";
		}
		change += super.toUpdateKML(true);
		if (this.geometry != null && this.geometry.isDirty()) {
			change += this.geometry.toUpdateKML();
		}
		if (isPrimDirty && !suppressEnclosingTags) {
			change += "</Placemark>\n";
		}
		setNotDirty();
		return change;
	}

	public Object clone() throws CloneNotSupportedException {
		Placemark result = (Placemark) super.clone();
		if (result.geometry != null) {
			result.geometry = (Geometry) this.geometry.clone();
			result.geometry.setParent(result);
		}
		return result;
	}

	public void setRecursiveNotDirty() {
		super.setRecursiveNotDirty();
		if (this.geometry != null && this.geometry.isDirty()) {
			this.geometry.setRecursiveNotDirty();
		}
	}
}
