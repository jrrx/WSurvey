package com.voyage.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * 实现servletContextListener接口
 * 
 * @author Houyangyang
 * */
public class MainContextListener implements ServletContextListener {
	private static Logger log = Logger.getLogger(MainContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		// 处理逻辑判断是否加载完成
		log.info("context init ...");
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		log.info("context destroyed ...");
	}

}
