package org.zerock.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTests {
	@Autowired
	private BoardMapper boardMapper;
	
	@Test
	public void testGetList() {
		log.info("-----------------------------------");
		boardMapper.getList();
	}
	
	@Test
	public void testsInsert() {
		
		BoardVO vo = new BoardVO();
		
		vo.setTitle("tests 테스트");
		vo.setContent("Content 테스트");
		vo.setWriter("tester");
		
		boardMapper.insert(vo);
		
		log.info("-----------------------------------");
		log.info("after insert : " + vo.getBno());
	}
	
	@Test
	public void testInsertSelectKey() {
		BoardVO vo = new BoardVO();
		
		vo.setTitle("AAA tests 테스트");
		vo.setContent("AAA Content 테스트");
		vo.setWriter("AAA tester");
		
		boardMapper.insertSelectKey(vo);
		
		log.info("-----------------------------------");
		log.info("after insert : " + vo.getBno());
	}
	
	@Test
	public void testRead() {
		BoardVO vo = boardMapper.read(9L);
		
		log.info(vo);
	}
	
	@Test
	public void testDelete() {
		int count = boardMapper.delete(1L);
		
		log.info("count  : " + count);
	}
	
	@Test
	public void testUpdate() {
		BoardVO vo = new BoardVO();
		
		vo.setBno(9L);
		vo.setTitle("update Title");
		vo.setContent("updateContent");
		vo.setWriter("user00");
		
		log.info("count : " + boardMapper.update(vo));
		
	}
	
	@Test 
	public void testPaging() {
		Criteria cri = new Criteria();
		
		List<BoardVO> list = boardMapper.getListWithPagging(cri);
		
		list.forEach(b -> log.info(b));
	}
	
	
	@Test 
	public void testPageDTO() {
		Criteria cri = new Criteria();
		
		cri.setPageNum(11);
				
		PageDTO pageDTO = new PageDTO(cri, 250);
		
		log.info(pageDTO);
	}
	
	@Test
	public void testSeach() {
		Map<String, String> map = new HashMap<>();
		
		map.put("T", "제목");
		map.put("C", "CCC");
		map.put("W", "WWW");
		
		Map<String, Map<String,String>> outer = new HashMap<>();
		outer.put("map", map);
		
		List<BoardVO> list = boardMapper.searchTest(outer);
		
		log.info(list);
	}
	
	@Test 
	public void testSearchPaging() {
		Criteria cri = new Criteria();
		
		cri.setType("TCW");
		cri.setKeyword("제목");
		
		List<BoardVO> list = boardMapper.getListWithPagging(cri);
		
		list.forEach(b -> log.info(b));
	}
	
	
}
