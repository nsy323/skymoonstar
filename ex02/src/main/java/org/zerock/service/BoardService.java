package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardService {

	/**
	 * 등록
	 * @param vo
	 * @return
	 */
	Long register(BoardVO vo);
	
	/**
	 * 조회
	 * @param bno
	 * @return
	 */
	BoardVO get(Long bno);
	
	/**
	 * 수정
	 * @param vo
	 * @return
	 */
	boolean modify(BoardVO vo);
	
	/**
	 * 삭제
	 * @param bno
	 * @return
	 */
	boolean remove(Long bno);
	
	/**
	 * 게시물 List
	 * @return
	 */
	List<BoardVO> getList();
	
	/**
	 * 게시물List(페이지정보 포함)
	 * @param cri
	 * @return
	 */
	List<BoardVO> getList(Criteria cri);
	
	/**
	 * 전체 건수
	 * @param cri
	 * @return
	 */
	int getTotal(Criteria cri);
	
	/**
	 * 첨부파일 조회
	 * @param bno
	 * @return
	 */
	public List<BoardAttachVO> getAttachList(Long bno);
}
