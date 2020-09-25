package com.mindia.avisosnick.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mindia.avisosnick.persistence.model.User;
import com.mongodb.client.MongoClients;

@Repository
public class UserRepository {
	
//	@Autowired
//	private MongoDbProperties mongoProperties;
	
	private MongoOperations mongoOps;
	
	
	@PostConstruct
	private void init() {
		
		//TODO: hacer la conexion en otra clase
		mongoOps = new MongoTemplate(MongoClients.create("mongodb://usrNick:huffm4n123@vps-1791261-x.dattaweb.com:27017"), "AvisosNick");
		
//	    mongoOps.insert(new Person("Joe", 34));
	}
	
	public User getUserByUserName(String userName) {
		Query query = new Query(where("username").is(userName));
		
		User user = mongoOps.findOne(query, User.class);
		return user;
	}
}
