package com.beenthere.kml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author power
 *
 */
abstract public class Node implements Cloneable
{
    public static final String NAMESPACE_URI = "http://earth.google.com/kml/2.1"; //$NON-NLS-1$
    
    protected Node parent;
    private boolean isPrimDirty;
    private boolean isChildDirty;
    
    protected static List createdNodes = new ArrayList();
    protected static List deletedNodes = new ArrayList();
  
    ////////////////////// Constructors ///////////////////////////////
    public Node() {
	// Empty Constructor, only used by Kml, since that's
	// the only Node without a parent.
    }

    public Node(Node parent) {
    	this.parent = parent;
    }

    ////////////////////// Public methods  ///////////////////////////////

    ////////////////////// Getters and setters  ///////////////////////////////
    
    /**
     * Is this node dirty ? ie. has it been modified since last update
     * @return whether it's dirty or not.
     */
    protected boolean isPrimitiveDirty() {
    	return this.isPrimDirty;
    }

    /**
     * Is this node dirty ? ie. has it been modified since last update
     * @return whether it's dirty or not.
     */
    protected boolean isDirty() {
    	return (this.isPrimDirty | this.isChildDirty);
    }
    
    /**
     * Mark this node as dirty, recursively set parent nodes dirty.
     *
     */
	protected void setDirty() {
		this.isPrimDirty = true;
		if (this.parent != null) { 
			// only recurse if we haven't already, and
			// parent's not null
			this.parent.setChildDirty();
		}
	}

	/**
	 * Mark this node as dirty, recursively set parent nodes dirty.
	 * 
	 */
	protected void setChildDirty() {
		this.isChildDirty = true;
		if (this.parent != null) {
			// only recurse if we haven't already, and
			// parent's not null
			this.parent.setChildDirty();
		}
	}
    
	/**
	 * Set dirty to false, note not recursive
	 */
	protected void setNotDirty() {
		this.isPrimDirty = false;
		this.isChildDirty = false;
	}

	protected void setRecursiveNotDirty() {
		this.isPrimDirty = false;
		this.isChildDirty = false;
	}

	protected void setParent(Node aNode) {
		this.parent = aNode;
	}

	public String toKML(boolean suppressEnclosingTags) {
		return "";
	}

	protected String toKML() {
		return toKML(false);
	}

	protected String toUpdateKML() {
		return toUpdateKML(false);
	}

	protected String toUpdateKML(boolean suppressEnclosingTags) {
		return "";
	}

	protected void markCreatedNode(Node newNode) {
		// only ObjectNodes can be updated at the moment
		if (!(newNode instanceof ObjectNode))
			return;

		if (!containsAncestor(createdNodes, newNode)) {
			Node.createdNodes.add(newNode);
		}
	}

	protected void markDeletedNode(Node newNode) {
		// only Features can be updated at the moment
		if (!(newNode instanceof Feature))
			return;

		if (!containsAncestor(createdNodes, newNode)) {
			Node.deletedNodes.add(newNode);
		}
	}

	protected List removeDescendants(List list) {
		List desc = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Node cur = (Node) iter.next();
			if (!containsAncestor(list, cur)) {
				desc.add(cur);
			}
		}
		return desc;
	}

	protected boolean containsAncestor(List list, Node newNode) {
		Node cur = newNode;
		while (cur.parent != null) {
			cur = cur.parent;
			if (list.contains(cur))
				return true;
		}
		return false;
	}
	
	public Node handleTag(String tag, String text) {
		return null;
	}

	public String toDeleteKML() {
		deletedNodes = removeDescendants(deletedNodes);

		StringBuilder delete = new StringBuilder();
		if (deletedNodes != null) {
			for (Iterator iter = deletedNodes.iterator(); iter.hasNext();) {
				Node newNode = (Node) iter.next();
				delete.append("<" + newNode.getClass().getSimpleName()
						+ " targetId=\"" + ((ObjectNode) newNode).getId()
						+ "\"></>\n");
			}
			deletedNodes.clear();
		}
		return delete.toString();
	}

	public String toCreateKML() {
		createdNodes = removeDescendants(createdNodes);

		StringBuilder create = new StringBuilder();
		if (createdNodes != null) {
			for (Iterator iter = createdNodes.iterator(); iter.hasNext();) {
				Node newNode = (Node) iter.next();
				if (newNode.parent instanceof ObjectNode) // ie. not Kml
				// node
				{
					ObjectNode newNodeParent = (ObjectNode) newNode.parent;
					create.append("<"
							+ newNodeParent.getClass().getSimpleName()
							+ " targetId=\"" + newNodeParent.getId() + "\">\n");
					create.append(newNode.toKML());
					create.append("<"
							+ newNodeParent.getClass().getSimpleName() + ">\n");
				} else {
					create.append(newNode.toKML());
				}
				newNode.setRecursiveNotDirty();
			}
			createdNodes.clear();
		}
		return create.toString();
	}
	// //////////////////// Private methods ///////////////////////////////
}
