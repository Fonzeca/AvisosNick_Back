package com.mindia.avisosnick.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.persistence.model.User;

@Repository
public class UserRepository {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void createUser(User user) {
		try {
			mongoTemplate.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public User getUserByEmail(String email) {
		Query query = new Query(where("email").is(email));
		
		User user = mongoTemplate.findOne(query, User.class);
		return user;
	}
	
	public void updateUser(User user) {
		try {
			Query query = new Query(where("_id").is(user.getId()));
			
			User busquedaUser = mongoTemplate.findOne(query, User.class);
			
			if(busquedaUser == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede actualizar un usuario inexistente.");
			}
			
			mongoTemplate.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
