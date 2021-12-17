package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardAttachVO;

/**
 * 
 * 첨부파일 등록
 *
 */
public interface BoardAttachMapper {

	/**
	 * 첨부파일 등록
	 * @param vo
	 */
	public void insert(BoardAttachVO vo);
	
	/**
	 * 첨부파일 삭제
	 * @param uuid
	 */
	
	public void delete(String uuid);
	
	
	/**
	 * 첨부파일 목록 조회
	 * @param bno
	 * @return
	 */
	public List<BoardAttachVO> findByBno(Long bno);
	
	/**
	 * 첨부파일 전체삭제
	 */
	public void deleteAll(Long bno);
	
	/**
	 * 어제 등록된 모든 파일 List조회
	 * @return
	 */
	public List<BoardAttachVO> getOldFiles();
}
