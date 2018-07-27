package com.afour.oauthsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller to check if REST API's are working fine.
 * @author rugved.m
 *
 */

@RestController
@RequestMapping(path = "/req")
public class HelloController {

	@GetMapping("hello")
	public String helloWorld(Authentication authentication) {
		return "Hello";
	}
}
