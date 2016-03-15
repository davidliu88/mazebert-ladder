package com.mazebert.plugins.system.mocks;

import org.mockito.Mockito;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class LoggerMock {
    private Logger logger = mock(Logger.class);

    public Logger getLogger() {
        return logger;
    }

    public void reset() {
        Mockito.reset(logger);
    }

    public void thenMessageIsLogged(Level level, String message) {
        verify(logger).log(level, message);
    }

    public void thenMessageIsLogged(Level level, String message, Throwable throwable) {
        verify(logger).log(level, message, throwable);
    }

    public void thenNoMessageIsLogged() {
        verify(logger, never()).log(any(Level.class), anyString());
        verify(logger, never()).log(any(Level.class), anyString(), any(Throwable.class));
    }
}
