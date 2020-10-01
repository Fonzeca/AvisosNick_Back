package com.mindia.avisosnick.managers;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.persistence.UserRepository;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.view.VUser;

@Service
public class UserManager {
	
	private final String REGEX_EMAIL = "";
	
	@Autowired
	UserRepository repo;
	
	public void createUser(VUser vUser) throws Exception {
		User user = new User();
		
		boolean match = Pattern.matches(REGEX_EMAIL, vUser.getEmail());
		if(!match) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email tiene formato incorrecto.");
		}
		
		user.setEmail(vUser.getEmail());
		
		//TODO: hashear password
		user.setPasswordHash(vUser.getPassword());
		
		if(repo.getUserByEmail(vUser.getEmail()) != null) {
			//TODO: handle exceptions system
			throw new Exception("Ya hay un usuario con ese email.");
		}
		
		
		repo.createUser(user);
	}
	
	public User validateLogIn(String email, String password) {
		User user = repo.getUserByEmail(email);
		
		if(!user.getPasswordHash().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email y/o contraseña incorrecta.");
		}
		
		return user;
	}
}
