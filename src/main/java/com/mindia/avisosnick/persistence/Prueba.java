package com.mindia.avisosnick.persistence;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Repository
public class Prueba {
	
	public void init() {
		final MongoClientURI uri = new MongoClientURI("mongodb://root:Ale159159!@vps-1791261-x.dattaweb.com:27017");
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.mindia.avisosnick.persistence");
		
		Datastore dataStore = morphia.createDatastore(new MongoClient(uri), "prueba");
		
		dataStore.ensureIndexes();
		
		Book book = new Book(new ObjectId(), "Mundo de las tinieblas", "afonzo", 52);
		
		dataStore.save(book);
		
	}
}
