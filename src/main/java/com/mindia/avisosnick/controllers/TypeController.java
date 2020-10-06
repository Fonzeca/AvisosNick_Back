package com.mindia.avisosnick.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindia.avisosnick.managers.TypeManager;
import com.mindia.avisosnick.persistence.model.UserType;
import com.mindia.avisosnick.util.Constants;
import com.mindia.avisosnick.view.PojoUserType;

@RestController
@RequestMapping("/types")
public class TypeController {
	
	@Autowired
	TypeManager manager;

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/create")
	public void createUserType(@RequestBody PojoUserType pojo) {
		manager.createUserType(pojo.getCode(), pojo.getDescription());
	}
	
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@GetMapping("/allTypes")
	public List<UserType> getTypes() {
		return manager.getTypes();
	}
	
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/modify")
	public void modifyType(@RequestBody PojoUserType pojo) {
		manager.modifyUserType(pojo);
	}
	
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	@PostMapping("/deactivate")
	public void deactivateType(@RequestParam String code) {
		manager.deactivateType(code);
	}
}
