package com.voyage.task;

import java.sql.Timestamp;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.voyage.core.WxService;
import com.voyage.meta.App;
import com.voyage.meta.TokenResult;
import com.voyage.util.LogUtil;
import com.voyage.util.Utils;

/**
 * 定时获取微信账户的access_token
 * 
 * @author Houyangyang
 */
@Component
public class TokenJob {

	private static WxService wxService;

	private static Object lock = new Object();
	private static String access_token = null;
	private static Timestamp lastAccessTime = null;
	private static int expires_in = 0;

	private static void refreshAccessToken() {

		LogUtil.GlobalLog.info("Begin to refresh AccessToken >>>");

		Timestamp time = new Timestamp(System.currentTimeMillis());
		TokenResult token = wxService.fetchAccessToken();

		if (token.getAccess_token() != null) {
			lastAccessTime = time;
			access_token = token.getAccess_token();
			expires_in = token.getExpires_in();
		} else {
			int count = 0;
			if (token.getErrcode() == 1) {// 系统繁忙
				while (true) {
					time = new Timestamp(System.currentTimeMillis());
					token = wxService.fetchAccessToken();
					// 如果获取到了，则正常结束
					if (token.getAccess_token() != null) {
						lastAccessTime = time;
						access_token = token.getAccess_token();
						expires_in = token.getExpires_in();
						break;
					}

					// 如果获取了10次都取不到，就停止获取，防止获取次数过多，因为weixin api调用次数有限制
					if (count >= 10) {
						break;
					}
					count++;

					// 等待一分钟 然后再尝试获取
					try {
						lock.wait(1 * 60 * 1000);
					} catch (InterruptedException e) {
						LogUtil.GlobalLog.warn(e);
					}
				}
			}
		}

		LogUtil.GlobalLog
				.info(String
						.format("End refresh AccessToken, lastAccessTime:%s, expires_in:%d, access_token:%s <<<",
								Utils.getInstance().getTimeStr(lastAccessTime),
								expires_in, access_token));
	}

	@Scheduled(cron = "0 0/5 * * * *")
	public void accessTokenJob() {
		// cron = "0 0/60 * * * *" 表示每60分钟执行一次
		// cron = "0/5 * * * * *" 表示每5秒钟执行一次
		if (lastAccessTime == null) {
			// 如果还没有取过access_token
			refreshAccessToken();
		} else {
			// 如果access_token过期了或者快要过期了
			long now = System.currentTimeMillis();
			long expire = lastAccessTime.getTime() + expires_in * 1000;
			long distance = expire - now;
			if (now >= expire || distance <= 20 * 60 * 1000) {
				refreshAccessToken();
			}
		}
	}

	public static String getAccessToken() {
		if (wxService == null) {
			wxService = App.appContext.getBean(WxService.class);
		}
		if (access_token == null) {
			refreshAccessToken();
		}
		return access_token;
	}

}
