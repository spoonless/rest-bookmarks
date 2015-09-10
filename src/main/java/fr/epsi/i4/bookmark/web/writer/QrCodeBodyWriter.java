package fr.epsi.i4.bookmark.web.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

@Provider
@Produces("image/png")
public class QrCodeBodyWriter implements MessageBodyWriter<String> {

	@Override
	public long getSize(String s, Class<?> rawType, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> rawType, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return rawType == String.class && mediaType.getType().equals("image") && mediaType.getSubtype().equals("png");
	}

	@Override
	public void writeTo(String s, Class<?> rawType, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException {
		QRCode.from(s).to(ImageType.PNG).writeTo(entityStream);
	}

}
