package com.voyage.meta;

//用户扫描带场景值二维码时，可能推送以下两种事件：
//如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
//如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者

public class WxMsg {

	private String toUserName; // 开发者微信号
	private String fromUserName; // 发送方帐号（一个OpenID）
	private int createTime;// 消息创建时间 （整型）
	private String msgType;// 消息类型，event
	private String event; // subscribe，scan
	// 若为订阅事件:事件KEY值，qrscene_为前缀，后面为二维码的参数值
	// 若用户已经关注，扫描事件，事件KEY值，是一个32位无符号整数
	private String eventKey;
	private String ticket; // 二维码的ticket，可用来换取二维码图片

	private String content;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
