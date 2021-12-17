package org.nsy.domain;

import lombok.Data;

/**
 * 
 * 파일 업로드 정보 DTO
 *
 */
@Data
public class AttachFileDTO {

	private String fileName;		//파일이름
	private String uploadPath;		//업로드경로
	private String uuid;			//uuid값
	private boolean image;			//이미지여부
}
