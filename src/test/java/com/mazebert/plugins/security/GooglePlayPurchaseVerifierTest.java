package com.mazebert.plugins.security;

import com.mazebert.plugins.system.mocks.LoggerMock;
import org.junit.Test;

import java.util.logging.Level;

public class GooglePlayPurchaseVerifierTest {
    private LoggerMock logger = new LoggerMock();
    private GooglePlayPurchaseVerifier purchaseVerifier = new GooglePlayPurchaseVerifier(logger.getLogger());

    @Test
    public void unexpectedException() {
        purchaseVerifier.isSignatureValid(null, "1234");

        logger.thenMessageIsLogged(Level.INFO, "Unexpected exception when verifying GooglePlay signature. (Exception Message: null)");
        logger.thenMessageIsLogged(Level.INFO, "Data: null");
        logger.thenMessageIsLogged(Level.INFO, "Signature: 1234");
    }
}