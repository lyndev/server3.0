package com.web.db.bean;

public class UserBean {
	private long userId;
	
	private String userName;
	
	private String password;
	
	private int level;
	
	private long lastlogin;
	
	private boolean isDisable;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(long lastlogin) {
		this.lastlogin = lastlogin;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}
	
}
