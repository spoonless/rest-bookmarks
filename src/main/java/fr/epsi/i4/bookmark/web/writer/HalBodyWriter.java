package fr.epsi.i4.bookmark.web.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;

@Provider
@Produces({RepresentationFactory.HAL_XML, RepresentationFactory.HAL_JSON})
public class HalBodyWriter implements MessageBodyWriter<ReadableRepresentation> {

	public static final MediaType HAL_JSON_TYPE = MediaType.valueOf(RepresentationFactory.HAL_JSON);
	public static final MediaType HAL_XML_TYPE = MediaType.valueOf(RepresentationFactory.HAL_XML);
    
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return ReadableRepresentation.class.isAssignableFrom(aClass) && isSupported(mediaType);
    }

	private boolean isSupported(MediaType mediaType) {
		return mediaType.isCompatible(HAL_JSON_TYPE) || mediaType.isCompatible(HAL_XML_TYPE);
	}

    @Override
    public long getSize(ReadableRepresentation representation, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return representation.toString(mediaType.toString()).length();
    }

    @Override
    public void writeTo(ReadableRepresentation representation, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        representation.toString(mediaType.toString(), new OutputStreamWriter(outputStream, Charsets.UTF_8), RepresentationFactory.PRETTY_PRINT);
    }
}