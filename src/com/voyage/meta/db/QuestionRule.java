package com.voyage.meta.db;

/**
 * 问题规则，不同类型的问题需要限定不同的规则
 * 
 * @author Houyangyang
 */
public class QuestionRule {

	private int id;
	private int questionid;
	private String attrkey;
	private String attrvalue;

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

	public String getAttrkey() {
		return attrkey;
	}

	public void setAttrkey(String attrkey) {
		this.attrkey = attrkey;
	}

	public String getAttrvalue() {
		return attrvalue;
	}

	public void setAttrvalue(String attrvalue) {
		this.attrvalue = attrvalue;
	}

}
