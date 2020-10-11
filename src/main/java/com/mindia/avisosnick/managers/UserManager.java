package com.mindia.avisosnick.managers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mindia.avisosnick.persistence.UserRepository;
import com.mindia.avisosnick.persistence.model.AuthUser;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.util.Constants;
import com.mindia.avisosnick.view.PojoUser;
import com.mindia.avisosnick.view.VUser;

@Service
public class UserManager {

	private static final JacksonFactory jacksonFactory = new JacksonFactory();

	private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
			jacksonFactory)
					// Specify the CLIENT_ID of the app that accesses the backend:
					.setAudience(Collections.singletonList(Constants.GOOGLE_OAUTH_CLIENT_ID))
					// Or, if multiple clients access the backend:
					// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
					.build();

	@Autowired
	UserRepository repo;

	@Autowired
	TypeManager typeManager;

	/**
	 * Se utiliza cuando un administrador quiere crear un usuario.
	 */
	public void createUser(VUser vUser) {
		User user = new User();

		boolean match = Pattern.matches(Constants.REGEX_EMAIL, vUser.getEmail());
		if (!match) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email tiene formato incorrecto.");
		}

		if (repo.getUserByEmail(vUser.getEmail()) != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya hay un usuario con ese email.");
		}

		user.setEmail(vUser.getEmail());
		user.setFullName(vUser.getFullName());

		// TODO: hashear password
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
	 * Se utiliza cuando un administrador quiere cambiar los atributos de un
	 * usuario.
	 */
	public void modifyUser(VUser vUser) {
		// Se acplican los cambios que son normales
		User user = applyModification(vUser);

		// Si se le quitan los roles o se agregan
		if (vUser.getRoles() != null && vUser.getRoles().size() != 0) {
			user.setRoles(vUser.getRoles());
		}

		// No comprueba el tamaño del array porque puede que le quiten todos los tipos
		// de usuario
		if (vUser.getUserType() != null) {
			user.setUserType(vUser.getUserType());
		}

		repo.updateUser(user);
	}

	/**
	 * Se utiliza para cuando un usuario se quiere cambiar los atributos.
	 */
	public void modifyMyUser(VUser vUser) {
		// Se aplican los cambios normales
		User user = applyModification(vUser);

		repo.updateUser(user);
	}

	/**
	 * Se utiliza para validar el inicio de sesion de un usuario
	 */
	public User validateLogIn(String email, String password) {
		User user = repo.getUserByEmail(email);

		if (!user.getPasswordHash().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email y/o contraseña incorrecta.");
		}

		return user;
	}

	/**
	 * Se utiliza para validar el inicio de sesion de un usuario por OAuth Google
	 * TODO: refactorizar metodo
	 */
	public User validateLogInGoogle(String idTokenString) {
		try {
			GoogleIdToken idToken = verifier.verify(idTokenString);
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				String userId = payload.getSubject();
				String email = payload.getEmail();
				Long expirationTime = payload.getExpirationTimeSeconds();

				// TODO: sacar las validacinoes de usuario a otra funcion
				User user = repo.getUserByEmail(email);
				if (user == null) {
					// Si no existe el usuario, se crea en base de datos
					user = new User();

					user.setEmail(email);
					user.setRoles(Arrays.asList(Constants.ROLE_USER));

					AuthUser authUser = new AuthUser();
					authUser.setLastIdToken(idTokenString);
					authUser.setProvider(Constants.OAUTH_PROVIDER_GOOGLE);
					authUser.setUserId(userId);
					authUser.setExpirationLastIdToken(expirationTime);

					user.setAuth(authUser);

					// Se guarda el usuario con el auth puesto
					repo.createUser(user);
				} else {
					// Si el usuario existe, verifica que inicia sesion con Auth
					if (user.getAuth() != null) {
						// Verificamos los datos
						if (!user.getAuth().getProvider().equals(Constants.OAUTH_PROVIDER_GOOGLE)) {
							throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
									"El usuario no tiene asociado este metodo de inicio de sesion.");
						}

						if (!user.getAuth().getUserId().equals(userId)) {
							throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
									"El inicio de sesion de Google no corresponde al usuario.");
						}

						// Si sale todo bien, actualizamos los datos
						user.getAuth().setExpirationLastIdToken(expirationTime);
						user.getAuth().setLastIdToken(idTokenString);
					} else {
						// Si no tiene el Auth, no se dejara iniciar sesion.
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
								"El usuario no tiene asociado este metodo de inicio de sesion.");
					}
				}
				return user;

			} else {
				// El token es invalido, nos e pude verificar con el proveedor
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalido.");
			}

		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Se aplican los cambios normales que cualquier usuario puede hacer. Por ahora
	 * cualquier usuario puede cambiarse la contraseña.
	 */
	private User applyModification(VUser vUser) {
		// Buscamos por mail el usuario
		User user = repo.getUserByEmail(vUser.getEmail());

		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
		}

		user.setPasswordHash(vUser.getPassword());
		user.setFullName(vUser.getFullName());

		return user;
	}

	/**
	 * Se asocia un token con un usuario, para esto se pide el mail del usuario y el
	 * token a asignar.
	 * 
	 * @param mail
	 * @param token
	 */
	public void setToken(String mail, String token) {
		User user = repo.getUserByEmail(mail);
		user.setUniqueMobileToken(token);
		repo.updateUser(user);
	}

	/**
	 * Lista todos los usuarios encontrados en la db.
	 * 
	 * @return una lista de User
	 */
	public List<PojoUser> getUsers() {
		List<PojoUser> pojoUsers = new ArrayList<PojoUser>();
		for (User u : repo.getUsers()) {
			
			PojoUser pojo = new PojoUser();
			pojo.setFullName(u.getFullName());
			pojo.setMail(u.getEmail());
			
			pojoUsers.add(pojo);
		}
		return pojoUsers;
	}

	/**
	 * Lista todos los usuarios con el tipo pedido.
	 * 
	 * @param type - el tipo de usuario que se busca listar
	 * @return Lista de user
	 */
	public List<PojoUser> getUsersByType(String type) {
		List<PojoUser> userByType = new ArrayList<PojoUser>();
		for (User user : repo.getUsers()) {
			for (String userType : user.getUserType()) {
				if (type.equals(userType)) {
					PojoUser pojo= new PojoUser();
					pojo.setFullName(user.getFullName());
					pojo.setMail(user.getEmail());
					userByType.add(pojo);
				}
			}
		}
		return userByType;
	}

	/**
	 * Se le asigna un nuevo tipo de usuario a un usuario TODO: comprobar que no sea
	 * repetido.
	 * 
	 * @param mail
	 * @param type
	 */
	public void setType(String mail, String newTypeCode) {

		if (!typeManager.typeExist(newTypeCode)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuario no encontrado.");
		} else {

			User user = repo.getUserByEmail(mail);
			List<String> types = user.getUserType();
			types.add(newTypeCode);
			user.setUserType(types);
			repo.updateUser(user);
		}

	}

	/**
	 * Se elimina un tipo de usuario a un usuario TODO: comprobar que no quede
	 * vac�o.
	 * 
	 * @param mail
	 * @param type
	 */
	public void removeType(String mail, String type) {
		User user = repo.getUserByEmail(mail);
		List<String> types = user.getUserType();
		types.remove(type);
		user.setUserType(types);
		repo.updateUser(user);
	}

	/**
	 * Se busca un usuario concreto por mail
	 * 
	 * @param mail
	 * @return un usuario o null en caso de no encontrarlo.
	 */
	public PojoUser getUserByMail(String mail) {
		for (User user : repo.getUsers()) {
			if (user.getEmail().equals(mail)) {
				PojoUser pojo= new PojoUser();
				pojo.setFullName(user.getFullName());
				pojo.setMail(mail);
				return pojo;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El mail ingresado no corresponde con ning�n usuario resgistrado.");
	}
}
