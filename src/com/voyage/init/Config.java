package com.voyage.init;

public class Config {
	// 生产环境设置为 true
	public static boolean productMode = true;
	

	// 是否处于微信接入模式， 用于验证接入服务器，生产环境设置为false
	public static boolean connectMode = false;

	// 默认十分钟(10*60秒)提醒一次，默认值为 600
	public static int noticeInterval = 600;

}
