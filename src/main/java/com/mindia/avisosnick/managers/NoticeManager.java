package com.mindia.avisosnick.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import com.mindia.avisosnick.view.PojoNotice;
import com.mindia.avisosnick.view.PojoUser;

@Service
public class NoticeManager {
	@Autowired
	UserManager uManager;

	@Autowired
	NoticeRepository nRepo;

	final private int DAYINMILLISECONDS = 86400000;// 3600 seconds * 1000 to milli * 24 hours

	public void createNotice(List<String> types, List<String> mails, boolean send, String title, String description, User author) {
		PojoUser pUser = new PojoUser();
		pUser.setMail(author.getEmail());
		pUser.setFullName(author.getFullName());
		if(mails==(null) || mails.isEmpty()) {
			mails= new ArrayList<String>();
			for (String string : types) {
				for (PojoUser user : uManager.getUsersByType(string)) {
					mails.add(user.getMail());
				};
			}
		}

		Notice notice = new Notice(title, description, pUser, mails);
		if (send) {
			List<User> usersToSend = uManager.getAllUsersByEmails(mails);
			List<String> tokens = new ArrayList<String>();
			for (User user : usersToSend) {
				if (user.getUniqueMobileToken().equals(null)) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario " + user.getFullName()
							+ " no posee asignado un token al cual enviar notificaciones.");
				}
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
				System.out.println("\nand " + response.getFailureCount() + " messages were not send.");
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

	public List<PojoNotice> getNoticesByUser(String mail) {
		List<PojoNotice> noticesForUser = new ArrayList<PojoNotice>();
		List<Notice> notices= nRepo.getAllNotices();
		Collections.sort(notices,new SortbyDate());
		Collections.reverse(notices);
		for (Notice notice : notices) {
			for (String userMail : notice.getNotifiedUsers()) {
				if (userMail.equals(mail)) {
					PojoNotice pojo = new PojoNotice();
					pojo.setId(notice.getId().toString());
					pojo.setTitle(notice.getTitle());
					pojo.setDescription(notice.getDescription());
					pojo.setAuthor(notice.getAuthor().getFullName());
					
					Date dateNotice = notice.getCreationDate();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
					
					pojo.setCreationDate(sdf.format(dateNotice));

					noticesForUser.add(pojo);
				}
			}
		}
		return noticesForUser;
	}

	public PojoNotice getNotice(String id) {
		Notice n = nRepo.getNoticeById(new ObjectId(id));
		PojoNotice pojo = new PojoNotice();
		pojo.setId(n.getId().toString());
		pojo.setTitle(n.getTitle());
		pojo.setDescription(n.getDescription());
		pojo.setAuthor(n.getAuthor().getFullName());
		pojo.setCreationDate(n.getCreationDate().toString());

		return pojo;
	}

	public List<PojoNotice> getNotices() {
		List<PojoNotice> pojoNotices = new ArrayList<PojoNotice>();
		List<Notice> notices=nRepo.getAllNotices();
		Collections.sort(notices, new SortbyDate());
		Collections.reverse(notices);

		for (Notice notice : notices) {
			PojoNotice pojo = new PojoNotice();
			pojo.setAuthor(notice.getAuthor().getFullName());
			pojo.setCreationDate(notice.getCreationDate().toString());
			pojo.setDescription(notice.getDescription());
			pojo.setId(notice.getId().toString());
			pojo.setTitle(notice.getTitle());

			pojoNotices.add(pojo);
		}
		return pojoNotices;
	}

	class SortbyDate implements Comparator<Notice> {
		public int compare(Notice a, Notice b) 
	    { 
	        return a.getCreationDate().compareTo(b.getCreationDate());
	    }
	}

}
