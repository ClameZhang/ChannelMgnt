package com.clame.channelmgnt.bean;

public class UserBean {
	public String userName;
	public String password;
	public boolean isInit;
	
	public UserBean() {
		isInit = true;
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
}
