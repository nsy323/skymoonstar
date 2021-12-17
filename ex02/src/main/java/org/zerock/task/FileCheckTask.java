package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * 
 * Quartz를 이용한 잘못 업로드된 파일 삭제(파일만 저장돼고 데이터데이스에 insert 되지 않은 파일들 삭제(전날기준))
 *
 */
@Log4j
@Component
public class FileCheckTask {

	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	private String getForderYesterDay() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, - 1);
		
		//log.info("cal.getTime() : " + cal.getTime());
		
		String str = sdf.format(cal.getTime());
				
		return str.replace("-", File.separator);
	}
	
	@Scheduled(cron="0 38 13 * * *")
	public void checkFiles() throws Exception{
		log.warn("File Check Task run..............................");
		log.warn(new Date());
		
		//file list in database
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();		//upload한 파일 List 가져오기
		
		//files in yesterday directory
		File targetDir = Paths.get("c:\\upload\\" + getForderYesterDay()).toFile();		//해당날짜 폴더(전날폴더경로)
		
		log.warn("===== targetDir : " + targetDir);
		
		if(fileList != null) {		//DB에 파일이 있을 경우
		
			// ready for check file in directory with database file list
			List<Path> fileListPaths = fileList.stream()
												.map(vo -> Paths.get("c:\\upload", vo.getUploadPath() , vo.getUuid() + "_" + vo.getFileName()))		//fileList정보를 가공해서 stream에 담아 반환
												.collect(Collectors.toList());			//List로 만들기
			
			//image file has thumnail file
			fileList.stream()
					.filter(vo -> vo.isFileType() == true)	//파일타입이 이미지일 경우만 필터링
					.map(vo -> Paths.get("c:\\upload", vo.getUploadPath() ,  "s_" + vo.getUuid() + "_" + vo.getFileName()))	//stream으로 가공하여 리턴
					.forEach(p -> fileListPaths.add(p));		//fileListPaths에 추가
			
			log.warn("===================================================");
			log.warn("[fileListPaths]");
			
			fileListPaths.forEach(p -> log.warn(p));		//담긴 fileListPaths를 출력(테이블(attach)에 insert한 실제 업로드된 파일들이다.삭제하면 안돼는 파일들이다.)
			
			log.warn("===================================================");
								
			File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false); //해당폴에의 파일List에서 fileListPaths에 포함하지 않는 파일경로 File배열에 담기
			
			if(removeFiles == null || removeFiles.length == 0) return;	//삭제할 파일이 없을 경우 return;
			
			log.warn("===================================================");
			log.warn("[removeFiles]");
			
			//삭제할 파일들 삭제
			for(File file : removeFiles) {
				
				log.warn(file.getAbsolutePath());	//파일절대경로
				
				file.delete();	//파일삭제
			}
			log.warn("===================================================");
			
		}else {	//DB에 파일이 없을 해당 폴더에 있는 모든 파일 삭제
			
			targetDir.delete();			
		}
	}
}
