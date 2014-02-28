package com.CeramicKoala.cs2340.model;

/**
 * DatabaseElement represents an abstract database element.
 * Extending this class enforces the id property, which all
 * database elements must have.
 * @author Benjamin Newcomer
 */
public abstract class DatabaseElement {
	
	protected int id;

	/**
	 * accessor for Id
	 * @return int Id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * setter for Id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("id: " + id + ".");
		return out.toString();
	}
	
}
