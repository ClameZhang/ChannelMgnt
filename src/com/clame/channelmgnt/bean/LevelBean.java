package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class LevelBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String lID;
	public String lName;

	public String getlID() {
		return lID;
	}

	public void setlID(String lID) {
		this.lID = lID;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

}
