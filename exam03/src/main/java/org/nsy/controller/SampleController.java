package org.nsy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.nsy.domain.SampleVO;
import org.nsy.domain.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	@GetMapping(value="/getSample2")
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
	
	/**
	 * Map 형태로 전송
	 * @return
	 */
	@GetMapping(value="/getMap")
	public Map<String, SampleVO> getMap(){
		
		Map<String, SampleVO> map = new HashMap<>();
		map.put("First", new SampleVO(111, "그루트", "주니어1"));
		map.put("Second", new SampleVO(222, "엔트리", "주니어2"));
		
		return map;
	}
	
	/**
	 * ★ ResponseEntity : 데이터와 함께 헤더의 상태메시지 등을 같이 전달 하는 용도로 사용
	 *  
	 * @param height
	 * @param weight
	 * @return
	 */
	@GetMapping(value="/check", params = { "height", "weight" })
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo = new SampleVO(0, "" + height, "" + weight);
		
		ResponseEntity<SampleVO> result = null;
		
		if(height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		} else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		
		return result;
		
	}
	
	/**
	 * @PathVariable 사용하기, 파라미터로 경로를 보냄
	 * 
	 * @param cat
	 * @param pid
	 * @return
	 */
	@GetMapping(value = "/product/{cat}/{pid}")
	public String[] getPath(
			@PathVariable("cat") String cat,
			@PathVariable("pid") int pid) {
		return new String[] {"category : " + cat, "productid : " + pid};
		
	}
	
	/**
	 * @RequestBody 예제
	 * 에러남. post방식 처리 배우고 다시 함 
	 * @param ticket
	 * @return
	 */
	@PostMapping(value="/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		log.info("convert.............ticket" + ticket);
		
		return ticket;
	}
	
	
	
	
	
}
