package com.voyage.core.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.voyage.cache.SurveyCache;
import com.voyage.conf.MsgConfig;
import com.voyage.core.MsgHandlerService;
import com.voyage.core.SurveyStatus;
import com.voyage.meta.WxMsg;
import com.voyage.util.LogUtil;
import com.voyage.util.StrUtil;

/**
 * 
 * 订阅事件处理器
 * 
 * @author Houyangyang
 */
@Component
public class SubscribeHandler implements WxMsgHandler {

	@Resource
	private SurveyCache surveyCache;

	@Resource
	private MsgHandlerService msgHandlerService;

	@Override
	public String getResponseMsg(WxMsg msg) {
		String re = null;

		String userId = msg.getFromUserName();
		// eventKey format in subscribe event is like "eventKey":"qrscene_1"
		String eventKey = msg.getEventKey();
		String sceneId = eventKey.replace("qrscene_", "");

		LogUtil.GlobalLog.info("The sceneId is:" + sceneId);
		String welcome_msg = null;
		if (StrUtil.it().isEmpty(sceneId)) {
			welcome_msg = MsgConfig.getMsg("welcome_msg");
		} else {
			SurveyStatus ss = this.msgHandlerService.getUserSurveyStatus(
					userId, sceneId);
			LogUtil.GlobalLog.info("ss is:" + ss);
			welcome_msg = MsgConfig.getMsg("welcome_msg_survey");
			welcome_msg = String.format(welcome_msg, ss.getQuestionSize());
		}
		re = this.msgHandlerService.getReTextMsg(userId, welcome_msg);

		LogUtil.GlobalLog.info("The return msg is:[" + re + "]");

		return re;
	}

	// @Override
	// public String getResponseMsg(WxMsg msg) {
	// String userId = msg.getFromUserName();
	// // eventKey format in subscribe event is like "eventKey":"qrscene_1"
	// String eventKey = msg.getEventKey();
	// String sceneId = eventKey.replace("qrscene_", "");
	//
	// SurveyStatus ss = this.getUserSurveyStatus(userId, sceneId);
	// String welcome_msg = MsgConfig.getMsg("welcome_msg");
	// welcome_msg = String.format(welcome_msg, ss.getQuestionSize());
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
	// ele.addCDATA(welcome_msg);
	//
	// return doc.asXML();
	// }

}