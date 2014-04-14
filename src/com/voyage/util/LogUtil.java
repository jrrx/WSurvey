package com.voyage.util;

import org.apache.log4j.Logger;

public class LogUtil {
	public static Logger GlobalLog = Logger.getLogger("GlobalLogger");
	
	public static Logger DevLog =  Logger.getLogger("DevLogger");
	
	
	
	
	static class LogWrapr extends Logger{
		
		protected LogWrapr(String name) {
			super(name);
		}
		
		
	}
	
	
}
