package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class GoodBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String gID;
	public String gName;

	public String getgID() {
		return gID;
	}

	public void setgID(String gID) {
		this.gID = gID;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}
}
