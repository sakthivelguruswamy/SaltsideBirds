package com.birds;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

public class BirdConverter {
	public static final String FIELD_ID = "_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_FAMILY = "family";
	public static final String FIELD_CONTINENTS = "continents";
	public static final String FIELD_ADDED = "added";
	public static final String FIELD_VISIBLE = "visible";

	public static DBObject toDBObject(Bird bird) {
		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append(FIELD_NAME, bird.getName())
				.append(FIELD_FAMILY, bird.getFamily())
				.append(FIELD_CONTINENTS, bird.getContinents())
				.append(FIELD_ADDED, bird.getAdded())
				.append(FIELD_VISIBLE, bird.isVisible());
		if (bird.getId() != null) {
			builder = builder.append(FIELD_ID, new ObjectId(bird.getId()));
		}
		return builder.get();
	}

	public static Bird toBird(DBObject doc) {
		Bird bird = new Bird();
		bird.setName((String) doc.get(FIELD_NAME));
		bird.setFamily((String) doc.get(FIELD_FAMILY));
		BasicDBList list = (BasicDBList) doc.get(FIELD_CONTINENTS);
		String[] continents = list.toArray(new String[list.size()]);
		bird.setContinents(continents);
		bird.setAdded((String) doc.get(FIELD_ADDED));
		bird.setVisible((Boolean) doc.get(FIELD_VISIBLE));
		ObjectId id = (ObjectId) doc.get(FIELD_ID);
		bird.setId(id.toString());
		return bird;
	}

	public static String convertToJsonString(Object object){
    	String jsonInString = "";
    	try {
			jsonInString = new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return jsonInString;
	}

}
