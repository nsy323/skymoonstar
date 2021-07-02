package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardService {

	Long register(BoardVO vo);
	
	BoardVO get(Long bno);
	
	int modify(BoardVO vo);
	
	int remove(Long bno);
	
	List<BoardVO> getList();
	
	List<BoardVO> getList(Criteria cri);
	
	int getTotal(Criteria cri);
}
