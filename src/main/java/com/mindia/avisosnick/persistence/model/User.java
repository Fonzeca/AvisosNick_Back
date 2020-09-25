package com.mindia.avisosnick.persistence.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
	
	@Id
    private ObjectId id;
    private String username;
    private String passwordHash;
    private String uniqueMobileToken;
}
