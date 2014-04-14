package com.voyage.meta.db;

import java.util.List;

/**
 * 数据库：t_question
 * 
 * @author Houyangyang
 */
public class Question {

	// table column
	private int id;
	private String content;
	private int paperid;
	private int position;
	private int typeid;

	// extra info fields

	// answer info
	private List<Answer> answers;
	// question attribute
	private List<QuestionRule> rules;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.content).append("\n\n");
		for (Answer answer : answers) {
			sb.append(answer.toString()).append("\n");
		}
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPaperid() {
		return paperid;
	}

	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<QuestionRule> getRules() {
		return rules;
	}

	public void setRules(List<QuestionRule> rules) {
		this.rules = rules;
	}

}
