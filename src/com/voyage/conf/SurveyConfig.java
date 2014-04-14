package com.voyage.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import com.voyage.meta.VoyageException;
import com.voyage.util.FileFinder;

public class SurveyConfig {
	private static Properties props;

	/**
	 * 初始化所有的配置消息
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void initMsgs() {
		try {
			File file = FileFinder
					.findFile("resources/properties/SurveyConfig.properties");
			props = new Properties();
			props.load(new FileReader(file));
		} catch (Throwable t) {
			throw new VoyageException(t);
		}
	}

	public static String getMsg(String key) {
		if (props != null) {
			return props.getProperty(key);
		} else {
			initMsgs();
		}

		if (props != null) {
			return props.getProperty(key);
		} else {
			throw new VoyageException("SurveyConfig is not inited...");
		}

	}
}
