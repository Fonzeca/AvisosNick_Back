package com.mindia.avisosnick.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.security.SecurityConfig;

@RestController
public class LogInController {

	@GetMapping("/holis")
	public String Prueba() {
		return "Heello";
	}

	@PostMapping("/login")
	public String LogIn(@RequestParam("email") String email, @RequestParam("password") String pwd) {
		String token = SecurityConfig.getJWTToken(email);
		return token;
	}
	
	@PostMapping("/register")
	public void SignUp(@RequestParam("email") String email, @RequestParam("password") String password) {
		
		
	}
}
