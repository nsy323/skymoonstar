package org.zerock.quartz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.mapper.ScheduleMapper;

import lombok.extern.log4j.Log4j;

@Service("scheduleService")
@Log4j
public class ScheduleServiceImpl implements ScheduleService {
	
	@Autowired
	private ScheduleMapper mapper;

	@Override
	public void testJobMethod() {
		log.info("Job Test.......................");
	}
 
	@Override
	public void testInsertJobMethod() {
		log.info("testInsertJobMethod() start..........................");
		
		int cnt = 0;
		
		List<BoardVO> list = mapper.getList(); 
		
		for(BoardVO vo : list) {
			mapper.insert(vo);
			cnt++;
			log.info("count : " + cnt + "..............................");
		}
		
		log.info("testInsertJobMethod() end..........................");
		
	}

}
