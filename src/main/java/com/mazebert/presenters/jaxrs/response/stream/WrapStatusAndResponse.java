package com.mazebert.presenters.jaxrs.response.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WrapStatusAndResponse extends AbstractResponseStream {
    private final String field;

    public WrapStatusAndResponse(JsonFactory jsonFactory, Object response, String field) {
        super(jsonFactory, response);
        this.field = field;
    }

    @Override
    protected void write(JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("status", "ok");
        generator.writeObjectField(field, response);
        generator.writeEndObject();
    }
}
