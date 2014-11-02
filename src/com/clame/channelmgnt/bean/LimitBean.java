package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class LimitBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String lID;
	public String lNum;

	public String getlID() {
		return lID;
	}

	public void setlID(String lID) {
		this.lID = lID;
	}

	public String getlNum() {
		return lNum;
	}

	public void setlNum(String lNum) {
		this.lNum = lNum;
	}
}
