package com.voyage.meta.paper;

/**
 * 默认题目判断器，所有回答都视为正确
 * 
 * @author Houyangyang
 */
public class DefaultJudger implements AnswerJudger {

	@Override
	public boolean isReplyValid(String reply, int size) {
		return true;
	}

}
