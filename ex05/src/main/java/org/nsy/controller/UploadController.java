package org.nsy.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.nsy.domain.AttachFileDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

/**
 * 
 * 첨부파일 업로드
 *
 */

@Controller
@Log4j
public class UploadController {

	/**
	 * <form>방식 파일업로드폼으로 이동
	 */
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form..............");
	}
	
	/**
	 * <form>방식 파일 업로드 
	 */
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		String uploadFolder = "c:\\upload"; 
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-----------------------------------");
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload File size : " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile); //upload 파일 저장
			} catch (Exception e) {
				log.error(e.getMessage());
			}//end catch
	
		}//end for
	}
	
	/**
	 * ajax방식 파일 업로드로 이동
	 */
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
		log.info("upload ajax...............");
	}
	
	/**
	 * ajax방식의 파일 업로드
	 * @param uploadFile
	 */
	@PostMapping(value="/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
	
		log.info("upload ajax post............");

		List<AttachFileDTO> list = new ArrayList<>();
		
		String uploadFolder = "c:\\upload";
		
		String uploadFolderPath = getFolder();
		
		//make folder -----
		File uploadPath = new File(uploadFolder, uploadFolderPath);	//upload Path
			
		//uploadPath가 존재하지 않으면
		if(!uploadPath.exists()) {
			
			uploadPath.mkdirs();	//폴더 생성
		
		}//make yyyy/mm/dd folder
		
		log.info("make uploadFolder..............");
		
		for(MultipartFile multipartFile : uploadFile) {
			
			AttachFileDTO attachFileDTO = new AttachFileDTO();
			
			log.info("---------------------------");
			
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload File size : " + multipartFile.getSize());
			
			//파일명
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//IE has file path 경로 포함시 경로 다음 부터 파일명 자르기
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1); 
			
			log.info("only fileName : " + uploadFileName);
			
			attachFileDTO.setFileName(uploadFileName);		//원본파일이름

			String uuid = getUUID();	//랜덤값
			
			uploadFileName = uuid + "_" + uploadFileName;
			
			attachFileDTO.setUuid(uuid);	//UUID값
			attachFileDTO.setUploadPath(uploadFolderPath); //업로드 경로
			
			File saveFile = new File(uploadPath, uploadFileName);
			
			log.info("saveFile : " + saveFile);
			
			try {
				multipartFile.transferTo(saveFile);
				
				//check image type file
				if(checkImageType(saveFile)) {
					
					attachFileDTO.setImage(true); //이미지 여부(ture)
					
					FileOutputStream thumbnail 
						= new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);	//썸네일생성
					
					thumbnail.close();
				}
				
				//add to List
				list.add(attachFileDTO);
				
			} catch (Exception e) {
				log.error(e.getMessage());
			} //end catch
			
		} //end for
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	/**
	 * upload 폴더명
	 * @return 
	 */
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		log.info("str : " + str + ".............");
		
		return str.replace("-", File.separator);
	}
	
	/**
	 * UUID 생성
	 * @return
	 */
	private String getUUID() {
		
		UUID uuid = UUID.randomUUID();
		
		return uuid.toString();
	}
	
	/**
	 * 이미지 파일 여부 체크(이미지 파일이면 thumbnail 파일 생성 하기 위해)
	 * @param file
	 * @return
	 */
	private boolean checkImageType(File file) {
		
		try {
			
			/**
			 * probeContentType이란?
			 * =>파일의 마임타입 확인하지 못하면 null 아니면 타입 반환
			 * 
			 * ex)text/plain    <---contentType을 찍어보면 나옴 
			 */
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 섬네일 데이터 전송(파일 보여주기)
	 * @param fileName
	 * @return
	 */
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		
		log.info("file Name(파일경로가 포함된 파일이름) : " + fileName);
		
		File file = new File("c:\\upload\\" + fileName);
		
		log.info("file : " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			
			HttpHeaders header = new HttpHeaders();
		
			//probeCotentType() : 파일의 MIME타입 확인
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			
			//copyToByteArray(File in) : 지정한 File 내용을 새로운 byte[]에 카피, 리턴값은 카피된 새로운 byte[]  
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header,HttpStatus.OK);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return result;
	}
	
	/**
	 * 첨부파일 다운로드
	 * @param fileName
	 * @return
	 */
	@GetMapping(value="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(
			@RequestHeader("User-Agent") String userAgent, 
			String fileName){
		
		log.info("download file : " + fileName);
		
		Resource resource =  new FileSystemResource("c:\\upload\\" + fileName);
		
		log.info("resource :" + resource);
		
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resourceName = resource.getFilename();
		
		//remove uuid
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1); 
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			String downLoadName = null;
			
			/* 브라우저 별 인코딩 처리  */
			if(userAgent.contains("Trident")) {
				
				log.info("IE browser");
				
				downLoadName =  URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			}else if(userAgent.contains("Edge")) {
				
				log.info("Edge browser");
				
				downLoadName = URLEncoder.encode(resourceOriginalName, "UTF-8");	
			}else{
				log.info("Chrome browser");
				
				downLoadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
		
			headers.add("Content-Disposition", "attachment; filename=" + downLoadName);
			
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
		
		log.info("deleteFile :" + fileName);
		
		File file;
		
		try {
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();	//파일삭제
			
			//사진파일이면
			if(type.contentEquals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_", "");		//파일이름에서 s_제거
				
				log.info("largeFileName :" + largeFileName);
				
				file = new File(largeFileName);
				
				file.delete();		//파일삭제
			}
		
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
			
		return new ResponseEntity<>("deleted", HttpStatus.OK);
	}
}
