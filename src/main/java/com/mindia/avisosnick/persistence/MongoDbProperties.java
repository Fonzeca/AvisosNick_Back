package com.mindia.avisosnick.persistence;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;



@ConfigurationProperties("database.mongo")
public class MongoDbProperties {
	public String user;
	public String password;
	public String ip;
	public String port;
	
	public String db;
	
	public MongoClient getUri() {
		return MongoClients.create("mongodb://" + user + ":" + password + "@" + ip + ":" + port);
	}
}
