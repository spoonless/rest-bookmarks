package fr.epsi.i4.bookmark.web;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;

import fr.epsi.i4.bookmark.Bookmark;
import fr.epsi.i4.bookmark.BookmarkRepository;
import fr.epsi.i4.bookmark.InvalidBookmarkException;

@Path("bookmarks")
public class BookmarksResource {

	private BookmarkRepository bookmarkRepository = new BookmarkRepository();

	@Context
	private UriInfo uriInfo;
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(Bookmark bookmark) throws InvalidBookmarkException {
		if (bookmark == null) {
			throw new InvalidBookmarkException("No bookmark sent in request body!");
		}
		String id = UUID.randomUUID().toString();
		bookmarkRepository.add(id, bookmark);
		URI resourceLocation = getUriBuilderFromId(id).build();
		return Response.created(resourceLocation).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(@FormParam("name") String name, @FormParam("description") String description, @FormParam("url") String url) throws InvalidBookmarkException {
		Bookmark bookmark = new Bookmark(name, description, url);
		return add(bookmark);
	}
	
	@Path("latest")
	public LatestBookmarkResource createLatestBookmarkResource() {
		String id = bookmarkRepository.getLatestId();
		if (id == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return new LatestBookmarkResource(new BookmarkResource(bookmarkRepository, id).getBookmark(), getUriBuilderFromId(id));
	}
	
	@Path("{id}")
	public BookmarkResource createBookmarkResource(@PathParam("id") String id) {
		return new BookmarkResource(bookmarkRepository, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getWithoutLink(@QueryParam("startIndex") @DefaultValue("0") int startIndex, @QueryParam("itemCount") @DefaultValue("10") int itemCount) {
		return get(startIndex, itemCount, false);
	}

	@GET
	@Produces({ RepresentationFactory.HAL_JSON, RepresentationFactory.HAL_XML })
	public Response get(@QueryParam("startIndex") @DefaultValue("0") int startIndex, @QueryParam("itemCount") @DefaultValue("10") int itemCount) {
		return get(startIndex, itemCount, true);
	}

	private Response get(int startIndex, int itemCount, boolean hal) {
		if (startIndex < 0) {
			throwBadRequest("startIndex query parameter must be zero or a positive integer!");
		}
		if (itemCount <= 0) {
			throwBadRequest("itemCount query parameter must be positive!");
		}
		
		BookmarksRepresentation bookmarksRepresentation = new BookmarksRepresentation();
		bookmarksRepresentation.setStartIndex(startIndex);
		bookmarksRepresentation.setItemCount(itemCount);

		UriBuilder uriBuilder = getUriBuilderFromId("{id}");
		
		for (Map.Entry<String, Bookmark> entry : bookmarkRepository.getBookmarkEntries(startIndex, itemCount)) {
			bookmarksRepresentation.addBookmark(uriBuilder.build(entry.getKey()), entry.getValue());
		}

		bookmarksRepresentation.addNavigationLinks(uriInfo.getRequestUriBuilder());

		ResponseBuilder responseBuilder = Response.ok(hal ? bookmarksRepresentation.toHal() : bookmarksRepresentation);
		responseBuilder.cacheControl(createCacheControl());
		for (Link link : bookmarksRepresentation.getNavigationLinks()) {
			responseBuilder.links(link);
		}
		return responseBuilder.build();
	}

	private CacheControl createCacheControl() {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
		return cacheControl;
	}

	private void throwBadRequest(String message) {
		throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(message).build());
	}

	private UriBuilder getUriBuilderFromId(String id) {
		return uriInfo.getBaseUriBuilder().path(this.getClass()).path(id);
	}

}
