package com.mindia.avisosnick.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.managers.UserManager;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.security.SecurityConfig;
import com.mindia.avisosnick.view.PojoLogIn;

@RestController
public class LogInController {

	@Autowired
	UserManager userManager;

	@PostMapping("/login")
	public PojoLogIn LogIn(@RequestParam("email") String email, @RequestParam("password") String pwd) {
		User user = userManager.validateLogIn(email, pwd);

		String token = SecurityConfig.getJWTToken(user);

		PojoLogIn pojo = new PojoLogIn();
		//pojo.setFullName(user.getFullName());
		pojo.setMail(user.getEmail());
		pojo.setRoles(user.getRoles());
		pojo.setUserType(user.getUserType());
		pojo.setToken(token);
		
		return pojo;
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/loginWithGoogle")
	public PojoLogIn LogInGoogle(@RequestParam("idToken") String idToken) {
		User user = userManager.validateLogInGoogle(idToken);

		String token = SecurityConfig.getJWTTokenWithOAuth(user);
		
		PojoLogIn pojo = new PojoLogIn();
		//pojo.setFullName(user.getFullName());
		pojo.setMail(user.getEmail());
		pojo.setRoles(user.getRoles());
		pojo.setUserType(user.getUserType());
		pojo.setToken(token);
		
		return pojo;
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping("/loginWithFacebook")
	public PojoLogIn LogInFacebook(@RequestParam("idToken") String accessToken) {
		User user = userManager.validateLogInFacebook(accessToken);

		String token = SecurityConfig.getJWTTokenWithOAuth(user);
		
		PojoLogIn pojo = new PojoLogIn();
		//pojo.setFullName(user.getFullName());
		pojo.setMail(user.getEmail());
		pojo.setRoles(user.getRoles());
		pojo.setUserType(user.getUserType());
		pojo.setToken(token);
		
		return pojo;
	}

	@PreAuthorize("permitAll()")
	@GetMapping("/validateToken")
	public boolean ValidateToken(Authentication auth) {
		return auth.isAuthenticated();
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
