package com.CeramicKoala.cs2340.model;

public abstract class DatabaseElement {
	
	protected String name;
	protected int id;
	
	/**
	 * accessor for name
	 * @return username
	 */
	public String getName() {
		return name;
	}

	/**
	 * accessor for Id
	 * @return int Id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * setter for name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
		out.append("name: " + name + ", ");
		out.append("id: " + id + ".");
		return out.toString();
	}
	
}
