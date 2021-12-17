package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
//@RequiredArgsConstructor
//@ToString
public class BoardServiceImpl implements BoardService{

	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;

	/**
	 * 게시글 등록
	 */
	@Transactional
	@Override
	public Long register(BoardVO vo) {
		log.info("register.........." + vo);
		
		mapper.insertSelectKey(vo);	//게시글 등록
		
		//첨부파일이 없을 경우 패스
		if(vo.getAttachList() == null || vo.getAttachList().size() == 0) {
			return vo.getBno();
		}
		
		//첨부파일 등록
		vo.getAttachList().forEach(attach -> {
			attach.setBno(vo.getBno());
			attachMapper.insert(attach);  
		});
		
		return vo.getBno();
	}

	@Override
	public BoardVO get(Long bno) {
		return mapper.read(bno);
	}

	/**
	 * 게시물 수정
	 * 첨부파일을 경우 모두 삭제 후 재등록
	 */
	@Transactional
	@Override
	public boolean modify(BoardVO vo) {
		log.info("modify........" + vo);
		
		attachMapper.deleteAll(vo.getBno());	//파일모두 삭제
		
		boolean modifyResult = mapper.update(vo) == 1;	//게시물 수정 완료시 1이면 true
		
		//파일 List 재등록
		if(modifyResult && vo.getAttachList() != null || vo.getAttachList().size() > 0) {
			
			vo.getAttachList().forEach(attach -> {
				attach.setBno(vo.getBno());
				
				attachMapper.insert(attach);
			});
		}
			
		return modifyResult;
	}

	/**
	 * 게시물 삭제
	 */
	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("remove......." + bno);
		
		attachMapper.deleteAll(bno);	//첨부파일 삭제
		
		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getList() {
		
		return mapper.getList();
	}
	
	@Override
	public List<BoardVO> getList(Criteria cri) {
		
		return mapper.getListWithPagging(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		return mapper.getTotalCount(cri);
	}
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("get Attach list by bno" + bno);
		
		return attachMapper.findByBno(bno);
	}
	
	
}
