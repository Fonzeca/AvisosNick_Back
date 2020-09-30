package com.mindia.avisosnick.persistence.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notices")
@TypeAlias("Notice")
public class Notice {
	@Id
	private ObjectId id;
	private String title, description;
	private User author;
	private List <String> notifiedUsers, readedByUsers;
	private Date creationDate;
	private boolean active, mobileDisp;
	
	public Notice(String title, String description, User autor, List<String> notifiedUsers) {
		this.title=title;
		this.description=description;
		this.author=autor;
		this.notifiedUsers=notifiedUsers;
		creationDate=new Date();
		active=true;
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
	public User getAutor() {
		return author;
	}
	public void setAutor(User autor) {
		this.author = autor;
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
	public boolean isMobileDisp() {
		return mobileDisp;
	}
	public void setMobileDisp(boolean mobileDisp) {
		this.mobileDisp = mobileDisp;
	}
	

}
