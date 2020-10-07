package com.mindia.avisosnick.managers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.persistence.TypeRepository;
import com.mindia.avisosnick.persistence.model.UserType;
import com.mindia.avisosnick.view.PojoUserType;

@Service
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
	public List<UserType> getTypes(){
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
	
	/**
	 * Se desactiva un tipo de usuario
	 * @param code
	 */
	public void deactivateType(String code) {
		UserType type=repo.getUserByCode(code);
		type.setActive(false);
		repo.updateUserType(type);
	}

	/**
	 * Método para comprobar si existe un tipo de usuario con el código ingresado
	 * @param code
	 * @return
	 */
	public boolean typeExist(String code) {
		for(UserType type:getTypes()) {
			if(type.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
	
}
