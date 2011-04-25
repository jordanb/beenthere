package com.beenthere.kml;

abstract public class Container extends Feature {

	public Container() {
		super();
	}

	public Container(Node parent) {
		super(parent);
	}

	public Node handleTag(String tag, String text) {
		return super.handleTag(tag, text);
	}

	public Object clone() throws CloneNotSupportedException {
		Container result = (Container) super.clone();
		return result;
	}

	public void setRecursiveNotDirty() {
		super.setRecursiveNotDirty();
	}
}
