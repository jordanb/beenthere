package com.beenthere.kml;

public class TimeStamp extends TimePrimitive {
	protected LongDate when;
	private boolean isWhenDirty;

	public TimeStamp() {
		super();
	}

	public TimeStamp(Node parent) {
		super(parent);
	}
	
	public Node handleTag(String tag, String text) {
		if (tag.equals("when") && text != null) {
			this.setWhen(text);
			return null;
		}
		return super.handleTag(tag, text);
	}

	public LongDate getWhen() {
		return this.when;
	}

	public void setWhen(String date) {
		setWhen(new LongDate(date));
	}

	public void setWhen(LongDate aDate) {
		this.when = aDate;
		this.isWhenDirty = true;
		setDirty();
	}

	public String toKML() {
		return toKML(false);
	}

	public String toKML(boolean suppressEnclosingTags) {
		String kml = "";
		if (!suppressEnclosingTags) {
			kml += "<TimeStamp";
			if (this.id != null) {
				kml += " id=\"" + this.getId() + "\"";
			}
			if (this.targetId != null) {
				kml += " targetId=\"" + this.getTargetId() + "\"";
			}
			kml += ">\n";
		}
		kml += super.toKML(true);
		if (this.when != null) {
			kml += "<when>" + SpecialCaseFormatter.toKMLString(this.when)
					+ "</when>\n";
		}
		if (!suppressEnclosingTags) {
			kml += "</TimeStamp>\n";
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
			change += "<TimeStamp";
			if (this.id != null) {
				change += " id=\"" + this.getId() + "\"";
			}
			if (this.targetId != null) {
				change += " targetId=\"" + this.getTargetId() + "\"";
			}
			change += ">\n";
		}
		change += super.toUpdateKML(true);
		if (this.when != null && this.isWhenDirty) {
			change += "<when>" + SpecialCaseFormatter.toKMLString(this.when)
					+ "</when>\n";
			this.isWhenDirty = false;
		}
		if (isPrimDirty && !suppressEnclosingTags) {
			change += "</TimeStamp>\n";
		}
		setNotDirty();
		return change;
	}

	public Object clone() throws CloneNotSupportedException {
		TimeStamp result = (TimeStamp) super.clone();
		return result;
	}

	public void setRecursiveNotDirty() {
		super.setRecursiveNotDirty();
		this.isWhenDirty = false;
	}
}
