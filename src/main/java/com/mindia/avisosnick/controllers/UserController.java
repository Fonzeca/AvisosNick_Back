package com.mindia.avisosnick.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.view.VUser;

@RestController
public class UserController {
	
	@PostMapping("/users/create")
	public void createUser(VUser user) {
		
	}
	
	@PostMapping("/users/modify")
	public void modifyUser(VUser user) {
		
	}
	
	@PostMapping("/users/desactivate")
	public void createUser(String email) {
		
	}
	
}
