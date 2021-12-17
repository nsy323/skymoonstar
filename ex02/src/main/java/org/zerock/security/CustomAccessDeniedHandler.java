package org.zerock.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

/**
 * 
 * AccessDeniedHadeler 인터페이스 구현하여 403에러 처리
 *
 */

@Log4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * servrlet API이용하여 에러페이지 처리
	 *
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		log.error("Acess Denied Handler");
		
		log.error("Redirect....");
		
		response.sendRedirect("/accessError");

	}

}
