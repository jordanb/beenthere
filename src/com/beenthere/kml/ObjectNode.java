package com.beenthere.kml;

abstract public class ObjectNode extends Node {
	protected String id;
	private boolean isIdDirty;
	protected String targetId;
	private boolean isTargetIdDirty;

	public ObjectNode() {
		this(null);
	}

	public ObjectNode(Node parent) {
		super(parent);
		generateID();
	}

	private void generateID() {
		if (Configuration.ON.equals(Configuration.properties
				.getProperty(Configuration.GENERATE_IDS))) {
			this.id = "" + this.hashCode();
		}
	}

	public String getId() {
		return this.id;
	}

	public void setId(String value) {
		this.id = value;
		this.isIdDirty = true;
		setDirty();
	}

	public String getTargetId() {
		return this.targetId;
	}

	public void setTargetId(String value) {
		this.targetId = value;
		this.isTargetIdDirty = true;
		setDirty();
	}

	public Node handleTag(String tag, String text) {
		return super.handleTag(tag, text);
	}

	public String toKML() {
		return toKML(false);
	}

	public String toKML(boolean suppressEnclosingTags) {
		String kml = "";
		kml += super.toKML(true);
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
		change += super.toUpdateKML(true);
		if (this.id != null && this.isIdDirty) {
			change += "<id>" + SpecialCaseFormatter.toKMLString(this.id)
					+ "</id>\n";
			this.isIdDirty = false;
		}
		if (this.targetId != null && this.isTargetIdDirty) {
			change += "<targetId>"
					+ SpecialCaseFormatter.toKMLString(this.targetId)
					+ "</targetId>\n";
			this.isTargetIdDirty = false;
		}
		setNotDirty();
		return change;
	}

	public Object clone() throws CloneNotSupportedException {
		ObjectNode result = (ObjectNode) super.clone();
		return result;
	}

	public void setRecursiveNotDirty() {
		super.setRecursiveNotDirty();
		this.isIdDirty = false;
		this.isTargetIdDirty = false;
	}
}
