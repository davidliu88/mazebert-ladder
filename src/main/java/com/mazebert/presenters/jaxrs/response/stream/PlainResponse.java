package com.mazebert.presenters.jaxrs.response.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class PlainResponse extends AbstractResponseStream {

    public PlainResponse(JsonFactory jsonFactory, Object response) {
        super(jsonFactory, response);
    }

    @Override
    protected void write(JsonGenerator generator) throws IOException {
        generator.writeObject(response);
    }
}
