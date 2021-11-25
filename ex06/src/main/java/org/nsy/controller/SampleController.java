package org.nsy.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

/**
 * 
 * spring security(시큐리티가 필요한 uri 설계)
 *
 */

@RequestMapping("/sample/*")
@Controller
@Log4j
public class SampleController {
	
	/**
	 * 로그인 하지 않은 사용자도 접근 가능한 uri
	 */
	@GetMapping("/all")
	public void doAll() {
		log.info("do all can access everybody");
	}
	
	/**
	 * 로그인 한 사용자들만 접근할 수 있는 uri
	 */
	@GetMapping("/member")
	public void doMember() {
		log.info("logined member");
	}
	
	/**
	 * 로그인 한 사용자들 중에서 관리자 권한을 가진 사용자만이 접근할 수 있는 uri
	 */
	@GetMapping("/admin")
	public void doAdmin() {
		log.info("admin only");
	}
	
	/**
	 *  어노테이션을 이용한 스프링 시큐리티 설정
	 *  @PreAuthorize() 사용 
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@GetMapping("/annoMember")
	public void doMember2() {
		log.info("logined annotation member");
	}
	
	/**
	 * 어노테이션을 이용한 스프링 시큐리티 설정
	 * @Secured() 사용
	 */
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/annoAdmin")
	public void doAdmin2() {
		log.info("admin annotation only");
	}

}
