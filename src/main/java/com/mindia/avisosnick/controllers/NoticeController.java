package com.mindia.avisosnick.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mindia.avisosnick.managers.NoticeManager;
import com.mindia.avisosnick.persistence.model.User;

@RestController
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	NoticeManager manager;

	@PostMapping("/create")
	public void createNotice(@RequestParam List<String> mails, @RequestParam boolean send, @RequestParam String title,
			@RequestParam String description, @RequestParam User author) {
		
		manager.createNotice(mails, send, title, description, author);
	}
	
	@PostMapping("/markReaded")
	public void markNoticeAsRead(@RequestParam String mail, @RequestParam ObjectId idNotice) {
		manager.markAsRead(mail, idNotice);
	}
	@PostMapping("/deactivate")
	public void deactivateNotice(@RequestParam ObjectId noticeId) {
		manager.deactivate(noticeId);
	}

	@PostConstruct
	private void firebaseLogIn() {
		try {
			FileInputStream serviceAccount = new FileInputStream(
					"src/main/resources/avisosnick-firebase-adminsdk-ln9j6-55140aa5db.json");

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://avisosnick.firebaseio.com").build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
