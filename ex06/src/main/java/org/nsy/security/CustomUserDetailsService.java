package org.nsy.security;

import org.nsy.domain.CustomUser;
import org.nsy.domain.MemberVO;
import org.nsy.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * 
 * UserDetailsService 인터페이스를 이용한 커스텀 security(기존쿼리 사용)
 *
 */
@Log4j
public class CustomUserDetailsService implements UserDetailsService{

	@Setter(onMethod_= {@Autowired})
	private MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.warn("Load User By UserName : " + username);
		
		//username means userid
		MemberVO vo = memberMapper.read(username);
		
		log.warn("queried by member mapper : " + vo);
		
		return vo == null? null : new CustomUser(vo);
	}

}
