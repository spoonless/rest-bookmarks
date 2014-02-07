package fr.epsi.i4.bookmark;

import java.net.URI;
import java.util.Objects;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.simple.SimpleContainerFactory;

import fr.epsi.i4.bookmark.web.BookmarkApplication;

public class Main {

	public static void main(String[] args) throws Exception {
		String port = Objects.toString(System.getenv("PORT"), "8080");
	    SimpleContainerFactory.create(new URI("http://localhost:" + port), new ApplicationHandler(new BookmarkApplication()));
	}

}
