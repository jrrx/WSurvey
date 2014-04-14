package com.voyage.core;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import com.voyage.cache.SurveyCache;
import com.voyage.meta.db.Paper;
import com.voyage.util.Consts;
import com.voyage.util.LogUtil;
import com.voyage.util.Utils;

@Component
public class MsgHandlerService {

	@Resource
	private SurveyCache surveyCache;

	public SurveyStatus getUserSurveyStatus(String userId, String sceneId) {
		SurveyStatus ss = this.surveyCache.getSurveyStatus(userId);
		if (ss == null) {
			ss = new SurveyStatus();
			ss.setUserOpenId(userId);
			ss.setLastStatusTime(Utils.getInstance().getCurrentTime());
			ss.setSceneId(sceneId);
			
			LogUtil.GlobalLog.info("the sceneId is:"+ sceneId);
			
			Paper paper = this.surveyCache.getPaper(sceneId);
			
			LogUtil.GlobalLog.info("The paper is :" +paper);
			
			ss.setQuestionSize(paper.getQuestions().size());
			ss.setPaperId(paper.getId());
			
			this.surveyCache.addSurveyStatus(ss);
			return ss;
		}
		
//		if(!ss.getSceneId().equals(sceneId)){//扫描不同的问卷二维码,此处需要讨论， 第一个问卷没答完，就扫描第二个
//			Paper paper = this.surveyCache.getPaper(sceneId);
//			return null;
//		}
		
		ss.setSceneId(sceneId);
		return ss;
	}

	public SurveyStatus getUserSurveyStatus(String userId) {
		return this.surveyCache.getSurveyStatus(userId);
	}
	
	public void removeUserSurveyStatus(String userId){
		this.surveyCache.removeSurveyStatus(userId);
	}

	public String getReTextMsg(String userId, String content) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");
		Element ele = root.addElement("ToUserName");
		ele.addCDATA(userId);
		ele = root.addElement("FromUserName");
		ele.addCDATA(Consts.WxName);

		long now = System.currentTimeMillis();
		ele = root.addElement("CreateTime");
		ele.addText("" + now);

		ele = root.addElement("MsgType");
		ele.addCDATA(Consts.Wx_Msg_Text);
		ele = root.addElement("Content");
		ele.addCDATA(content);
		return doc.asXML();
	}

}
