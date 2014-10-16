package fr.epsi.i4.bookmark.web;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import fr.epsi.i4.bookmark.Bookmark;

public class LatestBookmarkResource {

	private Bookmark bookmark;
	private UriBuilder uriBuilder;

	public LatestBookmarkResource(Bookmark bookmark, UriBuilder uriBuilder) {
		this.bookmark = bookmark;
		this.uriBuilder = uriBuilder;
	}
	
	@PUT
	public Response redirectPut() {
		return Response.temporaryRedirect(uriBuilder.build()).build();
	}

	@DELETE
	public Response redirectDelete() {
		return Response.temporaryRedirect(uriBuilder.build()).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get() {
		BookmarkRepresentation bookmarkRepresentation = new BookmarkRepresentation(bookmark, uriBuilder.clone());
		return Response.ok().header("Content-Location", uriBuilder.build()).entity(bookmarkRepresentation).lastModified(bookmark.getLastModification()).build();
	}
}
