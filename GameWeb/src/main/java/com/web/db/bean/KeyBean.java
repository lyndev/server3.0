package com.web.db.bean;

public class KeyBean {
	
	private String keyCode;
	
	private short attrId;
	
	private boolean isUse;
	
	private String userInfo;


	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public short getAttrId() {
		return attrId;
	}

	public void setAttrId(short attrId) {
		this.attrId = attrId;
	}

	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	
}
