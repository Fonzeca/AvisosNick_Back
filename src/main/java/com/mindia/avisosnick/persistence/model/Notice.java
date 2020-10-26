package com.mindia.avisosnick.persistence.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mindia.avisosnick.view.PojoUser;

@Document(collection = "notices")
@TypeAlias("Notice")
public class Notice {
	@Id
	private ObjectId id;
	private String title, description;
	private PojoUser author;
	private List <String> notifiedUsers = new ArrayList<String>(), readedByUsers = new ArrayList<String>();
	private Date creationDate;
	private boolean active, sendNotification;
	
	public Notice(String title, String description, PojoUser author, List<String> notifiedUsers, boolean sendNotification) {
		this.title=title;
		this.description=description;
		this.author=author;
		this.notifiedUsers=notifiedUsers;
		creationDate=new Date();
		active=true;
		readedByUsers= new ArrayList<String>();
		this.sendNotification=sendNotification;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PojoUser getAuthor() {
		return author;
	}
	public void setAuthor(PojoUser author) {
		this.author = author;
	}
	public List<String> getNotifiedUsers() {
		return notifiedUsers;
	}
	public void setNotifiedUsers(List<String> notifiedUsers) {
		this.notifiedUsers = notifiedUsers;
	}
	public List<String> getReadedByUsers() {
		return readedByUsers;
	}
	public void setReadedByUsers(List<String> readedByUsers) {
		this.readedByUsers = readedByUsers;
	}
	public void readedByUser(String mail) {
		readedByUsers.add(mail);
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isSend() {
		return sendNotification;
	}
	public void setSend(boolean mobileDisp) {
		this.sendNotification = mobileDisp;
	}
	

}
