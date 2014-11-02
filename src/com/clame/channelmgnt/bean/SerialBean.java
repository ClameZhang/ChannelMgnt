package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class SerialBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String sID;
	public String sName;

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}
}
