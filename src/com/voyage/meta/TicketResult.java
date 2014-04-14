package com.voyage.meta;

/**
 * 获取QR二维码Ticket返回的对象
 * 
 * @author Houyangyang
 */
public class TicketResult {

	private String ticket;
	private int expire_seconds;

	private int errcode;
	private String errmsg;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	};

}
