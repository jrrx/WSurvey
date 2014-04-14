package com.voyage.test;

import java.io.IOException;
import java.net.URISyntaxException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyage.conf.MsgConfig;
import com.voyage.meta.TokenResult;
import com.voyage.meta.WxMsg;
import com.voyage.util.Consts;

public class MTest {

	public static void main(String[] args) throws Exception {

		
		MsgConfig.initMsgs();
		String welcome_msg = MsgConfig.getMsg("welcome_msg");
		System.out.println(welcome_msg);
		welcome_msg = String.format(welcome_msg, 5);
		System.out.println(welcome_msg);
		
		//test_ParseXml();

		// test_WriteXml();
		// test_WriteJson();
	}

	private static void test_ParseXml() throws DocumentException,
			JsonProcessingException {

		String postXml = "<xml>"
				+ " <ToUserName><![CDATA[toUser]]></ToUserName>"
				+ "<FromUserName><![CDATA[fromUser]]></FromUserName> "
				+ "<CreateTime>1348831860</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA[this is a test]]></Content>"
				+ "<MsgId>1234567890123456</MsgId>" + "</xml>";

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
		msg.setContent(root.elementText("content"));

		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(msg));
	}

	private static void test_WriteXml() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");
		Element ele = root.addElement("ToUserName");
		ele.addCDATA("asdad");
		ele = root.addElement("FromUserName");
		ele.addCDATA(Consts.WxName);
		System.out.println(doc.asXML());
	}

	private static void test_WriteJson() throws JsonProcessingException {
		TokenResult tr = new TokenResult();
		tr.setAccess_token("aaaaaa");
		tr.setErrcode(393);
		tr.setErrmsg("dddf");
		tr.setExpires_in(7200);
		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(tr));
	}

}
