package fr.epsi.i4.bookmark.web;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;

public class Link {

	private String href;
	private String rel;

	public Link() {
	}

	public Link(URI href, String rel) {
		this.href = href.toASCIIString();
		this.setRel(rel);
	}

	@XmlAttribute
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@XmlAttribute
	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

}