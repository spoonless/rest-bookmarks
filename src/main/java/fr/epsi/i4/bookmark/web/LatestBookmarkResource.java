package fr.epsi.i4.bookmark.web;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class LatestBookmarkResource {

	private BookmarkResource bookmarkResource;
	private UriBuilder uriBuilder;

	public LatestBookmarkResource(BookmarkResource bookmarkResource, UriBuilder uriBuilder) {
		this.bookmarkResource = bookmarkResource;
		this.uriBuilder = uriBuilder;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get() {
		BookmarkResponse bookmarkResponse = new BookmarkResponse(bookmarkResource.getBookmark(), uriBuilder.clone());
		return Response.ok().header("Content-Location", uriBuilder.build()).entity(bookmarkResponse).build();
	}
}
