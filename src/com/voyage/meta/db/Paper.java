package com.voyage.meta.db;

import java.sql.Timestamp;
import java.util.List;

/**
 * 数据库表: t_paper
 * 
 * @author Houyangyang
 */
public class Paper {

	// table column
	private int id;
	private String title;
	private int paperstatus;
	private int papertype;
	private Timestamp createtime;
	private Timestamp lastmodifytime;
	private int authorid;

	// extra info
	private List<Question> questions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPaperstatus() {
		return paperstatus;
	}

	public void setPaperstatus(int paperstatus) {
		this.paperstatus = paperstatus;
	}

	public int getPapertype() {
		return papertype;
	}

	public void setPapertype(int papertype) {
		this.papertype = papertype;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getLastmodifytime() {
		return lastmodifytime;
	}

	public void setLastmodifytime(Timestamp lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}

	public int getAuthorid() {
		return authorid;
	}

	public void setAuthorid(int authorid) {
		this.authorid = authorid;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

}
