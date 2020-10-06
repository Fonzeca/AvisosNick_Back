package com.mindia.avisosnick.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.mindia.avisosnick.persistence.model.UserType;

@Repository
public class TypeRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void createUserType(UserType type) {
		try {
			mongoTemplate.save(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UserType getUserByCode(String code) {
		Query query = new Query(where("code").is(code));

		UserType type = mongoTemplate.findOne(query, UserType.class);
		return type;
	}

	public void updateUserType(UserType type) {
		try {
			Query query = new Query(where("_id").is(type.getId()));

			UserType busquedaUser = mongoTemplate.findOne(query, UserType.class);

			if (busquedaUser == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"No se puede actualizar un tipo de usuario inexistente.");
			}

			mongoTemplate.save(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<UserType> getAllTypes() {
		Query query= new Query(where("active").is(true));
		return mongoTemplate.find(query, UserType.class);
	}
}
