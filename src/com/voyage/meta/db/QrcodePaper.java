package com.voyage.meta.db;

/**
 * 数据库表：t_qrcode_paper
 * 
 * @author Houyangyang
 */
public class QrcodePaper {

	private int id;
	private int sceneid;
	private int paperid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSceneid() {
		return sceneid;
	}

	public void setSceneid(int sceneid) {
		this.sceneid = sceneid;
	}

	public int getPaperid() {
		return paperid;
	}

	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}

}
