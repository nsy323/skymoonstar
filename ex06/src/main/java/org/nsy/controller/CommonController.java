package org.nsy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j;

/**
 * 
 * 접근제한 메세지 처리
 *
 */

@Controller
@Log4j
public class CommonController {

	
	/**
	 * 특정한 URI를 지정한 접근제한페이지 이동 
	 * @param auth
	 * @param model
	 */
	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) {
		log.info("access Denied :  " + auth);
		
		model.addAttribute("msg","Access Denied");
		
	}
	
	/**
	 * 커스텀 로그인 페이지
	 * @param error
	 * @param logout
	 * @param model
	 */
	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) {
		
		log.info("error : " + error);
		log.info("logout: " + logout);
		
		if(error != null) {
			model.addAttribute("error", "Login Error Check Your Account");
		}
		
		if(logout != null) {
			model.addAttribute("logout","Logout!!");
		}
	}
	
	/**
	 * 로그아웃 화면으로 이동
	 */
	@GetMapping("/customLogout")
	public void logoutGET() {
		log.info("custom logout");
	}
	
	/**
	 * 로그아웃 처리
	 */
	@PostMapping("/customLogout")
	public void logoutPost() {
		log.info("post custom logout");
	}
	
	
	
}
