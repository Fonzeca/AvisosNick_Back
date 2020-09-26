package com.mindia.avisosnick.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.managers.UserManager;
import com.mindia.avisosnick.security.SecurityConfig;
import com.mindia.avisosnick.view.VUser;

@RestController
public class LogInController {
	
	@Autowired
	UserManager userManager;

	@PostMapping("/login")
	public String LogIn(@RequestParam("email") String email, @RequestParam("password") String pwd) {
		String token = SecurityConfig.getJWTToken(email);
		return token;
	}
	
	@PostMapping("/register")
	public void SignUp(@RequestBody VUser vUser) {
		try {
			userManager.createUser(vUser);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}
}
