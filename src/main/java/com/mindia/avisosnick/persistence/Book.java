package com.mindia.avisosnick.persistence;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("User")
public class Book {
    @Id
    private ObjectId id;
    private String username;
    private String passwordHash;
}
