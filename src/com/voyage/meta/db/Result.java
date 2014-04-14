package com.voyage.meta.db;


public class Result {

	private int id;

	private int paperid;

	private int fanid;

	private int questionpos;

	private String answertext;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPaperid() {
		return paperid;
	}

	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}

	public int getFanid() {
		return fanid;
	}

	public void setFanid(int userid) {
		this.fanid = userid;
	}

	public int getQuestionpos() {
		return questionpos;
	}

	public void setQuestionpos(int questionpos) {
		this.questionpos = questionpos;
	}

	public String getAnswertext() {
		return answertext;
	}

	public void setAnswertext(String answertext) {
		this.answertext = answertext;
	}

}
