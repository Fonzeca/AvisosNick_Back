package com.mindia.avisosnick.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.managers.UserManager;
import com.mindia.avisosnick.util.Constants;
import com.mindia.avisosnick.view.PojoDoubleString;
import com.mindia.avisosnick.view.PojoUser;
import com.mindia.avisosnick.view.VUser;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserManager userManager;

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/create")
	public void createUser(@RequestBody VUser user) {
		userManager.createUser(user);
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/modify")
	public void modifyUser(@RequestBody VUser user) {
		userManager.modifyUser(user);
	}

	@PreAuthorize("#authentication.principal == #user.email")
	@PostMapping("/modifyMyUser")
	public void modifyMyUser(@RequestBody VUser user, Authentication authentication) {
		userManager.modifyMyUser(user);
	}

	@PreAuthorize(Constants.IS_ADMIN)
	@PostMapping("/setActive")
	public void desactivateUser(@RequestParam("email") String email, @RequestParam("active") boolean active) {
		userManager.setActiveUser(email, active);
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') OR hasRole('" + Constants.ROLE_USER + "')")
	@PostMapping("/setToken")
	public void setTokenToUser(@RequestParam String token, Authentication authentication) {
		String mail = (String) authentication.getPrincipal();
		userManager.setToken(mail, token);

	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@GetMapping("/allUsers")
	public List<PojoUser> getUsers() {
		return userManager.getUsers();
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@GetMapping("/allUsersByType")
	public List<PojoUser> getUsersByType(@RequestParam String type) {
		return userManager.getUsersByType(type);
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/setType")
	public void setTypeToUser(@RequestBody PojoDoubleString pojo) {
		userManager.setType(pojo.getString1(), pojo.getString2());
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/removeType")
	public void removeTypeToUser(@RequestBody PojoDoubleString pojo) {
		userManager.removeType(pojo.getString1(), pojo.getString2());
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') OR #authentication.principal == #mail")
	@GetMapping("/getUserByMail")
	public PojoUser getUserByMail(@RequestParam String mail, Authentication authentication) {
		return userManager.getUserByMail(mail);
	}
}
