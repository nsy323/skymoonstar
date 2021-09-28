package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {
	
	/**
	 * 댓글생성
	 * @param vo
	 * @return
	 */
	public int insert(ReplyVO vo);
	
	/**
	 * 댓글 조회
	 * @param rno
	 * @return
	 */
	public ReplyVO read(Long rno);
	
	/**
	 * 댓글 삭제
	 * @param rno
	 * @return
	 */
	public int delete(Long rno);
	
	/**
	 * 댓글 수정
	 * @param rno
	 */
	public int update(ReplyVO vo);
	
	/**
	 *  댓글 목록조회 
	 * @param cri 페이징처리
	 * @param bno 게시판번호
	 * @return
	 */
	public List<ReplyVO> getListWithPaging(
			@Param("cri") Criteria cri,
			@Param("bno") Long bno);
	
}
