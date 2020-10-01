package com.mindia.avisosnick.managers;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.persistence.UserRepository;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.view.VUser;

@Service
public class UserManager {
	//TODO: Esto no va acá
	private final String REGEX_EMAIL = "^(.+)@(.+)$";
	
	@Autowired
	UserRepository repo;
	
	/**
	 * Se utiliza cuando un adminisitrador quiere crear un usuario. 
	 */
	public void createUser(VUser vUser) {
		User user = new User();
		
		boolean match = Pattern.matches(REGEX_EMAIL, vUser.getEmail());
		if(!match) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email tiene formato incorrecto.");
		}
		
		if(repo.getUserByEmail(vUser.getEmail()) != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya hay un usuario con ese email.");
		}
		
		user.setEmail(vUser.getEmail());
		
		//TODO: hashear password
		user.setPasswordHash(vUser.getPassword());
		
		user.setRoles(vUser.getRoles());
		user.setUserType(vUser.getUserType());
		
		
		repo.createUser(user);
	}
	public List<User> getAllUsersByEmails(List<String> emails) {
		List<User> users = new ArrayList<User>();
		for (String string : emails) {
			users.add(repo.getUserByEmail(string));
		}
		return users;
	}
	
	/**
	 * Se utiliza cuando un administrador quiere cambiar los atributos de un usuario. 
	 */
	public void modifyUser(VUser vUser) {
		//Se acplican los cambios que son normales
		User user = applyModification(vUser);
		
		//Si se le quitan los roles o se agregan
		if(vUser.getRoles() != null && vUser.getRoles().size() != 0) {
			user.setRoles(vUser.getRoles());
		}
		
		//No comprueba el tamaño del array porque puede que le quiten todos los tipos de usuario
		if(vUser.getUserType() != null) {
			user.setUserType(vUser.getUserType());
		}
		
		repo.updateUser(user);
	}
	
	/**
	 * Se utiliza para cuando un usuario se quiere cambiar los atributos. 
	 */
	public void modifyMyUser(VUser vUser) {
		//Se aplican los cambios normales
		User user = applyModification(vUser);
		
		repo.updateUser(user);
	}
	
	/**
	 * Se utiliza para validar el inicio de sesion de un usuario
	 */
	public User validateLogIn(String email, String password) {
		User user = repo.getUserByEmail(email);
		
		if(!user.getPasswordHash().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email y/o contraseña incorrecta.");
		}
		
		return user;
	}

	/**
	 * Se aplican los cambios normales que cualquier usuario puede hacer.
	 * Por ahora cualquier usuario puede cambiarse la contraseña. 
	 */
	private User applyModification(VUser vUser) {
		//Buscamos por mail el usuario
		User user = repo.getUserByEmail(vUser.getEmail());
		
		if(user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
		}
		
		user.setPasswordHash(vUser.getPassword());
		
		return user;
	}
}
