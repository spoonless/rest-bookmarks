package fr.epsi.i4.bookmark;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bookmark")
public class Bookmark {

	private String name;
	private String description;
	private String url;

	public Bookmark() {
	}

	public Bookmark(String name, String description, String url) {
		this.name = name;
		this.description = description;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void validate() throws InvalidBookmarkException {
		if (Objects.toString(name, "").isEmpty()) {
			throw new InvalidBookmarkException("Bookmark name cannot be empty!");
		}
		if (Objects.toString(url, "").isEmpty()) {
			throw new InvalidBookmarkException("Bookmark url cannot be empty!");
		}
	}
}
