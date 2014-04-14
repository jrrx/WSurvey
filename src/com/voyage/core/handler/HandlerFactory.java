package com.voyage.core.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.voyage.util.Consts;
import com.voyage.util.LogUtil;

@Component
public class HandlerFactory {

	@Resource
	private PlainMsgHandler plainMsgHandler;
	@Resource
	private ScanHandler scanHandler;
	@Resource
	private SubscribeHandler subscribeHandler;

	public WxMsgHandler getWxMsgHandler(String msgType, String event) {
		LogUtil.GlobalLog.info("Find WxMsgHandler with parameter:" + msgType
				+ "/" + event);

		if (Consts.Wx_Msg_Event.equals(msgType)) {// 接收事件推送
			if (Consts.Wx_Event_Subscribe.equalsIgnoreCase(event)) {
				LogUtil.GlobalLog.debug("Will use subscribeHandler."
						+ subscribeHandler);
				return this.subscribeHandler; // 订阅事件
			} else if (Consts.Wx_Event_Scan.equalsIgnoreCase(event)) {
				LogUtil.GlobalLog.debug("Will use scanHandler." + scanHandler);
				return this.scanHandler; // 扫描事件
			}
		} else if (Consts.Wx_Msg_Text.equalsIgnoreCase(msgType)) {
			LogUtil.GlobalLog.debug("Will use plainMsgHandler."
					+ plainMsgHandler);
			return this.plainMsgHandler; // 接收普通消息
		}
		return null;
	}

}
