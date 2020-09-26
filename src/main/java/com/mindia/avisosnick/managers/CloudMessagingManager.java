package com.mindia.avisosnick.managers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

@Service
public class CloudMessagingManager {

	public void sendMultipleNotices(List<String> tokens) {
		MulticastMessage notice = MulticastMessage.builder().setAndroidConfig(AndroidConfig.builder().setTtl(3600 * 1000 * 24 * 7) // 1 week in milliseconds
				.setPriority(AndroidConfig.Priority.NORMAL)
				.setNotification(AndroidNotification.builder().setTitle("Test Multimessage using Eclipse.")
						.setBody("This is a test message created on AvisosNick by Daiko'.").build())
				.build())
				.addAllTokens(tokens).build();
		BatchResponse response;
		try {
			response = FirebaseMessaging.getInstance().sendMulticast(notice);
			// See the BatchResponse reference documentation
			// for the contents of response.
			System.out.println(response.getSuccessCount() + " messages were sent successfully");
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
		// [END send_multicast]

	}

	public void sendNotice(String token) {
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
				// .putData("key", "data'")
				.setAndroidConfig(AndroidConfig.builder().setTtl(3600 * 1000 * 24 * 7) // 1 week in milliseconds
						.setPriority(AndroidConfig.Priority.NORMAL)
						.setNotification(AndroidNotification.builder().setTitle("Test Message using Eclipse.")
								.setBody("This is a test message created on AvisosNick by Daiko'.").build())
						.build())
				.setToken(token).build();
		// [END android_message]
		return message;
	}

	public void markAsRead(String mail, String idNotice) {
		
		
	}

}
