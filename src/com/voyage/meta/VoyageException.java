package com.voyage.meta;

import java.io.PrintWriter;
import java.io.StringWriter;

public class VoyageException extends RuntimeException {

	private static final long serialVersionUID = 4013966944892694251L;

	private String code;
	private String msg;
	private Throwable t;

	public VoyageException(Throwable t) {
		this.t = t;
	}

	public VoyageException(String msg) {
		this.msg = msg;
	}

	public VoyageException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public VoyageException(String code, String msg, Throwable t) {
		this.code = code;
		this.msg = msg;
		this.t = t;
	}

	@Override
	public String toString() {
		StringBuffer msg = new StringBuffer();
		msg.append(this.msg).append("\n");
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			msg.append(sw.toString());
			sw.close();
		} catch (Exception e) {
			msg.append(e.toString());
		}
		String ret = msg.toString();
		return ret;
	};

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Throwable getT() {
		return t;
	}

	public void setT(Throwable t) {
		this.t = t;
	}

}
