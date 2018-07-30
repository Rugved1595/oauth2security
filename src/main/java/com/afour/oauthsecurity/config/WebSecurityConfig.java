package com.afour.oauthsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.afour.oauthsecurity.filter.CORSFilter;

/**
 * This class extends WebSecurityConfigurerAdapter and provides usual spring
 * security configuration.
 * 
 * @author rugved.m
 *
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * In order to use the “password” grant type we need to inject and use the
	 * AuthenticationManager bean.
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class).csrf().disable().anonymous().disable()
				.authorizeRequests().antMatchers("/oauth/token").permitAll();
	}

}
