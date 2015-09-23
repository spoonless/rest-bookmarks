package fr.epsi.i4.bookmark.web;

import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.temporaryRedirect;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;

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
		return temporaryRedirect(uriBuilder.build()).build();
	}

	@DELETE
	public Response redirectDelete() {
		return temporaryRedirect(uriBuilder.build()).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getWithoutLinks(@Context Request request) {
		return get(request, false);
	}

	@GET
	@Produces({ RepresentationFactory.HAL_JSON, RepresentationFactory.HAL_XML })
	public Response get(@Context Request request) {
		return get(request, true);
	}

	private Response get(Request request, boolean toHal) {
		BookmarkResource.checkPreconditions(request, bookmark);
		BookmarkRepresentation bookmarkRepresentation = new BookmarkRepresentation(bookmark, uriBuilder);
		
		return ok()
				.header("Content-Location", uriBuilder.build())
				.header("Vary", "Accept")
				.cacheControl(createCacheControl())
				.tag(String.valueOf(bookmark.hashCode()))
				.entity(toHal ? bookmarkRepresentation.toHal() : bookmarkRepresentation)
				.lastModified(bookmark.getLastModification()).build();
	}

	private CacheControl createCacheControl() {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
		return cacheControl;
	}
}
