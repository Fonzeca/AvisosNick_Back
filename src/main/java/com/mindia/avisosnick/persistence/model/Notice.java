package com.mindia.avisosnick.persistence.model;

import java.util.ArrayList;
import java.util.Date;

public class Notice {
	private int id;
	private String title, description;
	private User autor;
	private ArrayList <String> notifiedUsers, readedByUsers;
	private Date creationDate;
	private boolean active, mobileDisp;
	
	public Notice(String title, String description, User autor, ArrayList<String> notifiedUsers) {
		this.title=title;
		this.description=description;
		this.autor=autor;
		this.notifiedUsers=notifiedUsers;
		creationDate=new Date();
		active=true;
	}
	public void removeNotice(int id) {
		//TODO: seleccionar la noticia desde la base de datos.
		this.active=false;
	}
	public void editNotice(String title, String description) {
		this.title=title;
		this.description=description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
		return autor;
	}
	public void setAutor(User autor) {
		this.autor = autor;
	}
	public ArrayList<String> getNotifiedUsers() {
		return notifiedUsers;
	}
	public void setNotifiedUsers(ArrayList<String> notifiedUsers) {
		this.notifiedUsers = notifiedUsers;
	}
	public ArrayList<String> getReadedByUsers() {
		return readedByUsers;
	}
	public void setReadedByUsers(ArrayList<String> readedByUsers) {
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
