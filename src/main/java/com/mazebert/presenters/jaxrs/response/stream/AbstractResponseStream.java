package com.mazebert.presenters.jaxrs.response.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractResponseStream implements StreamingOutput {
    protected final JsonFactory jsonFactory;
    protected final Object response;

    public AbstractResponseStream(JsonFactory jsonFactory, Object response) {
        this.jsonFactory = jsonFactory;
        this.response = response;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        try (JsonGenerator generator = jsonFactory.createGenerator(outputStream)) {
            write(generator);
            generator.flush();
        }
    }

    protected abstract void write(JsonGenerator generator) throws IOException;
}
