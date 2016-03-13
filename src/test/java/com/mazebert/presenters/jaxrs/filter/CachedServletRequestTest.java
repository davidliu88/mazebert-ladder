package com.mazebert.presenters.jaxrs.filter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CachedServletRequestTest {

    @Test
    public void nullStream() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getInputStream()).thenReturn(null);

        CachedServletRequest servletRequest = new CachedServletRequest(request);

        assertNull(servletRequest.getInputStream());
    }

    @Test
    public void streamContentCanBeReadTwice() throws Exception {
        try (ServletInputStream servletInputStream = new ServletInputStreamMock(a(inputStream().withString("TestContent")))) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getInputStream()).thenReturn(servletInputStream);

            CachedServletRequest servletRequest = new CachedServletRequest(request);

            assertEquals("TestContent", IOUtils.toString(servletRequest.getInputStream()));
            assertEquals("TestContent", IOUtils.toString(servletRequest.getInputStream()));
        }
    }

    private class ServletInputStreamMock extends ServletInputStream {
        private final InputStream inputStream;

        private ServletInputStreamMock(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}