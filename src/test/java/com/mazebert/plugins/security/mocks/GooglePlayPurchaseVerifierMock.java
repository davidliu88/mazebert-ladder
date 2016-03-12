package com.mazebert.plugins.security.mocks;

import com.mazebert.plugins.security.GooglePlayPurchaseVerifier;
import com.mazebert.plugins.system.mocks.LoggerMock;

public class GooglePlayPurchaseVerifierMock extends GooglePlayPurchaseVerifier {
    private boolean fakeVerification = true;

    public GooglePlayPurchaseVerifierMock() {
        super(new LoggerMock().getLogger());
    }

    @Override
    public boolean isSignatureValid(String data, String signature) {
        if (fakeVerification) {
            return true;
        }
        return super.isSignatureValid(data, signature);
    }

    public void givenRealVerificationIsDone() {
        fakeVerification = false;
    }
}
