package com.voyage.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static Utils utils = new Utils();

	public static Utils getInstance() {
		return utils;
	}

	private SimpleDateFormat defaultFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * @return 返回系统当前时间.
	 */
	public Timestamp getCurrentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return 返回当前系统时间，格式为"yyyy-MM-dd HH:mm:ss"
	 */
	public String getCurrentTimeStr() {
		return defaultFormat.format(this.getCurrentTime());
	}

	public String getTimeStr(Timestamp time) {
		if (time != null) {
			return defaultFormat.format(time);
		} else {
			return null;
		}
	}

	/**
	 * @param format
	 *            指定的时间格式
	 * @return 返回指定格式的时间字符串
	 */
	public String getCurrentTimeStr(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(this.getCurrentTime());
	}

	public static <T> T getObjectFromJson(Class<T> clazz, String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	public static String toJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

}
