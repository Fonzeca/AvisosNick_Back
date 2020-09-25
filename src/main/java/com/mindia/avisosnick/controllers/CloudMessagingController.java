package com.mindia.avisosnick.controllers;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

@RestController
public class CloudMessagingController {

	@PostMapping("/newMessage")
	public void sendToToken(
			//@RequestParam String token
			) {
		// Send a message to the device corresponding to the provided
		// registration token.
		String response;
		//Token Sony
//		String token= "dOZv-2maRrW0sFj_c0JdLu:APA91bE_wbWHlZ6yjLlwzyhWiGXwgyNWjJCGnzaPOL56S3iWY1K3yLyL93MGhYtctCCdt4yMT-s2C2UKiTf57sRNpE3_-UMKzdiVZ7MrFaBR-1wCQoibE2HSn3jVYH_v8JOlw8PH2xU9";
	//Token Fonzo
		String token="cyPz04QpSqK96YtIn785cE:APA91bFegLKBzmktuQ3UqHSGLV129A5prEDxW27FD4YJllK9AsAABRMPbaKKz6kvtllQWulRKB2ZkRRuyUQc8AufAkeDW5N94gGpbiEkN7Hdn_m9O-A3ikkbP2-7iLzcaOz_uXEIWN2N";
		
		try {
			response = FirebaseMessaging.getInstance().send(androidMessage(token));
			// Response is a message ID string.
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	private void firebaseLogIn() {
		try {
			System.out.println(System.getProperty("user.dir"));
			FileInputStream serviceAccount =
					  new FileInputStream("src/main/resources/avisosnick-firebase-adminsdk-ln9j6-55140aa5db.json");
			
					FirebaseOptions options = FirebaseOptions.builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .setDatabaseUrl("https://avisosnick.firebaseio.com")
					  .build();

					FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private Message androidMessage(String token) {
		// [START android_message]
		Message message = Message.builder().setAndroidConfig(AndroidConfig.builder().setTtl(3600 * 1000) // 1 hour in
																											// milliseconds
				.setPriority(AndroidConfig.Priority.NORMAL)
				.setNotification(AndroidNotification.builder().setTitle("Test Message using Eclipse.")
						.setBody("This is a test message created on AvisosNick by Daiko'.").build())
				.build()).setToken(token).build();
		// [END android_message]
		return message;
	}
}
