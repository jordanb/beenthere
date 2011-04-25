package com.beenthere.kml;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

abstract public class Feature extends ObjectNode {
	
	protected String name;
	private boolean isNameDirty;
	public static boolean DEFAULT_VISIBILITY = true;
	protected boolean visibility = DEFAULT_VISIBILITY;
	private boolean isVisibilityDirty;
	public static boolean DEFAULT_OPEN = true;
	protected boolean open = DEFAULT_OPEN;
	private boolean isOpenDirty;
	protected String address;
	private boolean isAddressDirty;
	protected String phoneNumber;
	private boolean isPhoneNumberDirty;
	protected String snippet;
	private boolean isSnippetDirty;
	protected String description;
	private boolean isDescriptionDirty;
	protected LookAt lookAt;
	protected TimePrimitive timePrimitive;
	protected String styleUrl;
	private boolean isStyleUrlDirty;
	protected List styleSelector = new ArrayList();
	protected Region region;
	protected Metadata metadata;

	public Feature() {
		super();
	}

	public Feature(Node parent) {
		super(parent);
	}

	public Node handleTag(String tag, String text) {
		if (tag.equals("name") && text != null) {
			this.setName(text);
			return null;
		}
		if (tag.equals("open") && text != null) {
			this.setOpen(text == "1");
			return null;
		}
		if (tag.equals("description") && text != null) {
			this.setDescription(text);
			return null;
		}
		if (tag.equals("TimeStamp")) {
			TimeStamp timestamp = new TimeStamp();
			this.addTimeStamp(timestamp);
			return timestamp;
		}
		return super.handleTag(tag, text);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String value) {
		this.name = value;
		this.isNameDirty = true;
		setDirty();
	}

	public boolean getVisibility() {
		return this.visibility;
	}

	public void setVisibility(boolean value) {
		this.visibility = value;
		this.isVisibilityDirty = true;
		setDirty();
	}

	public boolean getOpen() {
		return this.open;
	}

	public void setOpen(boolean value) {
		this.open = value;
		this.isOpenDirty = true;
		setDirty();
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String value) {
		this.address = value;
		this.isAddressDirty = true;
		setDirty();
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String value) {
		this.phoneNumber = value;
		this.isPhoneNumberDirty = true;
		setDirty();
	}

	public String getSnippet() {
		return this.snippet;
	}

	public void setSnippet(String value) {
		this.snippet = value;
		this.isSnippetDirty = true;
		setDirty();
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String value) {
		this.description = value;
		this.isDescriptionDirty = true;
		setDirty();
	}

	public LookAt getLookAt() {
		return this.lookAt;
	}

