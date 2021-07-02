package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Service
@RequiredArgsConstructor
@ToString
public class BoardServiceImpl implements BoardService{

	private final BoardMapper mapper;

	@Override
	public Long register(BoardVO vo) {
		mapper.insertSelectKey(vo);
		return vo.getBno();
	}

	@Override
	public BoardVO get(Long bno) {
		return mapper.read(bno);
	}

	@Override
	public int modify(BoardVO vo) {
		
		return mapper.update(vo);
	}

	@Override
	public int remove(Long bno) {
		
		return mapper.delete(bno);
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
	
	
}
