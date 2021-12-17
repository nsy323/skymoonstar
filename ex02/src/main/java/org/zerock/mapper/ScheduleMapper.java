package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardVO;

public interface ScheduleMapper {
	/**
	 * 게시판 list 조회하기 100개만
	 * @return
	 */
	List<BoardVO> getList();
	
	/**
	 * 게시판 목록 temp테이블에 insert
	 */
	void insert(BoardVO vo);
	
}
