package com.mindia.avisosnick.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mindia.avisosnick.persistence.model.Notice;

@Repository
public class NoticeRepository {
	@Autowired
	private MongoTemplate mongoTemplate;

	public void createNotice(Notice notice) {
		try {
			mongoTemplate.save(notice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Notice getNoticeById(ObjectId id) {
		Query query = new Query(where("id").is(id));

		return mongoTemplate.findOne(query, Notice.class);
	}
}
