package com.web.logic.construct;

public class Mail {
	
    private int type; //邮件发送类型：1-所有玩家，2-玩家userId：userId_userId，3-等级范围：min_max
    
    private String parameter;	//type所对应的参数，所有玩家无参数
    
    private String sendTime;	//发送时间
    
    private String deadTime;	//结束时间
    
    private String title; 	//标题
    
    private String content;	//内容
    
    private String senderName; //发送者
    
    private String resStr; //资源附件：类别_数量;类别_数量
    
    private String itemStr; //道具附件：ID_数量;ID_数量

    
	public Mail(int type, String parameter, String sendTime, String deadTime,
			String title, String content, String senderName, String resStr,
			String itemStr) {
		super();
		this.type = type;
		this.parameter = parameter;
		this.sendTime = sendTime;
		this.deadTime = deadTime;
		this.title = title;
		this.content = content;
		this.senderName = senderName;
		this.resStr = resStr;
		this.itemStr = itemStr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getParameter() {
		return parameter;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(String deadTime) {
		this.deadTime = deadTime;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getResStr() {
		return resStr;
	}

	public void setResStr(String resStr) {
		this.resStr = resStr;
	}

	public String getItemStr() {
		return itemStr;
	}

	public void setItemStr(String itemStr) {
		this.itemStr = itemStr;
	}
    
}
