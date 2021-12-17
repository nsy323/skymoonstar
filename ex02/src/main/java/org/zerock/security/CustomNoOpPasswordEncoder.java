package org.zerock.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j;

/**
 * 
 * 비밀번호 encoding 지정(spring security 5부터 필수로 지정 해야함 )
 *
 */

@Log4j
public class CustomNoOpPasswordEncoder implements PasswordEncoder{@Override
	public String encode(CharSequence rawPassword) {
		
		log.warn("before encode : " + rawPassword);
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		
		log.warn("matchs : " + rawPassword + " : " + encodedPassword);
		
		return rawPassword.toString().equals(encodedPassword);
	}
}
