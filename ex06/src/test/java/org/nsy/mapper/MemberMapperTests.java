package org.nsy.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsy.domain.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class MemberMapperTests {
	
	@Setter(onMethod_=@Autowired)
	private MemberMapper mapper;
	
	/**
	 * mapper를 이용한 조회
	 */
	@Test
	public void testRead() {
		MemberVO vo = mapper.read("admin99");
		
		log.info("vo : " + vo);
		
		vo.getAuthList().forEach(authVO -> log.info("authVO :" + authVO));
	}
}
