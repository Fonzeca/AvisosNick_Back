package com.mindia.avisosnick.controllers;

import java.io.IOException;
import java.io.InputStream;
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
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.util.Constants;
import com.mindia.avisosnick.view.PojoCreateNotice;
import com.mindia.avisosnick.view.PojoId;
import com.mindia.avisosnick.view.PojoModifyNotice;
import com.mindia.avisosnick.view.PojoNotice;

@RestController
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	NoticeManager manager;

	@Autowired
	UserManager userManager;

	@PostMapping("/create")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	public void createNotice(@RequestBody PojoCreateNotice pojo, Authentication authentication) {
		List<User> lstUsers = userManager.getAllUsersByEmails(Arrays.asList((String) authentication.getPrincipal()));

		if (lstUsers == null || lstUsers.size() == 0 || lstUsers.size() > 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author.");
		}

		manager.createNotice(pojo.getTypes(), pojo.getMails(), pojo.isSendNotification(), pojo.getTitle(), pojo.getDescription(), lstUsers.get(0), pojo.getData().getAdditionalProperties());
	}

	@PostMapping("/markAsRead")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') OR hasRole('" + Constants.ROLE_USER + "')")
	public void markNoticeAsRead(@RequestBody PojoId idNotice, Authentication authentication) {
		manager.markAsRead((String) authentication.getPrincipal(), new ObjectId(idNotice.getId()));
	}

	@PostMapping("/deactivate")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	public void deactivateNotice(@RequestBody PojoId idNotice) {
		manager.deactivate(new ObjectId(idNotice.getId()));
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	public void modifyNotice(@RequestBody PojoModifyNotice modifyNotice) {
		manager.modify(new ObjectId(modifyNotice.getIdNotice()), modifyNotice.getTitle(),
				modifyNotice.getDescription());
	}

	@PostMapping("/readedBy")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	public List<String> getReaders(@RequestBody PojoId idNotice) {
		return manager.getReaders(new ObjectId(idNotice.getId()));
	}

	@GetMapping("/checkNotices")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') OR hasRole('" + Constants.ROLE_USER + "')")
	public List<PojoNotice> noticesByUser(Authentication authentication) {
		return manager.getNoticesByUser((String) authentication.getPrincipal());
	}

	@GetMapping("/getAll")
	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	public List<PojoNotice> getAllNotices() {
		return manager.getNotices();
	}

	@PreAuthorize("hasRole('\" + Constants.ROLE_ADMIN + \"') OR hasRole('\" + Constants.ROLE_USER + \"')")
	@GetMapping("/get")
	public PojoNotice getNoticeById(@RequestParam String id) {
		return manager.getNotice(id);
	}

	@PostConstruct
	private void firebaseLogIn() {
		// TODO: sacar esto a otro lado
		try {

			InputStream serviceAccount = getClass()
					.getResourceAsStream("/avisosnick-firebase-adminsdk-ln9j6-55140aa5db.json");

//					new FileInputStream(
//					"src/main/resources/avisosnick-firebase-adminsdk-ln9j6-55140aa5db.json");

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://avisosnick.firebaseio.com").build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
