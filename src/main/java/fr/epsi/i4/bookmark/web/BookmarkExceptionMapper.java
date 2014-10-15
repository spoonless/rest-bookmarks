package fr.epsi.i4.bookmark.web;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import fr.epsi.i4.bookmark.InvalidBookmarkException;

@Provider
public class BookmarkExceptionMapper implements ExceptionMapper<InvalidBookmarkException> {

	@Override
	public Response toResponse(InvalidBookmarkException e) {
		return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
	}

}
