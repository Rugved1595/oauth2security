package com.afour.oauthsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.afour.oauthsecurity.filter.CORSFilter;

/**
 * Resource in our context is the REST API which we have exposed for the CRUD
 * operation.To access these resources, client must be authenticated.
 * 
 * @author rugved.m
 *
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
		.httpBasic()
		.and().authorizeRequests().antMatchers("/req/**").permitAll()
		.and().authorizeRequests().antMatchers("/users/**").permitAll()
		.and().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
		.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}
