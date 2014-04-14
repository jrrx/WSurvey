package com.voyage.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.voyage.meta.TicketResult;
import com.voyage.meta.TokenResult;
import com.voyage.meta.WxMsg;
import com.voyage.task.TokenJob;
import com.voyage.util.Consts;
import com.voyage.util.LogUtil;

@Service
public class WxService {

	private ObjectMapper jsonMapper = new ObjectMapper();

	@Autowired
	private MsgHandlerService msgHandlerService;

	/**
	 * 验证参数是否符合微信的验证规则
	 * 
	 * @param request
	 * @return true:说明请求参数符合微信请求验证规则，发送参数的请求可信任；false:不信任
	 */
	public boolean validateRequest(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		return this.validateRequest(signature, timestamp, nonce, echostr);
	}

	/**
	 * 验证参数是否符合微信的验证规则
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return true:说明请求参数符合微信请求验证规则，发送参数的请求可信任；false:不信任
	 */
	public boolean validateRequest(String signature, String timestamp,
			String nonce, String echostr) {
		String[] strs = { Consts.Token, timestamp, nonce };
		Arrays.sort(strs);
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		String allStr = sb.toString();
		String sha1Str = DigestUtils.sha1Hex(allStr);
		if (signature.equals(sha1Str)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 解析用户的Msg
	 * 
	 * @param postXml
	 * @return
	 * @throws DocumentException
	 */
	public WxMsg parseUserInfo(String postXml) throws DocumentException {
		WxMsg msg = new WxMsg();
		Document doc = DocumentHelper.parseText(postXml);
		Element root = doc.getRootElement();
		msg.setToUserName(root.elementText("ToUserName"));
		msg.setFromUserName(root.elementText("FromUserName"));
		msg.setCreateTime(Integer.parseInt(root.elementText("CreateTime")));
		msg.setMsgType(root.elementText("MsgType"));
		msg.setEvent(root.elementText("Event"));
		msg.setEventKey(root.elementText("EventKey"));
		msg.setTicket(root.elementText("Ticket"));
		msg.setContent(root.elementText("Content"));
		return msg;
	}

	/**
	 * @return 微信的access_token
	 */
	public TokenResult fetchAccessToken() {
		TokenResult tr = new TokenResult();
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		url = String.format(url, Consts.AppId, Consts.AppSecret);
		try {
			String responseStr = weixinGet(url);
			if (responseStr != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> res = jsonMapper.readValue(responseStr,
						Map.class);
				// {"access_token":"ACCESS_TOKEN","expires_in":7200}
				Object re = res.get("access_token");
				if (re != null) {
					tr.setAccess_token((String) re);
					tr.setExpires_in((Integer) res.get("expires_in"));
				} else {
					// like {"errcode":40013,"errmsg":"invalid appid"}
					re = res.get("errcode");
					tr.setErrcode((Integer) re);
					tr.setErrmsg((String) res.get("errmsg"));
					LogUtil.GlobalLog.error(String.format(
							"Get access_token error, errorcode:%d, errmsg:%s",
							tr.getErrcode(), tr.getErrmsg()));
				}
			}
		} catch (Throwable t) {
			LogUtil.GlobalLog
					.error("Error occured when get access_token:\n", t);
		}
		return tr;
	}

	public void sendTextMsg(String userId, String content) {
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("touser", userId);
			map.put("msgtype", "text");

			Map<String,Object> subMap = new HashMap<String, Object>();
			subMap.put("content", content);
			map.put("text", subMap);
			//content = jsonMapper.writeValueAsString(map);
			String reMsg = jsonMapper.writeValueAsString(map);
			
			String token = TokenJob.getAccessToken();
			String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
			url = String.format(url, token);
			String re = this.weixinPost(url, reMsg);
			LogUtil.GlobalLog.warn("After send notice info and the re:" + re);
		} catch (Exception e) {
			LogUtil.GlobalLog.warn("Send notice info failed.", e);
		}
	}

	public TicketResult fetchQrcodeTicket(int sceneId) {
		TicketResult tr = new TicketResult();

		String token = TokenJob.getAccessToken();
		String url = String
				.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s",
						token);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode subNode1 = mapper.createObjectNode();
		subNode1.put("scene_id", sceneId);
		ObjectNode subNode2 = mapper.createObjectNode();
		subNode2.put("scene", subNode1);
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("action_name", "QR_LIMIT_SCENE");
		rootNode.put("action_info", subNode2);
		String paramStr = rootNode.toString();
		try {
			String reStr = this.weixinPost(url, paramStr);
			@SuppressWarnings("unchecked")
			Map<String, Object> res = jsonMapper.readValue(reStr, Map.class);
			LogUtil.GlobalLog.info("fetchQrcodeTicket:" + reStr);
			Object re = res.get("ticket");
			if (re != null) {
				tr.setTicket((String) re);
				tr.setExpire_seconds((Integer) res.get("expire_seconds"));
			} else {
				re = res.get("errcode");
				tr.setErrcode((Integer) re);
				tr.setErrmsg((String) res.get("errmsg"));
				LogUtil.GlobalLog.error(String.format(
						"Get qr_ticket error, errorcode:%d, errmsg:%s",
						tr.getErrcode(), tr.getErrmsg()));
			}

		} catch (Throwable t) {
			LogUtil.GlobalLog.error("Error occured when get qr_ticket:\n", t);
		}
		return tr;
	}

	private String weixinGet(String url) throws ClientProtocolException,
			IOException {
		String responseStr = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			responseStr = httpclient.execute(httpGet, responseHandler);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LogUtil.GlobalLog.warn(
						"Weixin httpGet request close httpclient error:", e);
			}
		}
		return responseStr;
	}

	private String weixinPost(String url, String postStr)
			throws ClientProtocolException, IOException {
		String responseStr = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost hpost = new HttpPost(url);
			HttpEntity entity = new StringEntity(postStr, Consts.utf8);
			hpost.setEntity(entity);
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			responseStr = httpclient.execute(hpost, responseHandler);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LogUtil.GlobalLog.warn(
						"Weixin httpPost request close httpclient error:", e);
			}
		}
		return responseStr;
	}

}
