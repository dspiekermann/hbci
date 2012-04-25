package de.rlm.bp.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class HbciAuthenticationProvider implements AuthenticationProvider {
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String userid = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		if (!"test".equals(password)){
			throw new BadCredentialsException("wrong password");
		}
		
		GrantedAuthority role = new GrantedAuthorityImpl("ROLE_USER");
		
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(role);
		
		Principal p = new Principal() {
			@Override
			public String getName() {
				return userid;
			}
		};
		
		return new UsernamePasswordAuthenticationToken(p, password, roles);
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return true;
	}

}
