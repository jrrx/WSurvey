package com.voyage.task;

import java.sql.Timestamp;
import java.util.List;

import net.sf.ehcache.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.voyage.cache.SurveyCache;
import com.voyage.conf.MsgConfig;
import com.voyage.core.SurveyStatus;
import com.voyage.core.WxService;
import com.voyage.init.Config;
import com.voyage.util.LogUtil;

@Component
public class SurveyNoticeJob {

	@Autowired
	private SurveyCache surveyCache;

	@Autowired
	private WxService wxService;

	@Scheduled(cron = "0 0/5 * * * *")
	//@Scheduled(cron = "0/10 * * * * *")
	public void surveyNoticeJob() {

		LogUtil.GlobalLog.info("Doing Notice Job....");

		Cache cache = surveyCache.getSurveyStatusCache();
		@SuppressWarnings("unchecked")
		List<String> userIds = cache.getKeys();
		for (String userId : userIds) {
			SurveyStatus ss = this.surveyCache.getSurveyStatus(userId);

			LogUtil.GlobalLog.info("Begin check user:" + ss.getUserOpenId());
			boolean needNotice = this.isNeedNotice(ss);
			LogUtil.GlobalLog.info("End check user:" + ss.getUserOpenId() + "["
					+ needNotice + "]");

			if (needNotice) {
				LogUtil.GlobalLog.info("send msg to:" + userId);
				// TODO 此处需要设置发送消息的超时时间 并且 用线程处理
				this.wxService.sendTextMsg(userId,
						MsgConfig.getMsg("survey_alert"));
			}
		}
	}

	private boolean isNeedNotice(SurveyStatus ss) {

		long now = System.currentTimeMillis();
		Timestamp lst = ss.getLastStatusTime();
		Timestamp lnt = ss.getLastNoticeTime();
		int step = ss.getSurveyStep();
		int noticeCount = ss.getNoticeCount();

		int interval = 0;

		// 未接受或者答题中 停止了一段时间
		if (step == 0 || step == 1) {
			// 判断是否过了N秒钟
			interval = (int) ((now - lst.getTime()) / 1000);
		} else if (step == 3) {// 之前已经提醒过了， 那就比上次提醒时间
			interval = (int) ((now - lnt.getTime()) / 1000);
		}

		if (interval >= Config.noticeInterval) {
			if (noticeCount >= 3) {
				// remove this user from cache(if user not finish survey,do
				// not store the answer, just in cache)
				this.surveyCache.removeSurveyStatus(ss.getUserOpenId());

				// TODO 此处需要设置发送消息的超时时间 并且 用线程处理
				this.wxService.sendTextMsg(ss.getUserOpenId(),
						MsgConfig.getMsg("survey_stop"));

				return false;// 不需要发信息
			}

			ss.setSurveyStep(3);
			ss.setLastNoticeTime(new Timestamp(now));
			ss.setNoticeCount(noticeCount + 1);
			return true;
		}
		return false;
	}

}
