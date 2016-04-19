package com.birds;


public class Bird {

	private String id;
	private String name;
	private String family;
	private String[] continents;
	private String added;
	private boolean visible = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String[] getContinents() {
		return continents;
	}

	public void setContinents(String[] continents) {
		this.continents = continents;
	}

	public String getAdded() {
		return added;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
