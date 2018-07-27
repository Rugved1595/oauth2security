package com.afour.oauthsecurity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.afour.oauthsecurity.config.CustomUserDetails;
import com.afour.oauthsecurity.model.Role;
import com.afour.oauthsecurity.model.User;
import com.afour.oauthsecurity.repository.UserRepository;

/**
 * Starting point of the application.
 * @author rugved.m
 * @version 1.0
 */


@SpringBootApplication(scanBasePackages = {"com.afour.oauthsecurity"})
public class OAuthSecurityApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(OAuthSecurityApplication.class, args);
	}
	
	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo) throws Exception {
		// System.out.println(repo.count());
		if (repo.count() == 0) {
			Collection<Role> roles = new ArrayList<Role>();
			roles.add(new Role("ADMIN"));
			repo.save(new User(1L, "root", passwordEncoder.encode("root"), "root@afourtech.com", roles));
		}

		builder.userDetailsService(s -> new CustomUserDetails(repo.findByUsername(s)));
//		builder.userDetailsService(s -> new CustomUserDetails(repo.findByEmail(s)));
	}
}
