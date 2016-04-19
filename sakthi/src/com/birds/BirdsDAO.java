package com.birds;

import static com.birds.BirdConverter.FIELD_ID;
import static com.birds.BirdConverter.FIELD_VISIBLE;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

public class BirdsDAO {

	private DBCollection dbColl;

	public BirdsDAO() throws UnknownHostException {
		Mongo mongo = new Mongo("localhost", 27017);
		this.dbColl = mongo.getDB("sakthivel").getCollection("birds");
	}

	public static void main(String[] args) throws Exception {
		BirdsDAO dao = new BirdsDAO();
		for (int i=1; i<=10; i++) {
			Bird bird = new Bird();
			bird.setName("Peacock"+i);
			bird.setFamily("peac"+i);
			String added = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
			bird.setAdded(added);
			bird.setContinents(new String[] {"India", "Cambodia"});
			if (i%2 == 0)
				bird.setVisible(true);
			System.out.print (dao.createBird(bird) + "  ");
			System.out.println(bird.getId());
		}
		System.out.println(dao.getBirds());
		List<Bird> birdList = dao.getAllBirds();
		System.out.println(new ObjectMapper().writeValueAsString(birdList));
		for (Bird bird:birdList){
			System.out.println(bird.getName() + "   " + bird.getId());
			System.out.println(" ----" + dao.getBird(bird.getId()).getName());
			//dao.deleteBird(bird);
		}

		//System.out.println(dao.getBird("abcd"));
		//System.out.println(dao.deleteBird("abcd"));
	}

	public boolean createBird(Bird bird) {
		DBObject doc = BirdConverter.toDBObject(bird);
		WriteResult wr = this.dbColl.insert(doc);
		if (wr.getLastError().isEmpty()) {
			ObjectId id = (ObjectId) doc.get("_id");
			bird.setId(id.toString());
			return true;
		}
		return false;
	}

	public List<Bird> getAllBirds() {
		List<Bird> data = new ArrayList<Bird>();
		DBCursor cursor = dbColl.find();
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			Bird p = BirdConverter.toBird(doc);
			data.add(p);
		}
		return data;
	}

	/**
	 * @return id list of all birds with visible field as true
	 */
	public List<String> getBirds() {
		List<String> data = new ArrayList<String>();
		DBObject query = BasicDBObjectBuilder.start()
				.append(FIELD_VISIBLE, Boolean.TRUE).get();
		DBObject fields = BasicDBObjectBuilder.start()
				.append(FIELD_ID, Boolean.TRUE).get();
		DBCursor cursor = dbColl.find(query, fields);
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			ObjectId id = (ObjectId) doc.get(FIELD_ID);
			data.add(id.toString());
		}
		return data;
	}

	public boolean deleteBird(String birdId) {
		DBObject query = null;
		try {
			query = BasicDBObjectBuilder.start()
					.append(FIELD_ID, new ObjectId(birdId)).get();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			//when birdId is an invalid objectid
			return false;
		}
		WriteResult wr = this.dbColl.remove(query);
		return (wr.getN() > 0);
	}

	public boolean deleteBird(Bird bird) {
		DBObject query = null;
		try {
			query = BasicDBObjectBuilder.start()
					.append(FIELD_ID, new ObjectId(bird.getId())).get();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			//when birdId is an invalid objectid
			return false;
		}
		WriteResult wr = this.dbColl.remove(query);
		return (wr.getN() > 0);
	}

	public Bird getBird(String birdId) {
		Bird bird = null;
		DBObject data = null;
		DBObject query;
		try {
			query = BasicDBObjectBuilder.start()
					.append(FIELD_ID, new ObjectId(birdId)).get();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			//when birdId is an invalid objectid
			return null;
		}
		data = this.dbColl.findOne(query);
		if (data != null) {
			bird = BirdConverter.toBird(data);
		}
		return bird;
	}

}
