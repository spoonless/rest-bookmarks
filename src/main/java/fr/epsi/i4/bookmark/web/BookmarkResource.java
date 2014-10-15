package fr.epsi.i4.bookmark.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import fr.epsi.i4.bookmark.Bookmark;
import fr.epsi.i4.bookmark.BookmarkRepository;
import fr.epsi.i4.bookmark.InvalidBookmarkException;

public class BookmarkResource {
	
	private final BookmarkRepository bookmarkRepository;
	private final String id;
	
	public BookmarkResource(BookmarkRepository bookmarkRepository, String id) {
		this.bookmarkRepository = bookmarkRepository;
		this.id = id;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response merge(@Context Request request, Bookmark bookmark) throws InvalidBookmarkException {
		if (bookmark == null) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		checkPreconditions(request, bookmarkRepository.get(id));
		
		bookmarkRepository.add(id, bookmark);
		return Response.noContent().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@Context Request request, @Context UriInfo uriInfo) {
		Bookmark bookmark = getBookmark();
		checkPreconditions(request, bookmark);

		BookmarkRepresentation bookmarkRepresentation = new BookmarkRepresentation(bookmark, uriInfo.getRequestUriBuilder());
		return Response.ok(bookmarkRepresentation)
				        .lastModified(bookmark.getLastModification())
				        .link(bookmarkRepresentation.getQrCodeLink().getHref(), "http://bookmarks.epsi.fr/qrcode")
				        .build();
	}

	@DELETE
	public Response delete() {
		bookmarkRepository.delete(id);
		return Response.noContent().build();
	}

	@GET
	@Path("qrcode")
	@Produces("image/png")
	public Response getQrCode() {
		Bookmark bookmark = getBookmark();
		return Response.ok(bookmark.getUrl()).lastModified(bookmark.getLastModification()).build();
	}

	@OPTIONS
	@Path("qrcode")
	public Response getQrCodeHead() {
		return Response.ok().header("Allow", "GET,OPTIONS,HEAD").build();
	}
	
	public Bookmark getBookmark() {
		Bookmark bookmark = bookmarkRepository.get(id);
		if (bookmark == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return bookmark;
	}

	private void checkPreconditions(Request request, Bookmark bookmark) {
		if (bookmark != null) {
			ResponseBuilder builder = request.evaluatePreconditions(bookmark.getLastModification());
			if (builder != null) {
				throw new WebApplicationException(builder.build());
			}
		}
	}

}
