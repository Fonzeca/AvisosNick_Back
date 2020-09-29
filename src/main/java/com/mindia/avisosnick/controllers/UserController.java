package com.mindia.avisosnick.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@PostMapping("/users/create")
	public void createUser(String email, String password) {
		
	}
	
}
