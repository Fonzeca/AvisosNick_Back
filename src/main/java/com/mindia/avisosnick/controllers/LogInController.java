package com.mindia.avisosnick.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.managers.UserManager;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.security.SecurityConfig;

@RestController
public class LogInController {
	
	@Autowired
	UserManager userManager;

	@PostMapping("/login")
	public String LogIn(@RequestParam("email") String email, @RequestParam("password") String pwd) {
		User user = userManager.validateLogIn(email, pwd);
		
		String token = SecurityConfig.getJWTToken(user);
		return token;
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping("/loginWithGoogle")
	public String LogIn(@RequestParam("idToken") String idToken) {
		User user = userManager.validateLogInGoogle(idToken);
		
		String token = SecurityConfig.getJWTTokenWithOAuth(user);
		return token;
	}
	
//	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
//	@PostMapping("/register")
//	public void SignUp(@RequestBody VUser vUser) {
//		try {
//			userManager.createUser(vUser);
//			
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
//		}
//	}
}
