package fr.epsi.i4.bookmark.web;

import java.net.URI;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.theoryinpractise.halbuilder.api.Representable;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;

import fr.epsi.i4.bookmark.Bookmark;

@XmlRootElement(name = "bookmark")
@XmlType(propOrder = { "name", "url", "description" })
public class BookmarkRepresentation implements Representable {

	private Bookmark bookmark;
	private Link qrCodeLink;
	private Link collectionLink;
	private URI selfUri;
	private Link urlLink;

	public BookmarkRepresentation() {
	}

	public BookmarkRepresentation(Bookmark bookmark, UriBuilder uriBuilder) {
		this.bookmark = bookmark;
		selfUri = uriBuilder.build();
		URI qrcodeUri = uriBuilder.clone().path("qrcode").build();
		this.qrCodeLink = Link.fromUri(qrcodeUri).rel("http://bookmarks.epsi.fr/rels/qrcode").title("QR code").type("image/png").build();
		this.collectionLink = Link.fromUri(uriBuilder.clone().path("..").build()).rel("collection").build();
		this.urlLink = Link.fromUri(bookmark.getUrl()).rel("related").title("Bookmarked link").build();
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

	@XmlTransient
	public Link getQrCodeLink() {
		return qrCodeLink;
	}

	@XmlTransient
	public Link getUrlLink() {
		return urlLink;
	}
	
	@XmlTransient
	public URI getSelfUri() {
		return selfUri;
	}
	
	@XmlTransient
	public Link getCollectionLink() {
		return collectionLink;
	}

	@Override
	public void representResource(Representation resource) {
		resource.withNamespace("bk", "http://bookmarks.epsi.fr/rels/{rel}");
		resource.withLink(urlLink.getRel(), urlLink.getUri());
		resource.withLink("bk:qrcode", qrCodeLink.getUri());
		resource.withProperty("name", bookmark.getName());
		resource.withProperty("description", bookmark.getDescription());
		resource.withProperty("url", bookmark.getUrl());
	}
	
	public Representation toHal() {
		return new StandardRepresentationFactory().newRepresentation(selfUri).withRepresentable(this);
	}
	
}
