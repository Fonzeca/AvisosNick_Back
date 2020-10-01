package com.mindia.avisosnick.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.managers.UserManager;
import com.mindia.avisosnick.view.VUser;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserManager userManager;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create")
	public void createUser(@RequestBody VUser user) {
		userManager.createUser(user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/modify")
	public void modifyUser(@RequestBody VUser user) {
		userManager.modifyUser(user);
	}
	
	@PreAuthorize("#authentication.principal == #user.email")
	@PostMapping("/modifyMyUser")
	public void modifyMyUser(@RequestBody VUser user, Authentication authentication) {
		userManager.modifyMyUser(user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/desactivate")
	public void desactivateUser(String email) {
		//TODO: realizar las modificaciones para esta accion
	}
	
}
