package com.mindia.avisosnick.managers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.mindia.avisosnick.persistence.model.Notice;
import com.mindia.avisosnick.persistence.model.User;

@Service
public class NoticeManager {
	@Autowired
	UserManager uManager;
	final private int DAYINMILLISECONDS = 86400000;// 3600 seconds * 1000 to milli * 24 hours


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
				.setAndroidConfig(AndroidConfig.builder().setTtl(DAYINMILLISECONDS * 7) // 1 week in milliseconds
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

	public void createNotice(List<String> mails, boolean send, String title, String description, User author) {
		Notice notice = new Notice(title, description, author, mails);
		// TODO: agregar a base de datos?
		if (send) {
			List<User> usersToSend= uManager.getAllUsersByEmails(mails);
			List<String> tokens= new ArrayList<String>();
			for (User user : usersToSend) {
				tokens.add(user.getUniqueMobileToken());
			}
			MulticastMessage notification = MulticastMessage.builder()
					.setAndroidConfig(AndroidConfig.builder().setTtl(DAYINMILLISECONDS * 7) // 1 week in milliseconds
							.setPriority(AndroidConfig.Priority.NORMAL)
							.setNotification(AndroidNotification.builder().setTitle(title).setBody(description).build())
							.build())
					.addAllTokens(tokens).build();
			BatchResponse response;
			try {
				response = FirebaseMessaging.getInstance().sendMulticast(notification);
				// See the BatchResponse reference documentation
				// for the contents of response.
				System.out.println(response.getSuccessCount() + " messages were sent successfully");
			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
			}
			// [END send_multicast]
		}
	}

}
