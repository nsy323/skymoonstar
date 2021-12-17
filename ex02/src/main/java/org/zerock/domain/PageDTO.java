package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PageDTO {

	private int startPage, endPage;
	private boolean prev, next;
	
	private int total;
	private Criteria cri;
	
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
										//0.3 -> 1 -> 10
		this.endPage = (int) (Math.ceil(cri.getPageNum()/10.0)) * 10;
		
		this.startPage = endPage - 9;
		
		this.prev = this.startPage > 1;
									//71.0/10 => 7.1 => 8
		int realEnd = (int)(Math.ceil(total * 1.0) / cri.getAmount());
		
						// 8
		this.endPage = (realEnd <= endPage)? realEnd : endPage;
						
		this.next = this.endPage < realEnd;
	}
	
}
