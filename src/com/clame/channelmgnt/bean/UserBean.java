package com.clame.channelmgnt.bean;

import java.io.Serializable;

public class UserBean implements Serializable  {
	private static final long serialVersionUID = 4226755799531293257L;
	
	public String userName;
	public String password;
	public boolean isInit;
	public String uLevel;
	public String serialID;
	
	public UserBean() {
		isInit = false;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	public String getuLevel() {
		return uLevel;
	}

	public void setuLevel(String uLevel) {
		this.uLevel = uLevel;
	}

	public String getSerialID() {
		return serialID;
	}

	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}
}
