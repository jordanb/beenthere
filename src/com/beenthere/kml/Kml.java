package com.beenthere.kml;

public class Kml extends Node {
	protected NetworkLinkControl networkLinkControl;
	protected Feature feature;
	private String href;

	public Kml() {
		super();
	}

	public Kml(Node parent) {
		super(parent);
	}
	
	public Node handleTag(String tag, String text) {
		if (tag.equals("Folder")) {
			Folder folder = new Folder();
			this.addFolder(folder);
			return folder;
		}
		return super.handleTag(tag, text);
	}

	public NetworkLinkControl getNetworkLinkControl() {
		return this.networkLinkControl;
	}

	public void addNetworkLinkControl(NetworkLinkControl value) {
		if (this.networkLinkControl != null) {
			markDeletedNode(this.networkLinkControl);
		}
		this.networkLinkControl = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Feature getFeature() {
		return this.feature;
	}

	public void addFeature(Feature value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Document getDocument() {
		if (this.feature instanceof Document) {
			return (Document) this.feature;
		}
		return null;
	}

	public void addDocument(Document value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Folder getFolder() {
		if (this.feature instanceof Folder) {
			return (Folder) this.feature;
		}
		return null;
	}

	public void addFolder(Folder value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public NetworkLink getNetworkLink() {
		if (this.feature instanceof NetworkLink) {
			return (NetworkLink) this.feature;
		}
		return null;
	}

	public void addNetworkLink(NetworkLink value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public GroundOverlay getGroundOverlay() {
		if (this.feature instanceof GroundOverlay) {
			return (GroundOverlay) this.feature;
		}
		return null;
	}

	public void addGroundOverlay(GroundOverlay value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public ScreenOverlay getScreenOverlay() {
		if (this.feature instanceof ScreenOverlay) {
			return (ScreenOverlay) this.feature;
		}
		return null;
	}

	public void addScreenOverlay(ScreenOverlay value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Placemark getPlacemark() {
		if (this.feature instanceof Placemark) {
			return (Placemark) this.feature;
		}
		return null;
	}

	public void addPlacemark(Placemark value) {
		if (this.feature != null) {
			markDeletedNode(this.feature);
		}
		this.feature = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @param href
	 *            the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	public String toKML() {
		return toKML(false);
	}

	public String toKML(boolean suppressEnclosingTags) {
		String kml = "";
		kml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		kml += "<kml xmlns=\"http://earth.google.com/kml/2.1\">";

		kml += super.toKML(true);
		if (this.networkLinkControl != null) {
			kml += this.networkLinkControl.toKML();
		}
		if (this.feature != null) {
			kml += this.feature.toKML();
		}
		if (!suppressEnclosingTags) {
			kml += "</kml>\n";
		}
		return kml;
	}

	public String toUpdateKML() {
		return toUpdateKML(false);
	}

	public String toUpdateKML(boolean suppressEnclosingTags) {
		StringBuilder change = new StringBuilder();
		change.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		change.append("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
		change.append("<NetworkLinkControl>\n");
		change.append("<Update>\n");
		change.append("<targetHref>" + this.href + "</targetHref>\n");

		/**
		 * Important that these are called first, create marks new nodes as
		 * notDirty.
		 */
		String create = this.toCreateKML();
		String delete = this.toDeleteKML();

		if (this.feature != null && this.feature.isDirty()) {
			String updates = this.feature.toUpdateKML();
			if (updates.length() > 0) {
				change.append("<Change>\n");
				updates = updates.replaceAll("id=\"", "targetId=\"");
				change.append(updates);
				change.append("</Change>\n");
			}
		}
		if (create.length() > 0) {
			change.append("<Create>\n");
			change.append(create + "\n");
			change.append("</Create>\n");
		}
		if (delete.length() > 0) {
			change.append("<Delete>\n");
			change.append(delete + "\n");
			change.append("</Delete>\n");
		}
		change.append("</Update>\n");
		change.append("</NetworkLinkControl>\n");
		change.append("</kml>\n");
		setNotDirty();
		return change.toString();
	}

	public Object clone() throws CloneNotSupportedException {
		Kml result = (Kml) super.clone();
		if (result.networkLinkControl != null) {
			result.networkLinkControl = (NetworkLinkControl) this.networkLinkControl
					.clone();
			result.networkLinkControl.setParent(result);
		}
		if (result.feature != null) {
			result.feature = (Feature) this.feature.clone();
			result.feature.setParent(result);
		}
		return result;
	}

	public void setRecursiveNotDirty() {
		super.setRecursiveNotDirty();
		if (this.networkLinkControl != null
				&& this.networkLinkControl.isDirty()) {
			this.networkLinkControl.setRecursiveNotDirty();
		}
		if (this.feature != null && this.feature.isDirty()) {
			this.feature.setRecursiveNotDirty();
		}
	}
}
