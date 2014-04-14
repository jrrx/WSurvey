package com.voyage.meta;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.voyage.util.FileFinder;

/**
 * 
 * 微信公众号每次调用接口时，可能获得正确或错误的返回码
 * 
 * @author Houyangyang
 */
public class ErrReturn {

	private static ErrReturn er = new ErrReturn();

	private Map<Integer, String> codeMsg = new HashMap<Integer, String>();

	public ErrReturn() {
		try {
			File file = FileFinder
					.findFile("resources/properties/ErrCodes.properties");
			Properties props = new Properties();
			props.load(new FileInputStream(file));
			Set<Object> keys = props.keySet();
			for (Object key : keys) {
				codeMsg.put((Integer) key, (String) props.get(key));
			}
		} catch (Throwable t) {
			throw new VoyageException(t);
		}
	}

	public static ErrReturn getInstance() {
		return er;
	}

}
