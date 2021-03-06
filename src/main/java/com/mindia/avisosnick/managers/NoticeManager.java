package com.mindia.avisosnick.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void createNotice(List<String> types, List<String> mails, boolean send, String title, String description, User author, Map<String, String> dataMap) {
		PojoUser pUser = new PojoUser();
		pUser.setMail(author.getEmail());
		pUser.setFullName(author.getFullName());
		if (mails == (null) || mails.isEmpty()) {
			mails = new ArrayList<String>();
			for (String string : types) {
				for (PojoUser user : uManager.getUsersByType(string)) {
					mails.add(user.getMail());
				}
			}
		}

		Notice notice = new Notice(title, description, pUser, mails,send);
		notice.setId(new ObjectId());
		
		
		dataMap.putIfAbsent("id_notice", notice.getId().toString());
		if (notice.isSend()) {
			List<User> usersToSend = uManager.getAllUsersByEmails(mails);
			List<String> tokens = new ArrayList<String>();
			for (User user : usersToSend) {
//				No importa si no tienen asignado un mobile token
//				if (user.getUniqueMobileToken().equals(null)) {
//					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario " + user.getFullName()
//							+ " no posee asignado un token al cual enviar notificaciones.");
//				}
				if(user.getUniqueMobileToken() != null && !user.getUniqueMobileToken().isEmpty()) {
					tokens.add(user.getUniqueMobileToken());
				}
			}
			if(!tokens.isEmpty()) {
				MulticastMessage notification = MulticastMessage.builder()
						
						// Data needed by the app
						.putAllData(dataMap)
						
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
		}
		
		nRepo.createNotice(notice);
	}

	public void markAsRead(String mail, ObjectId idNotice) {
		Notice notice = nRepo.getNoticeById(idNotice);
		if(!notice.getReadedByUsers().contains(mail)) {
			notice.readedByUser(mail);
			nRepo.createNotice(notice);
		}

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
		List<PojoNotice> additionalNotices = new ArrayList<PojoNotice>();
		List<Notice> notices = nRepo.getAllNotices();
		Collections.sort(notices, new SortbyDate());
		Collections.reverse(notices);
		for (Notice notice : notices) {
			for (String userMail : notice.getNotifiedUsers()) {
				if (userMail.equals(mail)) {
					PojoNotice pojo = new PojoNotice();
					pojo.setId(notice.getId().toString());
					pojo.setTitle(notice.getTitle());
					pojo.setDescription(notice.getDescription());
					pojo.setAuthor(notice.getAuthor().getFullName());
					pojo.setMails(notice.getNotifiedUsers());

					Date dateNotice = notice.getCreationDate();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

					pojo.setCreationDate(sdf.format(dateNotice));
					if(alreadyReaded(notice, userMail)) {
						pojo.setReaded(true);
						additionalNotices.add(pojo);
					}else {
						pojo.setReaded(false);
						noticesForUser.add(pojo);
					}
				}
			}
		}
		noticesForUser.addAll(additionalNotices);
		return noticesForUser;
	}
	private boolean alreadyReaded(Notice notice, String userMail) {
		if(notice.getReadedByUsers().contains(userMail)) {
			return true;
		}return false;
	}

	public PojoNotice getNotice(String id, String userMail) {
		Notice notice = nRepo.getNoticeById(new ObjectId(id));
		
		PojoNotice pojo = new PojoNotice();
		pojo.setId(notice.getId().toString());
		pojo.setTitle(notice.getTitle());
		pojo.setDescription(notice.getDescription());
		pojo.setAuthor(notice.getAuthor().getFullName());
		pojo.setMails(notice.getNotifiedUsers());

		Date dateNotice = notice.getCreationDate();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

		pojo.setCreationDate(sdf.format(dateNotice));
		
		//TODO: refactorizar DIEGO!!!!
		if(notice.getNotifiedUsers().contains(userMail)) {
			if(alreadyReaded(notice, userMail)) {
				pojo.setReaded(true);
			}else {
				pojo.setReaded(false);
			}
		}else {
			pojo.setReaded(true);
		}

		return pojo;
	}

	public List<PojoNotice> getNotices() {
		List<PojoNotice> pojoNotices = new ArrayList<PojoNotice>();
		List<Notice> notices = nRepo.getAllNotices();
		Collections.sort(notices, new SortbyDate());
		Collections.reverse(notices);

		for (Notice notice : notices) {
			PojoNotice pojo = new PojoNotice();
			pojo.setAuthor(notice.getAuthor().getFullName());
			pojo.setDescription(notice.getDescription());
			pojo.setId(notice.getId().toString());
			pojo.setTitle(notice.getTitle());
			pojo.setMails(notice.getNotifiedUsers());
			
			Date dateNotice = notice.getCreationDate();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

			pojo.setCreationDate(sdf.format(dateNotice));

			pojoNotices.add(pojo);
		}
		return pojoNotices;
	}

	class SortbyDate implements Comparator<Notice> {
		public int compare(Notice a, Notice b) {
			return a.getCreationDate().compareTo(b.getCreationDate());
		}
	}

}
