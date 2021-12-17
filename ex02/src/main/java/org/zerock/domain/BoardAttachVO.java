package org.zerock.domain;

import lombok.Data;

/**
 * 
 * 첨부파일 정보
 *
 */

@Data
public class BoardAttachVO {

	private String uuid;			//uuid
	private String uploadPath;		//실제 파일이 업로드된 경로
	private String fileName;		//파일이름
	private boolean fileType;		//이미지파일여부

	private Long bno;				// 해당 게시물 번호
}
