package fr.epsi.i4.bookmark.web;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import fr.epsi.i4.bookmark.Bookmark;

@XmlRootElement(name = "bookmark")
@XmlType(propOrder = { "name", "url", "description", "qrCodeLink" })
public class BookmarkRepresentation {

	private Bookmark bookmark;
	private Link qrCodeLink;

	public BookmarkRepresentation() {
	}

	public BookmarkRepresentation(Bookmark bookmark, UriBuilder uriBuilder) {
		this.bookmark = bookmark;
		URI qrcodeUri = uriBuilder.path("qrcode").build();
		this.qrCodeLink = new Link(qrcodeUri, "qrcode");
	}

	@XmlElement
	public String getName() {
		return bookmark.getName();
	}

	@XmlElement
	public String getDescription() {
		return bookmark.getDescription();
	}

	@XmlElement
	public String getUrl() {
		return bookmark.getUrl();
	}

	@XmlElement(name = "link")
	public Link getQrCodeLink() {
		return qrCodeLink;
	}

}
