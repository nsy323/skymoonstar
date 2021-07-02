package org.zerock.mapper;

import java.util.List;
import java.util.Map;

import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	List<BoardVO> getList();
	
	void insert(BoardVO vo);
	
	void insertSelectKey(BoardVO vo);
	
	BoardVO read(Long bno);
	
	int delete(Long bno);
	
	int update(BoardVO vo);
	
	List<BoardVO> getListWithPagging(Criteria cri);
	
	int getTotalCount(Criteria cri);
	
	List<BoardVO> searchTest(Map<String, Map<String,String>> map);
	
}
