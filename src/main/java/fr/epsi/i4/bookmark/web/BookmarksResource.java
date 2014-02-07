package fr.epsi.i4.bookmark.web;

import java.net.URI;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import fr.epsi.i4.bookmark.Bookmark;
import fr.epsi.i4.bookmark.BookmarkRepository;

@Path("bookmarks")
public class BookmarksResource {

	private BookmarkRepository bookmarkRepository = new BookmarkRepository();

	@Context
	private UriInfo uriInfo;
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(Bookmark bookmark) {
		String id = UUID.randomUUID().toString();
		new BookmarkResource(bookmarkRepository, id).merge(bookmark);
		URI resourceLocation = getUriBuilderFromId(id).build();
		return Response.created(resourceLocation).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(@FormParam("name") String name, @FormParam("description") String description, @FormParam("url") String url) {
		Bookmark bookmark = new Bookmark(name, description, url);
		return add(bookmark);
	}
	
	@Path("latest")
	public LatestBookmarkResource createLatestBookmarkResource() {
		String id = bookmarkRepository.getLatestId();
		if (id == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return new LatestBookmarkResource(new BookmarkResource(bookmarkRepository, id), getUriBuilderFromId(id));
	}
	
	@Path("{id}")
	public BookmarkResource createBookmarkResource(@PathParam("id") String id) {
		return new BookmarkResource(bookmarkRepository, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public BookmarksResponse get(@QueryParam("startIndex") @DefaultValue("0") int startIndex, @QueryParam("itemCount") @DefaultValue("10") int itemCount) {
		if (startIndex < 0) {
			throwBadRequest("startIndex query parameter must be zero or a positive integer!");
		}
		if (itemCount <= 0) {
			throwBadRequest("itemCount query parameter must be positive!");
		}
		
		BookmarksResponse bookmarksResponse = new BookmarksResponse();
		bookmarksResponse.setStartIndex(startIndex);
		bookmarksResponse.setItemCount(itemCount);

		UriBuilder uriBuilder = getUriBuilderFromId("{id}");

		for (String id : bookmarkRepository.getIds(startIndex, itemCount)) {
			URI uri = uriBuilder.build(id);
			bookmarksResponse.addBookmarkLink(uri);
		}

		bookmarksResponse.addNavigationLinks(uriInfo.getRequestUriBuilder());

		return bookmarksResponse;
	}

	private void throwBadRequest(String message) {
		throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(message).build());
	}

	private UriBuilder getUriBuilderFromId(String id) {
		return uriInfo.getBaseUriBuilder().path(this.getClass()).path(id);
	}

}
