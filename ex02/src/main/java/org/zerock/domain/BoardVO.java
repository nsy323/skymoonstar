package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * 게시판
 *
 */
@Data
public class BoardVO {
	private Long bno;
	private String title, content, writer;
	private Date regDate, updateDate;
	private int replyCnt;
	
	List<BoardAttachVO> attachList;	//첨부파일 목록
}
