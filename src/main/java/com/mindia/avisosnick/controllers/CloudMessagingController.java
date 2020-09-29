package com.mindia.avisosnick.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mindia.avisosnick.managers.CloudMessagingManager;

@RestController
public class CloudMessagingController {
	@Autowired
	CloudMessagingManager manager;

	@PostMapping("/newNotice")
	public void sendToToken(
	// @RequestParam String token
	) {
		// Token Sony
		String token= "dOZv-2maRrW0sFj_c0JdLu:APA91bE_wbWHlZ6yjLlwzyhWiGXwgyNWjJCGnzaPOL56S3iWY1K3yLyL93MGhYtctCCdt4yMT-s2C2UKiTf57sRNpE3_-UMKzdiVZ7MrFaBR-1wCQoibE2HSn3jVYH_v8JOlw8PH2xU9";
		// Token Fonzo
//		String token = "cyPz04QpSqK96YtIn785cE:APA91bFegLKBzmktuQ3UqHSGLV129A5prEDxW27FD4YJllK9AsAABRMPbaKKz6kvtllQWulRKB2ZkRRuyUQc8AufAkeDW5N94gGpbiEkN7Hdn_m9O-A3ikkbP2-7iLzcaOz_uXEIWN2N";

		manager.sendNotice(token);
	}
	
	@PostMapping("/multipleNotice")
	public void sendMultiple() {
		//Up to 500 tokens
		List<String> tokens= Arrays.asList(
				"dOZv-2maRrW0sFj_c0JdLu:APA91bE_wbWHlZ6yjLlwzyhWiGXwgyNWjJCGnzaPOL56S3iWY1K3yLyL93MGhYtctCCdt4yMT-s2C2UKiTf57sRNpE3_-UMKzdiVZ7MrFaBR-1wCQoibE2HSn3jVYH_v8JOlw8PH2xU9",
				"cyPz04QpSqK96YtIn785cE:APA91bFegLKBzmktuQ3UqHSGLV129A5prEDxW27FD4YJllK9AsAABRMPbaKKz6kvtllQWulRKB2ZkRRuyUQc8AufAkeDW5N94gGpbiEkN7Hdn_m9O-A3ikkbP2-7iLzcaOz_uXEIWN2N");
	manager.sendMultipleNotices(tokens);
	}
	
	@PostMapping("/readNotice")
	public void markNoticeAsRead() {
		//TODO: completar mail e id
		String mail="", idNotice = "";
		manager.markAsRead(mail, idNotice);
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