	public void addLookAt(LookAt value) {
		if (this.lookAt != null) {
			markDeletedNode(this.lookAt);
		}
		this.lookAt = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public TimePrimitive getTimePrimitive() {
		return this.timePrimitive;
	}

	public void addTimePrimitive(TimePrimitive value) {
		if (this.timePrimitive != null) {
			markDeletedNode(this.timePrimitive);
		}
		this.timePrimitive = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public TimeStamp getTimeStamp() {
		if (this.timePrimitive instanceof TimeStamp) {
			return (TimeStamp) this.timePrimitive;
		}
		return null;
	}

	public void addTimeStamp(TimeStamp value) {
		if (this.timePrimitive != null) {
			markDeletedNode(this.timePrimitive);
		}
		this.timePrimitive = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public TimeSpan getTimeSpan() {
		if (this.timePrimitive instanceof TimeSpan) {
			return (TimeSpan) this.timePrimitive;
		}
		return null;
	}

	public void addTimeSpan(TimeSpan value) {
		if (this.timePrimitive != null) {
			markDeletedNode(this.timePrimitive);
		}
		this.timePrimitive = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public String getStyleUrl() {
		return this.styleUrl;
	}

	public void setStyleUrl(String value) {
		this.styleUrl = value;
		this.isStyleUrlDirty = true;
		setDirty();
	}

	public StyleSelector[] getStyleSelectors() {
		StyleSelector[] array = new StyleSelector[this.styleSelector.size()];
		return (StyleSelector[]) this.styleSelector.toArray(array);
	}

	public void removeStyleSelector(StyleSelector value) {
		if (value != null) {
			markDeletedNode(value);
			this.styleSelector.remove(value);
		}
	}

	public void addStyleSelector(StyleSelector value) {
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
			this.styleSelector.add(value);
		}
	}

	public StyleMap[] getStyleMaps() {
		List list = new ArrayList();
		for (Iterator iter = this.styleSelector.iterator(); iter.hasNext();) {
			StyleSelector cur = (StyleSelector) iter.next();
			if (cur instanceof StyleMap) {
				list.add((StyleMap) cur);
			}
		}
		StyleMap[] array = new StyleMap[list.size()];
		return (StyleMap[]) list.toArray(array);
	}

	public void addStyleMap(StyleMap value) {
		if (value != null) {
			value.setParent(this);
			this.styleSelector.add(value);
			markCreatedNode(value);
		}
	}

	public Style[] getStyles() {
		List list = new ArrayList();
		for (Iterator iter = this.styleSelector.iterator(); iter.hasNext();) {
			StyleSelector cur = (StyleSelector) iter.next();
			if (cur instanceof Style) {
				list.add((Style) cur);
			}
		}
		Style[] array = new Style[list.size()];
		return (Style[]) list.toArray(array);
	}

	public void addStyle(Style value) {
		if (value != null) {
			value.setParent(this);
			this.styleSelector.add(value);
			markCreatedNode(value);
		}
	}

	public Region getRegion() {
		return this.region;
	}

	public void addRegion(Region value) {
		if (this.region != null) {
			markDeletedNode(this.region);
		}
		this.region = value;
		if (value != null) {
			value.setParent(this);
			markCreatedNode(value);
		}
	}

	public Metadata getMetadata() {
		return this.metadata;
	}

	public void addMetadata(Metadata value) {
		if (this.metadata != null) {
			markDeletedNode(this.metadata);
		}
		this.metadata = value;
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
		kml += super.toKML(true);
		if (this.name != null) {
			kml += "<name>" + SpecialCaseFormatter.toKMLString(this.name)
					+ "</name>\n";
		}
		if (this.visibility != DEFAULT_VISIBILITY) {
			kml += "<visibility>" + this.visibility + "</visibility>\n";
		}
		if (this.open != DEFAULT_OPEN) {
			kml += "<open>" + this.open + "</open>\n";
		}
		if (this.address != null) {
			kml += "<address>" + SpecialCaseFormatter.toKMLString(this.address)
					+ "</address>\n";
		}
		if (this.phoneNumber != null) {
			kml += "<phoneNumber>"
					+ SpecialCaseFormatter.toKMLString(this.phoneNumber)
					+ "</phoneNumber>\n";
		}
		if (this.snippet != null) {
			kml += "<snippet>" + SpecialCaseFormatter.toKMLString(this.snippet)
					+ "</snippet>\n";
		}
		if (this.description != null) {
			kml += "<description>"
					+ SpecialCaseFormatter.toKMLString(this.description)
					+ "</description>\n";
		}
		if (this.lookAt != null) {
			kml += this.lookAt.toKML();
		}
		if (this.timePrimitive != null) {
			kml += this.timePrimitive.toKML();
		}
		if (this.styleUrl != null) {
			kml += "<styleUrl>"
					+ SpecialCaseFormatter.toKMLString(this.styleUrl)
					+ "</styleUrl>\n";
		}
		for (Iterator iter = this.styleSelector.iterator(); iter.hasNext();) {
			StyleSelector cur = (StyleSelector) iter.next();
			kml += cur.toKML();
		}
		if (this.region != null) {
			kml += this.region.toKML();
		}
		if (this.metadata != null) {
			kml += this.metadata.toKML();
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
		change += super.toUpdateKML(true);
		if (this.name != null && this.isNameDirty) {
			change += "<name>" + SpecialCaseFormatter.toKMLString(this.name)
					+ "</name>\n";
			this.isNameDirty = false;
		}
		if (this.isVisibilityDirty) {
			change += "<visibility>" + this.visibility + "</visibility>\n";
			this.isVisibilityDirty = false;
		}
		if (this.isOpenDirty) {
			change += "<open>" + this.open + "</open>\n";
			this.isOpenDirty = false;
		}
		if (this.address != null && this.isAddressDirty) {
			change += "<address>"
					+ SpecialCaseFormatter.toKMLString(this.address)
					+ "</address>\n";
			this.isAddressDirty = false;
		}
		if (this.phoneNumber != null && this.isPhoneNumberDirty) {
			change += "<phoneNumber>"
					+ SpecialCaseFormatter.toKMLString(this.phoneNumber)
					+ "</phoneNumber>\n";
			this.isPhoneNumberDirty = false;
		}
		if (this.snippet != null && this.isSnippetDirty) {
			change += "<snippet>"
					+ SpecialCaseFormatter.toKMLString(this.snippet)
					+ "</snippet>\n";
			this.isSnippetDirty = false;
		}
		if (this.description != null && this.isDescriptionDirty) {
			change += "<description>"
					+ SpecialCaseFormatter.toKMLString(this.description)
					+ "</description>\n";
			this.isDescriptionDirty = false;
		}
		if (this.lookAt != null && this.lookAt.isDirty()) {
			change += this.lookAt.toUpdateKML();
		}
		if (this.timePrimitive != null && this.timePrimitive.isDirty()) {
			change += this.timePrimitive.toUpdateKML();
		}
		if (this.styleUrl != null && this.isStyleUrlDirty) {
			change += "<styleUrl>"
					+ SpecialCaseFormatter.toKMLString(this.styleUrl)
					+ "</styleUrl>\n";
			this.isStyleUrlDirty = false;
		}
		for (Iterator iter = this.styleSelector.iterator(); iter.hasNext();) {
			StyleSelector cur = (StyleSelector) iter.next();
			if (cur.isDirty()) {
				change += cur.toUpdateKML();
			}
		}
		if (this.region != null && this.region.isDirty()) {
			change += this.region.toUpdateKML();
		}
		if (this.metadata != null && this.metadata.isDirty()) {
			change += this.metadata.toUpdateKML();
		}
		setNotDirty();
		return change;
	}

	public Object clone() throws CloneNotSupportedException {
		Feature result = (Feature) super.clone();
		if (result.lookAt != null) {
			result.lookAt = (LookAt) this.lookAt.clone();
			result.lookAt.setParent(result);
		}
		if (result.timePrimitive != null) {
			result.timePrimitive = (TimePrimitive) this.timePrimitive.clone();
			result.timePrimitive.setParent(result);
		}
		if (result.styleSelector != null) {
			result.styleSelector = new ArrayList();
			for (Iterator iter = this.styleSelector.iterator(); iter.hasNext();) {
				StyleSelector element = (StyleSelector) iter.next();
				StyleSelector elementClone = (StyleSelector) element.clone();
				elementClone.setParent(result);
				result.styleSelector.add(elementClone);
			}
		}
		if (result.region != null) {
			result.region = (Region) this.region.clone();
			result.region.setParent(result);
		}
		if (result.metadata != null) {
			result.metadata = (Metadata) this.metadata.clone();
			result.metadata.setParent(result);
		}
		return result;
	}

	public void setRecursiveNotDirty() {
		super.setRecursiveNotDirty();
		this.isNameDirty = false;
		this.isVisibilityDirty = false;
		this.isOpenDirty = false;
		this.isAddressDirty = false;
		this.isPhoneNumberDirty = false;
		this.isSnippetDirty = false;
		this.isDescriptionDirty = false;
		if (this.lookAt != null && this.lookAt.isDirty()) {
			this.lookAt.setRecursiveNotDirty();
		}
		if (this.timePrimitive != null && this.timePrimitive.isDirty()) {
			this.timePrimitive.setRecursiveNotDirty();
		}
		this.isStyleUrlDirty = false;
		for (Iterator iter = this.styleSelector.iterator(); iter.hasNext();) {
			StyleSelector cur = (StyleSelector) iter.next();
			cur.setRecursiveNotDirty();
		}
		if (this.region != null && this.region.isDirty()) {
			this.region.setRecursiveNotDirty();
		}
		if (this.metadata != null && this.metadata.isDirty()) {
			this.metadata.setRecursiveNotDirty();
		}
	}
}
