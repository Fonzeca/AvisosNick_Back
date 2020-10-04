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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mindia.avisosnick.managers.NoticeManager;
import com.mindia.avisosnick.persistence.model.Notice;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.view.PojoIdNotice;
import com.mindia.avisosnick.view.PojoModifyNotice;

@RestController
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	NoticeManager manager;
	

	@PostMapping("/create")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void createNotice(
//			@RequestParam List<String> mails, 
			@RequestParam boolean sendNotification, @RequestParam String title,
			@RequestParam String description
//			, @RequestParam User author
			) {
		
		List<String> mails= new ArrayList<String> ();
		//TODO: REMOVE
		mails.add("daiko_011@hotmail.com");
		mails.add("daiko_022@hotmail.com");
		User user= new User();
		user.setEmail("as@gh.com");
		
		manager.createNotice(mails, sendNotification, title, description, user);
	}

	@PostMapping("/markAsRead")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
	public void markNoticeAsRead(@RequestBody PojoIdNotice idNotice, Authentication authentication) {
		manager.markAsRead((String)authentication.getPrincipal(), new ObjectId(idNotice.getIdNotice()));
	}

	@PostMapping("/deactivate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deactivateNotice(@RequestBody PojoIdNotice idNotice) {
		manager.deactivate(new ObjectId(idNotice.getIdNotice()));
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void modifyNotice(@RequestBody PojoModifyNotice modifyNotice) {
		manager.modify(new ObjectId(modifyNotice.getIdNotice()), modifyNotice.getTitle(), modifyNotice.getDescription());
	}
	
	@PostMapping("/readedBy")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<String> getReaders(@RequestBody PojoIdNotice idNotice){
		return manager.getReaders(new ObjectId(idNotice.getIdNotice()));
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
