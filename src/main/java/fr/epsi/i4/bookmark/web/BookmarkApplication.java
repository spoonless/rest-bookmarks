package fr.epsi.i4.bookmark.web;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import fr.epsi.i4.bookmark.web.writer.HalBodyWriter;
import fr.epsi.i4.bookmark.web.writer.QrCodeBodyWriter;

public class BookmarkApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(QrCodeBodyWriter.class);
		classes.add(BookmarkExceptionMapper.class);
		classes.add(HalBodyWriter.class);
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> objects = new HashSet<>();
		objects.add(new BookmarksResource());
		return objects;
	}

}
