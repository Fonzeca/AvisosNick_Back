package com.mindia.avisosnick.managers;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

@Service
public class CloudMessagingManager {

	public void sendMessage(String token) {
		// Send a message to the device corresponding to the provided
		// registration token.
		String response;

		try {
			response = FirebaseMessaging.getInstance().send(androidMessage(token));
			// Response is a message ID string.
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}

	private Message androidMessage(String token) {
		// [START android_message]
		Message message = Message.builder()
				//.putData("key", "data'")
				.setAndroidConfig(AndroidConfig.builder().setTtl(3600 * 1000 * 24 * 7) // 1 week in milliseconds
				.setPriority(AndroidConfig.Priority.NORMAL)
				.setNotification(AndroidNotification.builder().setTitle("Test Message using Eclipse.")
						.setBody("This is a test message created on AvisosNick by Daiko'.").build())
				.build()).setToken(token).build();
		// [END android_message]
		return message;
	}

}
