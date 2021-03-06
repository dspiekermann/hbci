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

import de.rlm.hbci.GVResult;
import de.rlm.hbci.HbciApi;
import de.rlm.hbci.HbciException;
import de.rlm.hbci.UserRequest;
import de.rlm.hbci.UserRequestImpl;
import de.rlm.hbci.gv.data.User;

public class HbciAuthenticationProvider implements AuthenticationProvider {
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String userid = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		//TODO where to get the blz
		UserRequest userRequest = new UserRequestImpl("37050198", userid, password);
		HbciApi hbci = HbciApi.getInstance(userRequest);
		
		User user = null;
		
		GVResult<User> result = null;
		try {
			result = hbci.login();
			
			if (result!=null){
				if (!result.isOk()){
					throw new BadCredentialsException(result.getErrorMessage());
				}
				List<User> users = result.getResult();
				if (users!=null && users.size()>0){
					user = users.get(0);
				}
			}
			
			if (user==null){
				throw new BadCredentialsException("cannot authenticate user");
			}
		} catch (HbciException e) {
			throw new BadCredentialsException(e.getMessage());
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
