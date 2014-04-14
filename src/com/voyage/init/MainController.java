package com.voyage.init;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.URLEncoder;
import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyage.core.WxService;
import com.voyage.core.handler.HandlerFactory;
import com.voyage.core.handler.WxMsgHandler;
import com.voyage.meta.TicketResult;
import com.voyage.meta.TokenResult;
import com.voyage.meta.WxMsg;
import com.voyage.util.LogUtil;

@Controller
@RequestMapping("/survey")
public class MainController {

	@Autowired
	private WxService wxService;

	@Resource
	private HandlerFactory handlerFactory;

	private ObjectMapper om = new ObjectMapper();

	@RequestMapping(value = "/doit", method = RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		boolean isValid = wxService.validateRequest(signature, timestamp,
				nonce, echostr);
		try {
			PrintWriter writer = response.getWriter();
			if (isValid) {
				writer.write(echostr);
			} else {
				LogUtil.GlobalLog
						.error(String
								.format("Connect to Weixin failed, the param is [signature:%s, timestamp:%s, nonce:%s, echostr:%s",
										signature, timestamp, nonce, echostr));
				writer.write("Can not recogonize your request!");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			LogUtil.GlobalLog.error("Connect to Weixin failed.", e);
		}

	}

	@RequestMapping(value = "/doit", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr = request.getParameter("echostr");
			boolean isValid = wxService.validateRequest(signature, timestamp,
					nonce, echostr);
			if (isValid == false) {
				return;
			}

			String postData = IOUtils.toString(request.getInputStream());
			WxMsg msg = wxService.parseUserInfo(postData);

			// 一行临时代码
			LogUtil.GlobalLog.info("接收到的msg对象:" + om.writeValueAsString(msg));
			String msgType = msg.getMsgType();
			WxMsgHandler wmh = handlerFactory.getWxMsgHandler(msgType,
					msg.getEvent());

			if (wmh != null) {
				String reStr = wmh.getResponseMsg(msg);
				if (reStr != null) {
					PrintWriter writer = response.getWriter();
					writer.print(reStr);
					writer.close();
				}
			}

			// // 接收事件推送
			// if (Consts.Wx_Msg_Event.equals(msgType)) {
			// String event = msg.getEvent();
			// if (Consts.Wx_Event_Subscribe.equalsIgnoreCase(event)) {
			// // 订阅事件
			// WxMsgHandler wmh = new SubscribeHandler();
			// String reStr = wmh.getResponseMsg(msg);
			// PrintWriter writer = response.getWriter();
			// writer.print(reStr);
			// writer.close();
			//
			// LogUtil.GlobalLog.info("Reply subscribe message:" + reStr);
			// } else if (Consts.Wx_Event_Scan.equalsIgnoreCase(event)) {
			// // 扫描事件
			// WxMsgHandler wmh = new ScanHandler();
			// String reStr = wmh.getResponseMsg(msg);
			// PrintWriter writer = response.getWriter();
			// writer.print(reStr);
			// writer.close();
			// }
			// } else if (Consts.Wx_Msg_Text.equalsIgnoreCase(msgType)) {
			// // 接收普通消息
			// WxMsgHandler wmh = new PlainMsgHandler();
			// String reStr = wmh.getResponseMsg(msg);
			// PrintWriter writer = response.getWriter();
			// writer.print(reStr);
			// writer.close();
			// }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getQrTicket")
	public String getQrTicket(HttpServletRequest request,
			HttpServletResponse response) {
		int sceneId = Integer.parseInt(request.getParameter("sceneId"));
		if (sceneId >= 1 && sceneId <= 1000) {
			TicketResult re = wxService.fetchQrcodeTicket(sceneId);
			if (re.getTicket() != null) {
				return re.getTicket();
			}
		}
		return null;
	}

	/**
	 * 根据SceneId获取二维码，SceneId是1到1000的整数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getQrcode")
	public void getQrcodeBySceneId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int sceneId = Integer.parseInt(request.getParameter("sceneId"));
		if (sceneId >= 1 && sceneId <= 1000) {
			TicketResult re = wxService.fetchQrcodeTicket(sceneId);
			String ticket = re.getTicket();
			if (ticket != null) {
				String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
				ticket = new URLEncoder().encode(ticket);// 按照微信API，对ticket进行UrlEncode
				url = String.format(url, ticket);
				response.sendRedirect(url);
				return;
			}
		} else {
			PrintWriter writer = response.getWriter();
			writer.println("The sceneId is not valid.");
			writer.close();
		}
	}

	// the interface is for test
	@RequestMapping("/test/getToken")
	// @RequestMapping("/test/getToken.go")
	public void test_AccessToken(HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException,
			IOException {
		TokenResult re = wxService.fetchAccessToken();
		ObjectMapper om = new ObjectMapper();
		response.getWriter().println(om.writeValueAsString(re));
	}

	@RequestMapping("/test/getTicket")
	public void test_AccessQrTicket(HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException,
			IOException {
		TicketResult re = wxService.fetchQrcodeTicket(1);
		ObjectMapper om = new ObjectMapper();
		response.getWriter().println(om.writeValueAsString(re));
	}

}
