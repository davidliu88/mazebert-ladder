package com.mazebert.plugins.system.mocks;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoggerMock {
    private Logger logger = mock(Logger.class);

    public Logger getLogger() {
        return logger;
    }

    public void thenMessageIsLogged(Level level, String message) {
        verify(logger).log(level, message);
    }

    public void thenMessageIsLogged(Level level, String message, Throwable throwable) {
        verify(logger).log(level, message, throwable);
    }
}
