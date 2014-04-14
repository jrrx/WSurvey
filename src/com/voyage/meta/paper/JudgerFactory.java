package com.voyage.meta.paper;

public class JudgerFactory {

	public static AnswerJudger getAnswerJudger(int questionType) {
		AnswerJudger aj = null;
		switch (questionType) {
		case Judger.quetype_single_choose:
			aj = new ChooseJudger();
			break;
		default:
			aj = new DefaultJudger();
			break;
		}
		return aj;
	}

}
