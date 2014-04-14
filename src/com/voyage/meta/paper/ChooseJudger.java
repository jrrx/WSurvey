package com.voyage.meta.paper;

/**
 * 选择题，回复判断器
 * 
 * @author Houyangyang
 */
public class ChooseJudger implements AnswerJudger {

	/**
	 * 0：代表数字选项， 1：代表字母选项
	 */
	private int replyType = 0; // inited by question rule
	private String[] ans_char = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };

	@Override
	public boolean isReplyValid(String reply, int size) {
		return this.judgeReply(reply, size);
	}

	private boolean judgeReply(String reply, int size) {
		if (replyType == Judger.answer_num) {
			int re = -1;
			try {
				re = Integer.parseInt(reply);
			} catch (NumberFormatException e) {
				return false;
			}
			return re >= 1 && re <= size; // 选项从1 开始，1、2、3、4...
		} else if (replyType == Judger.answer_char) {
			return this.isValidChar(reply, size);
		}
		return true;
	}

	private boolean isValidChar(String reply, int size) {
		reply = reply.trim();
		for (int i = 0; i < size && i < ans_char.length; i++) {
			if (ans_char[i].equalsIgnoreCase(reply)) {
				return true;
			}
		}
		return false;
	}

}
