package org.zerock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/")
@RestController
@Log4j
@AllArgsConstructor
public class ReplyController {
	
	private ReplyService service;
	
	/**
	 * 댓글 등록
	 * @param vo
	 * @return
	 */
	@PostMapping(
			value="/new",
			consumes="application/json",
			produces= {MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		
		log.info("ReplyVO : " + vo);
		
		int insertCount = service.register(vo);
		
		log.info("Reply Insert count : " + insertCount);
		
		return (insertCount == 1)? 
				new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 댓글 list 조회	
	 * @param bno
	 * @param page
	 * @return
	 */
	@GetMapping(
			value="/pages/{bno}/{page}",
			produces= { MediaType.APPLICATION_XML_VALUE,
						MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(
			@PathVariable("bno") Long bno,
			@PathVariable("page") int page){
		
		log.info("getList.....................");
		
		Criteria cri = new Criteria(page, 10);
		
		log.info("cri :" + cri);
		
		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}
	
	/**
	 * 댓글 조회
	 * @param rno
	 * @return
	 */
	@GetMapping(value= "/{rno}",
			produces= {MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
		
		log.info("get.............. : " + rno);
		
		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}
	
	
	/**
	 * 댓글 삭제
	 * @param rno
	 * @return
	 */
	@DeleteMapping(value="/{rno}")
	@PreAuthorize("principal.username == #vo.replyer")
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
		log.info("remove ......... : " + rno);
		
		log.info("replyer : " + vo.getReply());
		
		return service.remove(rno) == 1 ?
				new ResponseEntity<> ("success" , HttpStatus.OK) :
					new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 댓글 수정
	 * @param rno
	 * @param vo
	 * @return
	 */
	@RequestMapping(
			method = {RequestMethod.PUT, RequestMethod.PATCH},
			value="/{rno}",
			consumes = "application/json")
	@PreAuthorize("principal.username == #vo.replyer")
	public ResponseEntity<String> modify(
			@PathVariable("rno") Long rno,
			@RequestBody ReplyVO vo){
		
		log.info("modify...........: " + rno);
		
		vo.setRno(rno);
		
		log.info("modify..........vo: " + vo);
		
		return service.modify(vo) == 1 ?
				new ResponseEntity<> ("success" , HttpStatus.OK) :
					new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	
}
