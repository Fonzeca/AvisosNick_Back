package com.mindia.avisosnick.managers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.mindia.avisosnick.persistence.NoticeRepository;
import com.mindia.avisosnick.persistence.model.Notice;
import com.mindia.avisosnick.persistence.model.User;
import com.mindia.avisosnick.view.PojoUser;

@Service
public class NoticeManager {
	@Autowired
	UserManager uManager;

	@Autowired
	NoticeRepository nRepo;

	final private int DAYINMILLISECONDS = 86400000;// 3600 seconds * 1000 to milli * 24 hours

	public void createNotice(List<String> mails, boolean send, String title, String description, User author) {
		PojoUser pUser = new PojoUser();
		pUser.setMail(author.getEmail());
		pUser.setFullName(author.getFullName());

		Notice notice = new Notice(title, description, pUser, mails);
		if (send) {
			List<User> usersToSend = uManager.getAllUsersByEmails(mails);
			List<String> tokens = new ArrayList<String>();
			for (User user : usersToSend) {
				if(user.getUniqueMobileToken().equals(null)) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario "+user.getFullName()+" no posee asignado un token al cual enviar notificaciones.");
				}
				tokens.add(user.getUniqueMobileToken());
				System.out.println(user.getUniqueMobileToken());
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
				System.out.println("\nand "+response.getFailureCount()+" messages were not send.");
			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
			}
			// [END send_multicast]
		}
		nRepo.createNotice(notice);

	}

	public void markAsRead(String mail, ObjectId idNotice) {
		Notice notice = nRepo.getNoticeById(idNotice);
		notice.readedByUser(mail);
		nRepo.createNotice(notice);

	}

	public void deactivate(ObjectId noticeId) {
		Notice notice = nRepo.getNoticeById(noticeId);
		notice.setActive(false);
		nRepo.createNotice(notice);

	}

	public void modify(ObjectId idNotice, String title, String description) {
		Notice notice = nRepo.getNoticeById(idNotice);
		notice.setTitle(title);
		notice.setDescription(description);
		nRepo.createNotice(notice);

	}

	public List<String> getReaders(ObjectId idNotice) {
		Notice notice = nRepo.getNoticeById(idNotice);
		return notice.getReadedByUsers();

	}

	public List<Notice> getNoticesByUser(String mail) {
		List<Notice> noticesForUser = new ArrayList<Notice>();
		for (Notice notice : nRepo.getAllNotices()) {
			for (String userMail : notice.getNotifiedUsers()) {
				if (userMail.equals(mail)) {
					noticesForUser.add(notice);
				}
			}
		}
		return noticesForUser;
	}

	public Notice getNotice(String id) {
		return nRepo.getNoticeById(new ObjectId(id));
	}

}
