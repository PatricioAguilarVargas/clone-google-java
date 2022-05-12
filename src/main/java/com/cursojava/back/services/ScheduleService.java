package com.cursojava.back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleService {

	@Autowired
	private SpiderService spiderServices;
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void scheduleIndexWebPages() {
		spiderServices.indexWebPages();
	}
}
