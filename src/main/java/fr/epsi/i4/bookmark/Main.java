package fr.epsi.i4.bookmark;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.simple.SimpleContainerFactory;
import org.glassfish.jersey.simple.SimpleContainerProvider;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

import fr.epsi.i4.bookmark.web.BookmarkApplication;

public class Main implements Container {

	private Container container;
	
	public Main() {
		SimpleContainerProvider provider = new SimpleContainerProvider();
		container = provider.createContainer(Container.class, new BookmarkApplication());
	}

	public static void main(String[] args) throws URISyntaxException {
		String port = Objects.toString(System.getenv("PORT"), "8080");
        Logger.getLogger("bookmarks").log(Level.INFO, "Starting web services on port " + port);
		SimpleContainerFactory.create(new URI("http://rest-bookmarks.herokuapp.com:" + 8080), ResourceConfig.forApplication(new BookmarkApplication()));
	}

	@Override
	public void handle(Request request, Response response) {
		if (request.getPath().getPath().equals("/")) {
			if (request.getMethod().equals("GET")) {
				response.setContentType("text/html");
				try(PrintStream printStream = response.getPrintStream()) {
					printStream.println("Hello");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				response.setCode(405);
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		else {
			container.handle(request, response);
		}
		
	}
}

