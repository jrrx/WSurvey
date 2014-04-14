package com.voyage.meta.db;

/**
 * 数据库表：t_answer
 * 
 * @author Houyangyang
 */
public class Answer {

	private int id;
	private int questionid;
	private int position;
	private String content;

	@Override
	public String toString() {
		return this.content;
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionid() {
		return questionid;
	}

	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
