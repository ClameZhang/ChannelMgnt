package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class ManagementHistoryBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String time;
	public String goodName;
	public String nfc;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getNfc() {
		return nfc;
	}

	public void setNfc(String nfc) {
		this.nfc = nfc;
	}
}
