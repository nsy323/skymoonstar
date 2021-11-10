package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {
	
	private Long[] bnoArr = {10900L, 10901L, 10902L, 10903L, 10904L};
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	/**
	 * mapper 생성 잘 됐는지 테스트
	 */
	@Test
	public void testMapper() {
		log.info(mapper);
	}
	
	/**
	 * 댓글 생성 테스트
	 */
	@Test
	public void testCreate() {
		IntStream.rangeClosed(1, 10).forEach(i -> {
			 ReplyVO vo = new ReplyVO();
			 
			 vo.setBno(bnoArr[i%5]);
			 vo.setReply("댓글테스트 " + i);
			 vo.setReplyer("replayer " + i);
			 
			 mapper.insert(vo);
		});
	}
	
	/**
	 * 댓글 조회 테스트
	 */
	@Test
	public void testRead() {
		Long targetRno = 5L;
		
		ReplyVO vo = mapper.read(targetRno);
		
		log.info(vo);
	}
	
	/**
	 * 댓글 삭제 테스트
	 */
	@Test
	public void testDelete() {
		Long targetRno = 10L;
		
		int count = mapper.delete(targetRno);
		
		log.info("testDelete : " + count);
		
		
	}
	
	/**
	 * 댓글 수정 테스트
	 */
	@Test
	public void testUpdate() {
		Long targetBno = 3L;
		
		ReplyVO vo = mapper.read(targetBno);
		
		vo.setReply("Update Reply");
		
		int count = mapper.update(vo);
		
		log.info("Update count : " + count);
	}
	
	@Test
	public void testList() {
		Criteria cri = new Criteria();
		
		List<ReplyVO> list = mapper.getListWithPaging(cri, bnoArr[0]);
		
		list.forEach(reply -> log.info(reply));
	}
	
	//댓글조회 테스트(paging)
	@Test
	public void testList2() {
		Criteria cri = new Criteria(1, 10);
			
		List<ReplyVO> replies = mapper.getListWithPaging(cri, 10901L);
		
		replies.forEach(reply -> log.info(reply));
	
	}
	
	

}
