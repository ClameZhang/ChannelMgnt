package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class PackageHistoryBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String time;
	public String name;
	public String nfcID;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNfcID() {
		return nfcID;
	}

	public void setNfcID(String nfcID) {
		this.nfcID = nfcID;
	}
}
