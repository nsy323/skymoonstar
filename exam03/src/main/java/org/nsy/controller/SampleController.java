package org.nsy.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.nsy.domain.SampleVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/sample")
@Log4j
public class SampleController {
	
	/**
	 * 텍스트 전송
	 * @return
	 */
	@GetMapping(value="/getText", produces ="text/plain; charset=UTF-8")
	public String getText() {
		log.info("MIME TYPE : " + MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";
	}
	
	/**
	 * xml과 json형태로 전송/ getSample.json으로 url 보내면 json형식으로 감
	 * @return
	 */
	@GetMapping(value="/getSample", produces= { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE })
	public SampleVO getSample() {
		return new SampleVO(100, "홍", "길동");
	}
	
	/**
	 * produces를 작성하지 않아도 xml과 json으로 전송됨.
	 * @return
	 */
	@GetMapping(value="getSample2")
	public SampleVO getSample2() {
		return new SampleVO(101, "고", "길동");
	}
	
	/**
	 * List 형태 전송
	 * @return
	 */
	@GetMapping(value="/getList")
	public List <SampleVO> getList(){
		
		return IntStream.range(1, 10)
						.mapToObj(i -> new SampleVO(i, i + "First", i + "Last"))
						.collect(Collectors.toList());
	}
	
	
}
