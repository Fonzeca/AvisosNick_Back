package com.mindia.avisosnick.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mindia.avisosnick.managers.NoticeManager;
import com.mindia.avisosnick.managers.UserManager;
import com.mindia.avisosnick.persistence.model.Notice;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.view.PojoCreateNotice;

@RestController
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	NoticeManager manager;
	
	@Autowired
	UserManager userManager;

	@PostMapping("/create")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void createNotice(@RequestBody PojoCreateNotice pojo, Authentication authentication) {
		List<User> lstUsers = userManager.getAllUsersByEmails(Arrays.asList((String)authentication.getPrincipal()));
		
		if(lstUsers == null || lstUsers.size() == 0 || lstUsers.size() > 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author.");
		}
		
		manager.createNotice(pojo.getMails(), pojo.isSendNotification(), pojo.getTitle(), pojo.getDescription(), lstUsers.get(0));
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
		//TODO: sacar esto a otro lado
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
