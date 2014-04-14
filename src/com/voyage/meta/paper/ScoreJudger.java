package com.voyage.meta.paper;

/**
 * 打分题目，回复判断器 <br>
 * TODO No using now
 * 
 * @author Houyangyang
 */
public class ScoreJudger implements AnswerJudger {

	// 打分题目的最高和最低分数
	private int lowScore = 0;
	private int highScore = 0;

	@Override
	public boolean isReplyValid(String reply, int size) {
		int score = lowScore - 1;
		try {
			score = Integer.parseInt(reply);
		} catch (NumberFormatException e) {
			// do nothing
		}
		return score >= lowScore && score <= highScore;
	}

	public int getLowScore() {
		return lowScore;
	}

	public void setLowScore(int lowScore) {
		this.lowScore = lowScore;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

}
