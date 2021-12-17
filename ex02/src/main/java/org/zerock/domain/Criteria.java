package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {

	private int pageNum;
	private int amount;
	
	private String type;		//t, tc, tcw, cw
	private String keyword;
	
	public Criteria() {
		this(1,10);
	}
	
	public Criteria(int pageNum, int amount) {
		super();
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public String[] getTypeArr() {
		System.out.println("getTypeArr......................");
		return type == null? new String[] {} : type.split("");
	}
	
	/**
	 * 파라미터 한번에 보내기 
	 * 
	 * rttr.addAttribute("pageNum", cri.getPageNum());
	 * rttr.addAttribute("amount", cri.getAmount());
	 * rttr.addAttribute("type", cri.getType());
	 * rttr.addAttribute("keyword", cri.getKeyword());
	 * 
	 * @return
	 */
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
	}
}
