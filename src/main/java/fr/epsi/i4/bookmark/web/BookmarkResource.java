package fr.epsi.i4.bookmark.web;

import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;

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
			throw new InvalidBookmarkException("No bookmark sent in request body!");
		}
		
		checkPreconditions(request, bookmarkRepository.get(id));
		
		boolean isCreated = bookmarkRepository.add(id, bookmark);
		return status(isCreated ? Status.CREATED : Status.NO_CONTENT).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(@Context Request request, @FormParam("name") String name, @FormParam("description") String description, @FormParam("url") String url) throws InvalidBookmarkException {
		return merge(request, new Bookmark(name, description, url));
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getWithoutLink(@Context Request request, @Context UriInfo uriInfo) {
		return get(request, uriInfo, false);
	}

	@GET
	@Produces({ RepresentationFactory.HAL_JSON, RepresentationFactory.HAL_XML })
	public Response get(@Context Request request, @Context UriInfo uriInfo) {
		return get(request, uriInfo, true);
	}

	private Response get(Request request, UriInfo uriInfo, boolean toHal) {
		Bookmark bookmark = getBookmark();
		checkPreconditions(request, bookmark);

		BookmarkRepresentation bookmarkRepresentation = new BookmarkRepresentation(bookmark, uriInfo.getRequestUriBuilder());
		return ok(toHal ? bookmarkRepresentation.toHal() : bookmarkRepresentation)
				.lastModified(bookmark.getLastModification())
				.header("Vary", "Accept")
				.tag(String.valueOf(bookmark.hashCode()))
				.links(bookmarkRepresentation.getQrCodeLink(), bookmarkRepresentation.getUrlLink(), bookmarkRepresentation.getCollectionLink())
				.build();
	}

	@DELETE
	public Response delete(@Context Request request) {
		checkPreconditions(request, bookmarkRepository.get(id));
		bookmarkRepository.delete(id);
		return noContent().build();
	}

	@GET
	@Path("qrcode")
	@Produces("image/png")
	public Response getQrCode(@Context Request request, @Context UriInfo uriInfo) {
		Bookmark bookmark = getBookmark();
		checkPreconditions(request, bookmarkRepository.get(id));
		return ok(bookmark.getUrl())
				.link(uriInfo.getAbsolutePath().resolve("."), "alternate")
				.lastModified(bookmark.getLastModification())
				.tag(String.valueOf(bookmark.hashCode()))
				.build();
	}

	@OPTIONS
	@Path("qrcode")
	public Response getQrCodeHead() {
		return noContent().allow("GET", "OPTIONS", "HEAD").build();
	}
	
	public Bookmark getBookmark() {
		Bookmark bookmark = bookmarkRepository.get(id);
		if (bookmark == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return bookmark;
	}

	public static void checkPreconditions(Request request, Bookmark bookmark) {
		ResponseBuilder builder = null;
		if (bookmark != null) {
			builder = request.evaluatePreconditions(bookmark.getLastModification(), new EntityTag(String.valueOf(bookmark.hashCode())));
		}
		else {
			builder = request.evaluatePreconditions();
		}

		if (builder != null) {
			throw new WebApplicationException(builder.build());
		}
	}

}
