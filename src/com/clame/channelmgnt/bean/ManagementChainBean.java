package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class ManagementChainBean implements Serializable {
	private static final long serialVersionUID = 4226755799531293257L;

	public String time;
	public String sendLID;
	public String sendID;
	public String sendLName;
	public String sendName;
	public String recvLID;
	public String recvID;
	public String recvLName;
	public String recvName;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSendID() {
		return sendID;
	}

	public void setSendID(String sendID) {
		this.sendID = sendID;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendLID() {
		return sendLID;
	}

	public void setSendLID(String sendLID) {
		this.sendLID = sendLID;
	}

	public String getRecvID() {
		return recvID;
	}

	public void setRecvID(String recvID) {
		this.recvID = recvID;
	}

	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

	public String getRecvLID() {
		return recvLID;
	}

	public void setRecvLID(String recvLID) {
		this.recvLID = recvLID;
	}

	public String getSendLName() {
		return sendLName;
	}

	public void setSendLName(String sendLName) {
		this.sendLName = sendLName;
	}

	public String getRecvLName() {
		return recvLName;
	}

	public void setRecvLName(String recvLName) {
		this.recvLName = recvLName;
	}
}
