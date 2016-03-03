package models;

import interfaces.VersionInterface;

public class Version implements VersionInterface {
	private String path;
	private String type;
	
	public Version(String path, String type){
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
