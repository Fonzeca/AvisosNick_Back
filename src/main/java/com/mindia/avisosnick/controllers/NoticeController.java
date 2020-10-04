package com.mindia.avisosnick.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mindia.avisosnick.managers.NoticeManager;
import com.mindia.avisosnick.persistence.model.Notice;
import com.mindia.avisosnick.persistence.model.User;

@RestController
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	NoticeManager manager;

	@PostMapping("/create")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void createNotice(
//			@RequestParam List<String> mails, 
			@RequestParam boolean send, @RequestParam String title,
			@RequestParam String description
//			, @RequestParam User author
			) {
		
		List<String> mails= new ArrayList<String> ();
		//TODO: REMOVE
		mails.add("daiko_011@hotmail.com");
		mails.add("daiko_022@hotmail.com");
		User user= new User();
		user.setEmail("as@gh.com");
		
		manager.createNotice(mails, send, title, description, user);
	}

	@PostMapping("/markAsRead")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
	public void markNoticeAsRead(@RequestParam String mail, @RequestParam ObjectId idNotice) {
		manager.markAsRead(mail, idNotice);
	}

	@PostMapping("/deactivate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deactivateNotice(@RequestParam ObjectId noticeId) {
		manager.deactivate(noticeId);
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void modifyNotice(@RequestParam ObjectId idNotice, @RequestParam String title,
			@RequestParam String description) {
		manager.modify(idNotice, title, description);
	}
	
	@PostMapping("/readedBy")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<String> getReaders(@RequestParam ObjectId idNotice){
		return manager.getReaders(idNotice);
	}

	@GetMapping("/checkNotices")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
	public List<Notice> noticesByUser(Authentication authentication){
		return manager.getNoticesByUser((String)authentication.getPrincipal());
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
