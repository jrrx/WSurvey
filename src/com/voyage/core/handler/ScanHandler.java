package com.voyage.core.handler;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.voyage.conf.MsgConfig;
import com.voyage.core.MsgHandlerService;
import com.voyage.core.SurveyStatus;
import com.voyage.meta.WxMsg;
import com.voyage.util.Utils;

@Component
public class ScanHandler implements WxMsgHandler {

	@Resource
	private MsgHandlerService msgHandlerService;

	@Override
	public String getResponseMsg(WxMsg msg) {

		Timestamp now = Utils.getInstance().getCurrentTime();

		String userId = msg.getFromUserName();
		String eventKey = msg.getEventKey();
		String sceneId = eventKey.replace("qrscene_", "");

		//先删除刚才的缓存的信息
		this.msgHandlerService.removeUserSurveyStatus(userId);
		
		SurveyStatus ss = this.msgHandlerService.getUserSurveyStatus(userId,
				sceneId);
		if (ss.getSurveyStep() == 2) {
			ss.setQuestionPos(0);
			ss.setSurveyStep(0);
			ss.setSceneId(sceneId);

			this.updateSSBeginSurvey(ss, now);

			String welcome_msg = MsgConfig.getMsg("welcome_msg_survey");
			welcome_msg = String.format(welcome_msg, ss.getQuestionSize());
			return this.msgHandlerService.getReTextMsg(userId, welcome_msg);

			// return null;
		} else if (ss.getSurveyStep() == 0) {
			String welcome_msg = MsgConfig.getMsg("welcome_msg_survey");
			welcome_msg = String.format(welcome_msg, ss.getQuestionSize());
			return this.msgHandlerService.getReTextMsg(userId, welcome_msg);
		} else if (ss.getSurveyStep() == 1) {
			ss.setQuestionPos(0);
			ss.setSurveyStep(0);
			ss.setSceneId(sceneId);

			this.updateSSBeginSurvey(ss, now);

			String welcome_msg = MsgConfig.getMsg("welcome_msg_survey");
			welcome_msg = String.format(welcome_msg, ss.getQuestionSize());
			return this.msgHandlerService.getReTextMsg(userId, welcome_msg);
		}
		return null;
	}

	private void updateSSBeginSurvey(SurveyStatus ss, Timestamp now) {
		ss.setSurveyStep(0);
		ss.setNoticeCount(0);
		ss.setLastStatusTime(now);
		ss.setLastNoticeTime(null);
	}

	// @Override
	// public String getResponseMsg(WxMsg msg) {
	// String userId = msg.getFromUserName();
	//
	// Document doc = DocumentHelper.createDocument();
	// Element root = doc.addElement("xml");
	// Element ele = root.addElement("ToUserName");
	// ele.addCDATA(userId);
	// ele = root.addElement("FromUserName");
	// ele.addCDATA(Consts.WxName);
	//
	// long now = System.currentTimeMillis();
	// ele = root.addElement("CreateTime");
	// ele.addText("" + now);
	//
	// ele = root.addElement("MsgType");
	// ele.addCDATA(Consts.Wx_Msg_Text);
	// ele = root.addElement("Content");
	// ele.addCDATA("扫描事件：欢迎再次和微洞察互动，你的用户Id是：" + userId + ",msgkey is:"
	// + msg.getEventKey());
	//
	// return doc.asXML();
	// }

}
