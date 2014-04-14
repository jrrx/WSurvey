package com.voyage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class FileFinder {

	public static File findFile(String path) throws URISyntaxException {
		return findFile(path, FileFinder.class, "UTF-8");
	}

	public static File findFile(String path, Class<?> klass, String enc)
			throws URISyntaxException {
		File f = new File(path);
		if (f.exists())
			return f;
		URL url = null;
		if (null != klass)
			url = klass.getClassLoader().getResource(path);
		f = getURLFile(url);
		if (f != null && f.exists())
			return f;
		url = ClassLoader.getSystemResource(path);
		return getURLFile(url);
	}

	private static File getURLFile(URL url) throws URISyntaxException {
		if (url != null) {
			File f = new File(url.toURI());
			if (f.exists())
				return f;
		}
		return null;
	}

	public static InputStream findFileAsStream(String path, Class<?> klass,
			String enc) {
		File f = new File(path);
		if (f.exists())
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				return null;
			}
		if (null != klass) {
			InputStream ins = klass.getClassLoader().getResourceAsStream(path);
			if (null == ins)
				ins = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(path);
			if (null != ins)
				return ins;
		}
		return ClassLoader.getSystemResourceAsStream(path);
	}
}
