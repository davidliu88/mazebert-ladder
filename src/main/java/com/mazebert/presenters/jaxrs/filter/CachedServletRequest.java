package com.mazebert.presenters.jaxrs.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CachedServletRequest extends HttpServletRequestWrapper {
    private CachedInputStream cachedInputStream;

    public CachedServletRequest(HttpServletRequest request) {
        super(request);

        try {
            ServletInputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                cachedInputStream = new CachedInputStream(inputStream);
            }
        } catch (IOException e) {
            cachedInputStream = null;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedInputStream != null) {
            cachedInputStream.reset();
        }
        return cachedInputStream;
    }

    private class CachedInputStream extends ServletInputStream {
        private ServletInputStream originalStream;
        private ByteArrayInputStream cachedStream;

        public CachedInputStream(ServletInputStream inputStream) throws IOException {
            originalStream = inputStream;
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                IOUtils.copy(originalStream, os);
                cachedStream = new ByteArrayInputStream(os.toByteArray());
            }
        }

        public void reset() {
            cachedStream.reset();
        }

        @Override
        public boolean isFinished() {
            return originalStream.isFinished();
        }

        @Override
        public boolean isReady() {
            return originalStream.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            originalStream.setReadListener(readListener);
        }

        @Override
        public int read() throws IOException {
            return cachedStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return cachedStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return cachedStream.read(b, off, len);
        }

        @Override
        public void close() throws IOException {
            cachedStream.close();
            originalStream.close();
        }
    }
}
