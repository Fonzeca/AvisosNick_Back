package com.mindia.avisosnick.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mindia.avisosnick.persistence.model.User;

@Repository
public class UserRepository {
	
	@Autowired
	private MongoDbProperties mongoProperties;
	
	private MongoOperations mongoOps;
	
	
	@PostConstruct
	private void init() {
		
		//TODO: hacer la conexion en otra clase
		mongoOps = new MongoTemplate(mongoProperties.getUri(), mongoProperties.db);
		
//	    mongoOps.insert(new Person("Joe", 34));
	}
	
	public User getUserByUserName(String userName) {
		Query query = new Query(where("username").is(userName));
		
		User user = mongoOps.findOne(query, User.class);
		return user;
	}
}
