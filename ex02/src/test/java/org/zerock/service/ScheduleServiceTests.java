package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.quartz.service.ScheduleService;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ScheduleServiceTests {

	@Autowired
	private ScheduleService service;
	
	@Test
	public void jobTest() {
		service.testJobMethod();
	}
	
	@Test
	public void testInsertJobMethod() {
		log.info("testInsertJobMethod..................");
		service.testInsertJobMethod();
	}
	
	
	
	
}
