package com.mindia.avisosnick.controllers;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogInController {
	
	@PostConstruct
	public void logIn() {
		try {
//			FileInputStream serviceAccount = new FileInputStream("C:\\Users\\Alexis Fonzo\\Desktop\\Software para Nick Tecnología\\Secretos\\service-account-file.json");
//			
//			FirebaseOptions options = FirebaseOptions.builder()
//					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
//					.setDatabaseUrl("https://nickservicios-notices.firebaseio.com")
//					.build();
//			
//			FirebaseApp.initializeApp(options);
//			
//			String userName = "";
//			
//			FirebaseAuth auth = FirebaseAuth.getInstance();
//			
//			
//			
//			CreateRequest rq = new CreateRequest();
//			
//			ListUsersPage page = auth.listUsers(null);
//			
//			while (page != null) {
//			  for (ExportedUserRecord user : page.getValues()) {
//			    System.out.println("User: " + user.getPasswordSalt());
//			  }
//			  page = page.getNextPage();
//			}
			
		
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@GetMapping("/holis")
	public String Prueba() {
		return "Heello";
	}
}
