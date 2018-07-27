package com.afour.oauthsecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.afour.oauthsecurity.filter.CORSFilter;

/**
 * This class is responsible for managing access tokens. Provides configuration
 * for Authorization.In other words, it is responsible for generating tokens
 * specific to a client.
 * 
 * @author rugved.m
 *
 */

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	private static String REALM = "MY_OAUTH_REALM";

	/**
	 * A bean for AuthenticationManager is created in WebSecurityConfig class and is
	 * injected here.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * MySQL DataSource is configured for the application.
	 */
	@Autowired
	private DataSource dataSource;

	/**
	 * A PasswordEncoder needs to be explicitly created and injected in
	 * configuration for the newer version of spring application.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * In order to persist tokens, we used JdbcTokenStore.
	 * 
	 * @return JdbcTokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()");
		security.addTokenEndpointAuthenticationFilter(new CORSFilter());
		security.realm(REALM + "/client");

	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("trusted-client").secret(passwordEncoder.encode("secret"))
				.accessTokenValiditySeconds(3600).scopes("read", "write", "trust")
				.authorizedGrantTypes("client_credentials", "password", "refresh_token")
				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
	}

}
