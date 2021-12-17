package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Log4j
public class BoardController {
	 
	private final BoardService service;

//	@GetMapping("/list")
//	public void list(Model model) {
//		log.info("list..................................");
//		
//		model.addAttribute("list", service.getList());
//		
//	}
	
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		
		log.info("----------------------------------------");
		log.info(cri);
		log.info("list..................................");
		
		model.addAttribute("list", service.getList(cri));
		model.addAttribute("pageMaker", new PageDTO(cri, service.getTotal(cri)));
		
	}
	
	/**
	 * @PreAuthorize("isAuthenticated()") 사용으로 
	 * 로그인 사용자만 게시물 등록 가능
	 */
	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void registerGet() {
		
	}
	
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {
		
		log.info("===============================================");
		
		log.info("register : " + board);
		
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		
		log.info("===============================================");
		
		Long bno = service.register(board);
		
		log.info("BNO : " + bno);
		
		rttr.addFlashAttribute("result", bno);
		
		return "redirect:/board/list";
		
	}
	
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model ) {
		model.addAttribute("board", service.get(bno));
	}
	
	@PostMapping("/modify")
	@PreAuthorize("principal.username == #board.writer")
	public String modify(BoardVO board, Criteria cri, RedirectAttributes rttr) {
	
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/list";
	}
	
	/**
	 * 게시물 삭제
	 * @param bno
	 * @param cri
	 * @param rttr
	 * @param writer
	 * @return
	 */
	@PostMapping("/remove")
	@PreAuthorize("principal.username == #writer")
	public String remove(@RequestParam("bno") Long bno, Criteria cri,  RedirectAttributes rttr, String writer) {
		
		log.info("remove...." + bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);		//첨부파일목록 조회
		
		//첨부파일 데이터가 삭제 완료되면
		if(service.remove(bno)) {
			deleteFiles(attachList);	//첨부파일삭제
			
			rttr.addFlashAttribute("result", "success");
		}
		
		
//		rttr.addAttribute("pageNum", cri.getPageNum());
//		rttr.addAttribute("amount", cri.getAmount());
//		rttr.addAttribute("type", cri.getType());
//		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/list" + cri.getListLink();
	}
	
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0 ) return;
		
		log.info("delete attach files.................");
		log.info(attachList);
		
		attachList.forEach(attach -> {
			
			try {
				Path file = Paths.get("c:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName() );
				
				// deleteIfExists() : 파일이 존재하면 삭제하고 존재하지 않을 경우 삭제하지 않고 false 리턴 
				Files.deleteIfExists(file);
				
				//이미지일 경우 섬네일도 삭제
				if(Files.probeContentType(file).startsWith("image")){
					Path thumbNail = Paths.get("c:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					
					Files.delete(thumbNail);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
				log.error("delete file error" + e.getMessage());
			} //end of catch
			
			
			
		});	//end of forEach
		
		
		
	}
	
	/**
	 * 첨부파일 List조회
	 * @param bno
	 * @return
	 */
	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		
			log.info("getAttachList..........." + bno);
		
		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	}

	
}
