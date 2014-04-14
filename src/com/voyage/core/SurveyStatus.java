package com.voyage.core;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 调查状态，记录参与调查的用户的状态
 * 
 * @author Houyangyang
 */
public class SurveyStatus {

	// 微信用户的openId
	private String userOpenId;

	// 用户对应的sceneId
	private String sceneId;

	// 用户最后扫描问卷二维码回答的问卷Id，不是二维码场景Id，因为二维码会随着时间的不同映射到不同的问卷上
	private int paperId;

	// 回答到了第几个问题
	private int questionPos = 0;

	// 用户所处的调查阶段：0 : 尚未接受，1: 已经接受，答题中，2：答题完毕; 3: 提醒答题中
	private int surveyStep = 0;

	// 问卷问题个数
	private int questionSize = -1;

	// 上次状态时间
	private Timestamp lastStatusTime;

	// 上次提醒时间
	private Timestamp lastNoticeTime;

	// 中间提醒答题次数
	private int noticeCount;

	// 存储用户回答的答案, key：回答的问题的位置questionpos, value: 回答的问题的文本answertext
	private Map<Integer, String> replies = new HashMap<Integer, String>();

	public String getUserOpenId() {
		return userOpenId;
	}

	public void setUserOpenId(String userOpenId) {
		this.userOpenId = userOpenId;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}

	public int getQuestionPos() {
		return questionPos;
	}

	public void setQuestionPos(int questionPos) {
		this.questionPos = questionPos;
	}

	public int getSurveyStep() {
		return surveyStep;
	}

	public void setSurveyStep(int surveyStep) {
		this.surveyStep = surveyStep;
	}

	public int getQuestionSize() {
		return questionSize;
	}

	public void setQuestionSize(int questionSize) {
		this.questionSize = questionSize;
	}

	public Timestamp getLastStatusTime() {
		return lastStatusTime;
	}

	public void setLastStatusTime(Timestamp lastStatusTime) {
		this.lastStatusTime = lastStatusTime;
	}

	public Timestamp getLastNoticeTime() {
		return lastNoticeTime;
	}

	public void setLastNoticeTime(Timestamp lastNoticeTime) {
		this.lastNoticeTime = lastNoticeTime;
	}

	public int getNoticeCount() {
		return noticeCount;
	}

	public void setNoticeCount(int noticeCount) {
		this.noticeCount = noticeCount;
	}

	public Map<Integer, String> getReplies() {
		return replies;
	}

	public void setReplies(Map<Integer, String> replies) {
		this.replies = replies;
	}

}
