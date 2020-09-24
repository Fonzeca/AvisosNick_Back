package com.mindia.avisosnick.controllers;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@RestController
public class LogInController {

	@PostConstruct
	public void logIn() {
		try {
			System.out.println("test");

			FileInputStream serviceAccount = new FileInputStream(
					"/resources/avisosnick-firebase-adminsdk-ln9j6-55140aa5db.json");

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://avisosnick.firebaseio.com").build();
			
			FirebaseApp.initializeApp(options);
			System.out.println("test done");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@GetMapping("/holis")
	public String Prueba() {
		return "Heello";
	}
}
