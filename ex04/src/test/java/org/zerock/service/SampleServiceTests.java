package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SampleServiceTests {
	
	@Autowired
	private SampleService sampleService;
		
	/**
	 * Before 테스트 
	 * @throws Exception
	 */
	@Test
	public void doAddTest() throws Exception {
		log.info("doAddTest() : " + sampleService.doAdd("2", "3"));
	}
	
	/**
	 * 예외 발생시켜 AfterThrowing 테스트
	 * @throws Exception
	 */
	@Test
	public void testAddError() throws Exception{
		
		log.info(sampleService.doAdd("222", "AAA"));
		
	}
	
}
