package com.mindia.avisosnick.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mindia.avisosnick.persistence.model.User;

@Repository
public class UserRepository {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	
	public User getUserByUserName(String userName) {
		Query query = new Query(where("username").is(userName));
		
		User user = mongoTemplate.findOne(query, User.class);
		return user;
	}
}
