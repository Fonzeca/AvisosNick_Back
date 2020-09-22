package com.mindia.avisosnick.persistence;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity("Books")
public class Book {
    @Id
    private ObjectId isbn;
    private String title;
    private String author;
    @Property("price")
    private double cost;
	public Book(ObjectId isbn, String title, String author, double cost) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.cost = cost;
	}
    
}
