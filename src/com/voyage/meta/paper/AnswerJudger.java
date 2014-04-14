package com.voyage.meta.paper;

public interface AnswerJudger {

	/**
	 * @param reply
	 *            用户的回复
	 * @param size
	 *            问题的选项
	 * @return true: 是合理的回答 ；false: 不合理的回答
	 */
	public boolean isReplyValid(String reply, int size);

}
