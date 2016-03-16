package com.mazebert.presenters.jaxrs.response.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class MergeStatusWithResponse extends AbstractResponseStream {
    private static final byte[] prefix = "{\"status\":\"ok\"".getBytes(Charset.forName("UTF-8"));

    public MergeStatusWithResponse(JsonFactory jsonFactory, Object response) {
        super(jsonFactory, response);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        OutputStreamWrapper outputStreamWrapper = new OutputStreamWrapper(outputStream);

        if (response == null) {
            outputStream.write(prefix);
            outputStream.write('}');
        } else {
            outputStream.write(prefix);
            outputStreamWrapper.setInterceptNextCharacter(true);
            super.write(outputStreamWrapper);
        }
    }

    @Override
    protected void write(JsonGenerator generator) throws IOException {
        generator.writeObject(response);
    }

    private static class OutputStreamWrapper extends OutputStream {
        private final OutputStream os;
        private boolean interceptNextCharacter;
        private int interceptedCharacter;

        private OutputStreamWrapper(OutputStream os) {
            this.os = os;
        }

        @Override
        public void write(int b) throws IOException {
            if (interceptNextCharacter) {
                if (interceptedCharacter == 0) {
                    interceptedCharacter = b;
                } else {
                    if ('}' == b) {
                        os.write('}');
                    } else {
                        os.write(',');
                        os.write(b);
                    }
                    interceptNextCharacter = false;
                }
            } else {
                os.write(b);
            }
        }

        @Override
        public void write(byte[] b) throws IOException {
            if (interceptNextCharacter) {
                super.write(b);
            } else {
                os.write(b);
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (interceptNextCharacter) {
                super.write(b, off, len);
            } else {
                os.write(b);
            }
        }

        @Override
        public void flush() throws IOException {
            os.flush();
        }

        @Override
        public void close() throws IOException {
            os.close();
        }

        public void setInterceptNextCharacter(boolean interceptNextCharacter) {
            this.interceptNextCharacter = interceptNextCharacter;
        }
    }
}
