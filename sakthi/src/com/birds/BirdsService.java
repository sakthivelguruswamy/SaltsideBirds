package com.birds;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class BirdsService {

	@POST
	@Path("/birds")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addBird(Bird bird) {
		if (bird.getName() == null
				|| bird.getContinents() == null || bird.getContinents().length == 0
				|| bird.getFamily() == null) {
			//missing mandatory fields
			return Response.status(400).build();
		}
		String added = new SimpleDateFormat("YYYY-MM-DD").format(new Date());
		bird.setAdded(added);
		boolean created = getBirdsDAO().createBird(bird);
		if (created) {
			String json = BirdConverter.convertToJsonString(bird);
			System.out.println("Bird added Successfully with id="+bird.getId());
			return Response.status(201).entity(json).build();
		}
		//error
		return Response.status(500).build();
	}

    @GET
    @Path("/birds")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBirds() {
    	try {
			//gets only id field of birds with visible flag as true
			List<String> birds = getBirdsDAO().getBirds();
			String json = BirdConverter.convertToJsonString(birds);
			return Response.status(200).entity(json).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//error
		return Response.status(500).build();
    }

    @GET
    @Path("/birds/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBird(@PathParam("id") final String birdId) {
    	Bird bird = getBirdsDAO().getBird(birdId);
    	if (bird == null) {
    		return Response.status(404).build();
    	}
    	if (bird.isVisible()) {
    		String json = BirdConverter.convertToJsonString(bird);
    		return Response.status(200).entity(json).build();
    	}
        return Response.status(200).build();
    }

    @DELETE
    @Path("/birds/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBird(@PathParam("id") final String birdId) {
    	if ( getBirdsDAO().deleteBird(birdId) ) {
    		return Response.status(200).build();
    	}
    	return Response.status(404).build();
    }

    private BirdsDAO getBirdsDAO() {
		try {
			BirdsDAO birdsDAO = new BirdsDAO();
			return birdsDAO;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

}