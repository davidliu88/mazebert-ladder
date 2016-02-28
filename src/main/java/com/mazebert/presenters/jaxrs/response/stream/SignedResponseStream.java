package com.mazebert.presenters.jaxrs.response.stream;

import com.mazebert.error.InternalServerError;
import com.mazebert.usecases.security.SignServerResponse;
import org.apache.commons.io.IOUtils;
import org.jusecase.UsecaseExecutor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class SignedResponseStream implements StreamingOutput {
    private String signature;
    private InputStream inputStream;

    public SignedResponseStream(UsecaseExecutor usecaseExecutor, StreamingOutput output) {
        wrapOutputToInputStream(output);
        createSignature(usecaseExecutor);
        resetInputStream();
    }

    private void resetInputStream() {
        try {
            inputStream.reset();
        } catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            throw new InternalServerError("Failed to reset input stream when signing response.", e);
        }
    }

    private void createSignature(UsecaseExecutor usecaseExecutor) {
        SignServerResponse.Request signRequest = new SignServerResponse.Request();
        signRequest.content = inputStream;

        SignServerResponse.Response signResponse = usecaseExecutor.execute(signRequest);
        signature = signResponse.signature;
    }

    private void wrapOutputToInputStream(StreamingOutput output) {
        ByteArrayOutputStream outputStream = writeToOutputStream(output);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        inputStream.mark(Integer.MAX_VALUE);
    }

    private ByteArrayOutputStream writeToOutputStream(StreamingOutput output) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            try {
                output.write(outputStream);
            } finally {
                outputStream.close();
            }
        } catch (IOException e) {
            throw new InternalServerError("Failed to write content to output stream while signing response.", e);
        }
        return outputStream;
    }

    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {
        InputStream signatureStream = a(inputStream().withString(signature));
        try {
            IOUtils.copy(signatureStream, output);
            IOUtils.copy(inputStream, output);
        } finally {
            IOUtils.closeQuietly(signatureStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(output);
        }
    }
}
