package com.afour.oauthsecurity.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.afour.oauthsecurity.model.Role;
import com.afour.oauthsecurity.model.User;

public class CustomUserDetails implements UserDetails{
	
	
		private static final long serialVersionUID = 1L;
		private String username;
	    private String password;
	    
	    Collection<? extends GrantedAuthority> authorities;
		
	    public CustomUserDetails(User byUsername){
	    	this.username = byUsername.getUsername();
//	    	this.username = byUsername.getEmail();
	    	this.password = byUsername.getPassword();
	    	
	    	List<GrantedAuthority> auths = new ArrayList<>();
	    	
	    	for(Role role : byUsername.getRoles()){
	    		auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
	    	}
	    	
	    	this.authorities = auths;
	    }
	    
	    
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}


}
