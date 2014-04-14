package com.voyage.util;

public class StrUtil {

	public static StrUtil it() {
		return new StrUtil();
	}

	public boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

}
