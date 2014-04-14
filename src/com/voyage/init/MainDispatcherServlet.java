package com.voyage.init;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.voyage.cache.SurveyCache;
import com.voyage.conf.MsgConfig;
import com.voyage.meta.App;
import com.voyage.task.TokenJob;
import com.voyage.util.LogUtil;

public class MainDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 7976791347183908334L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			LogUtil.GlobalLog.info("VoyageSever main servlet inited ...");
			super.init(config);

			// 启动TokenJob
			if (Config.productMode && !Config.connectMode) {
				this.startTokenJob();
			}

			// 加载默认配置信息
			MsgConfig.initMsgs();

			// 缓存问卷信息
			if(Config.productMode && !Config.connectMode){
				this.initEhcache();
			}
			

			LogUtil.GlobalLog.info("VoyageSever main servlet inited ...");
		} catch (Throwable t) {
			t.printStackTrace();
			LogUtil.GlobalLog.error("Voyage server init failed", t);
			System.exit(0);
		}
	}

	@Override
	public void destroy() {
		LogUtil.GlobalLog
				.info("VoyageSever main servlet had been destroyed ...");
		super.destroy();
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() {
		App.appContext = super.initWebApplicationContext();
		LogUtil.GlobalLog.info("VoyageSever init webApplicationContext ok...");
		return App.appContext;
	}

	private void startTokenJob() {
		// 服务器接入微信平台之前， 先不要启动这个，否则服务器无法启动导致，无法接入
		String token = TokenJob.getAccessToken();
		LogUtil.GlobalLog.info(String.format("Refresh access token as: %s",
				token));
	}

	private void initEhcache() throws Exception {
		SurveyCache surveyCache = App.appContext.getBean(SurveyCache.class);
		surveyCache.initCache();
	}

}
