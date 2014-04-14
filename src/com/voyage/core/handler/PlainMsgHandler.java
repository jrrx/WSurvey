package com.voyage.core.handler;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.voyage.cache.SurveyCache;
import com.voyage.conf.MsgConfig;
import com.voyage.core.MsgHandlerService;
import com.voyage.core.ResultStorer;
import com.voyage.core.SurveyStatus;
import com.voyage.meta.WxMsg;
import com.voyage.meta.db.Paper;
import com.voyage.meta.db.Question;
import com.voyage.meta.paper.AnswerJudger;
import com.voyage.meta.paper.Judger;
import com.voyage.meta.paper.JudgerFactory;
import com.voyage.task.ResultStoreJob;
import com.voyage.util.LogUtil;
import com.voyage.util.Utils;

@Component
public class PlainMsgHandler implements WxMsgHandler {

	private String[] ans = { "A", "B", "C", "D", "E", "F", "G" };

	private boolean isValidAnswer(String msg, int size) {
		msg = msg.trim();
		for (int i = 0; i < size && i < ans.length; i++) {
			if (ans[i].equalsIgnoreCase(msg)) {
				return true;
			}
		}
		return false;
	}

	@Resource
	private MsgHandlerService msgHandlerService;

	@Resource
	private SurveyCache surveyCache;
	
	@Resource
	private ResultStorer resultStorer;

	@Override
	public String getResponseMsg(WxMsg msg) {

		Timestamp now = Utils.getInstance().getCurrentTime();

		String userId = msg.getFromUserName();
		// String sceneId = msg.getEventKey();
		String msgContent = msg.getContent();
		String re = null;

		SurveyStatus ss = this.msgHandlerService.getUserSurveyStatus(userId);
		if (ss == null) {
			return null;
		}

		String sceneId = ss.getSceneId();
		int step = ss.getSurveyStep();
		if (step == 0 || step == 3) {
			if ("Y".equalsIgnoreCase(msgContent)) {
				// TODO getNextQuestion()
				Paper p = this.surveyCache.getPaper(sceneId);
				Question q = p.getQuestions().get(ss.getQuestionPos());
				re = q.toString();

				this.updateSSAnswering(ss, now);

			} else if ("N".equalsIgnoreCase(msgContent)) {
				return null;
			} else {
				re = MsgConfig.getMsg("invalid_accept");
			}
		} else if (step == 1) {
			Paper p = this.surveyCache.getPaper(sceneId);
			Question q = p.getQuestions().get(ss.getQuestionPos());
			
			boolean isvalid = false;
			
			if(p.getId()==1){ // 临时这么写， 因为没有加载问卷信息
				isvalid = this.isValidAnswer(msg.getContent(), q.getAnswers().size());
			}else if(p.getId()==2){
				AnswerJudger jg = JudgerFactory.getAnswerJudger(Judger.quetype_single_choose);
				isvalid = jg.isReplyValid(msg.getContent(), q.getAnswers().size());
			} 
			
			if(isvalid){
				// 如果答案有效
				ss.getReplies().put(ss.getQuestionPos(), msg.getContent());
				ss.setQuestionPos(ss.getQuestionPos() + 1);
				if (ss.getQuestionPos() >= ss.getQuestionSize()) {
					re = MsgConfig.getMsg("goodbye_msg");
					ss.setSurveyStep(2);
					
					//删除用户缓存
					this.msgHandlerService.removeUserSurveyStatus(userId);
					
					// 存储用户的回答
					LogUtil.GlobalLog.info("Store user replies, the user is:"+ ss.getUserOpenId()
							+",and the size is:"+ss.getReplies().size());
					ResultStoreJob.storeResult(ss);
					
				} else {
					q = p.getQuestions().get(ss.getQuestionPos());
					re = q.toString();

					this.updateSSAnswering(ss, now);
				}
			}else {
				// 如果答案无法识别
				re = MsgConfig.getMsg("errorreturn_msg") + "\n" + q.toString();
			}
			
			
//			if (this.isValidAnswer(msg.getContent(), q.getAnswers().size())) {
//				// 如果答案有效
//				ss.setQuestionPos(ss.getQuestionPos() + 1);
//				if (ss.getQuestionPos() >= ss.getQuestionSize()) {
//					re = MsgConfig.getMsg("goodbye_msg");
//					ss.setSurveyStep(2);
//				} else {
//					q = p.getQuestions().get(ss.getQuestionPos());
//					re = q.toString();
//
//					this.updateSSAnswering(ss, now);
//				}
//			} else {
//				// 如果答案无法识别
//				re = MsgConfig.getMsg("errorreturn_msg") + "\n" + q.toString();
//			}
		} else if (step == 2) {
			// TODO in demo, do nothing
		}

		String content = this.msgHandlerService.getReTextMsg(userId, re);
		return content;
	}

	private void updateSSAnswering(SurveyStatus ss, Timestamp now) {
		ss.setSurveyStep(1);
		ss.setNoticeCount(0);
		ss.setLastStatusTime(now);
		ss.setLastNoticeTime(null);
	}
}
