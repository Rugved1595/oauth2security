/**
 * 
 */
package com.afour.oauthsecurity.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afour.oauthsecurity.config.CustomPasswordEncoder;
import com.afour.oauthsecurity.model.Role;
import com.afour.oauthsecurity.model.User;
import com.afour.oauthsecurity.repository.RoleRepository;
import com.afour.oauthsecurity.repository.UserRepository;

/**
 * @author rugved.m
 *
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomPasswordEncoder passwordEncoder;
	
	public Boolean registerNewUser(User user) {
		User newUser = new User();
		Role userRole = roleRepository.findAllById((long) 2);
		List<Role> roles = new ArrayList<Role>();
		roles.add(userRole);
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setUsername(user.getUsername());
		newUser.setRoles(roles);
		
		userRepository.save(newUser);
		return true;
	}
}
