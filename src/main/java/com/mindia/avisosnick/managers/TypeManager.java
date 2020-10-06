package com.mindia.avisosnick.managers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.persistence.TypeRepository;
import com.mindia.avisosnick.persistence.model.UserType;
import com.mindia.avisosnick.view.PojoUserType;

public class TypeManager {

	@Autowired
	TypeRepository repo;

	/**
	 * Se utiliza cuando un administrador quiere crear un tipo de usuario.
	 */
	public void createUserType(String code, String description) {
		UserType type=new UserType();
		type.setCode(code);
		type.setDescription(description);
		type.setActive(true);
		if (repo.getUserByCode(type.getCode()) != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un tipo de usuario con ese código.");
		}
		repo.createUserType(type);
	}
	
	/**
	 * Se utiliza para listar los tipos de usuario disponibles
	 */
	public List<UserType> types(){
		return repo.getAllTypes();
	}
	/**
	 * Se modifica la descripción de un tipo de usuario
	 * @param pojo "code":código del tipo a editar, "description": nueva descripción
	 */
	public void modifyUserType(PojoUserType pojo) {
		UserType type=repo.getUserByCode(pojo.getCode());
		type.setDescription(pojo.getDescription());
		repo.updateUserType(type);
	}
	
	public void deactivateType(String code) {
		UserType type=repo.getUserByCode(code);
		type.setActive(false);
		repo.updateUserType(type);
	}

	
}
