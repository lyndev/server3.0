package com.web.db.bean;

public class KeyAttrBean {

	private int batch;			//生成的批次
	
	private int keyType;
	
	private String reward;		//兑换奖励json
	
	private int useServer;		//限制服务器
	
	private short fgi; 			//渠道：uc、360
	
	private String platform;	// 平台安卓或者ios
	
	private short generateNum;
	
	private long deadTime;   
	
	private long generateTime;
	
	private String operator;
	
	private String descStr;

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}

	public int getKeyType() {
		return keyType;
	}

	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getUseServer() {
		return useServer;
	}

	public void setUseServer(int useServer) {
		this.useServer = useServer;
	}

	public short getFgi() {
		return fgi;
	}

	public void setFgi(short fgi) {
		this.fgi = fgi;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public short getGenerateNum() {
		return generateNum;
	}

	public void setGenerateNum(short generateNum) {
		this.generateNum = generateNum;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public long getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDescStr() {
		return descStr;
	}

	public void setDescStr(String descStr) {
		this.descStr = descStr;
	}
}
