package com.voyage.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

	
	//@Scheduled(cron = "0/5 * * * * *")
	public void accessTokenJob() {
		System.out.println("dd");
	}
}
