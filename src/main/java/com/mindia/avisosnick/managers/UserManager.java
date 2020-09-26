package com.mindia.avisosnick.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindia.avisosnick.persistence.UserRepository;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.view.VUser;

@Service
public class UserManager {
	
	@Autowired
	UserRepository repo;
	
	public void createUser(VUser vUser) throws Exception {
		User user = new User();
		
		user.setEmail(vUser.getEmail());
		
		//TODO: hashear password
		user.setPasswordHash(vUser.getPassword());
		
		if(repo.getUserByEmail(vUser.getEmail()) != null) {
			//TODO: handle exceptions system
			throw new Exception("Ya hay un usuario con ese email.");
		}
		
		
		repo.createUser(user);
	}
	
}
