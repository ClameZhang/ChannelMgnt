package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class UserInfoBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String ID;
	public String Name;
	public String Level;

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}
}
