package fr.epsi.i4.bookmark;

import java.util.Date;
import java.util.Objects;

public class Bookmark {

	private String name;
	private String description;
	private String url;
	private Date lastModification;

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

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.description, this.name, this.url);
	}
}
