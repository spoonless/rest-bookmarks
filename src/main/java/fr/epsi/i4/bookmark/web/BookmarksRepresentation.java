package fr.epsi.i4.bookmark.web;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.theoryinpractise.halbuilder.api.Representable;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;

import fr.epsi.i4.bookmark.Bookmark;

@XmlRootElement(name = "bookmarks")
public class BookmarksRepresentation implements Representable {

	private List<BookmarkRepresentation> bookmarks = new ArrayList<>();
	private final List<Link> navigationLinks = new ArrayList<>();
	private int startIndex;
	private int itemCount;

	@XmlElement(name = "bookmark")
	public List<String> getItems() {
		return bookmarks.stream().map(l -> l.getSelfUri().toASCIIString()).collect(Collectors.toList());
	}

	@XmlTransient
	public List<Link> getNavigationLinks() {
		return navigationLinks;
	}

	public void addBookmark(URI uri, Bookmark bookmark) {
		bookmarks.add(new BookmarkRepresentation(bookmark, UriBuilder.fromUri(uri)));
	}
	
	@XmlAttribute
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@XmlAttribute
	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public void addNavigationLinks(UriBuilder uriBuilder) {
		UriBuilder navUriBuilder = uriBuilder.clone().replaceQueryParam("startIndex", "{startIndex}");
		if (this.startIndex > 0) {
			int previousIndex = Math.max(0, this.startIndex - this.itemCount);
			this.navigationLinks.add(Link.fromUri(navUriBuilder.build(previousIndex)).rel("prev").build());
		}
		if (!this.bookmarks.isEmpty() && this.bookmarks.size() == this.itemCount) {
			this.navigationLinks.add(Link.fromUri(navUriBuilder.build(this.startIndex + this.itemCount)).rel("next").build());
		}
		this.navigationLinks.add(Link.fromUri(uriBuilder.path("latest").replaceQuery("").build()).rel("current").build());
	}

	@Override
	public void representResource(Representation resource) {
		for (Link link : navigationLinks) {
			resource.withLink(link.getRel(), link.getUri());
		}
		resource.withProperty("startIndex", startIndex);
		resource.withProperty("itemCount", itemCount);
		StandardRepresentationFactory representationFactory = new StandardRepresentationFactory();
		for (BookmarkRepresentation bookmark : bookmarks) {
			resource.withRepresentation("item", 
					representationFactory.newRepresentation(bookmark.getSelfUri())
										.withLink(bookmark.getUrlLink().getRel(), bookmark.getUrlLink().getUri())
										.withProperty("name", bookmark.getName()));
		}
	}
	
	public Representation toHal() {
		return new StandardRepresentationFactory().newRepresentation().withRepresentable(this);
	}
}
