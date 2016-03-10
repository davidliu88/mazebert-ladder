package com.mazebert.plugins.security.mocks;

import com.mazebert.plugins.security.GooglePlayPurchaseVerifier;

public class GooglePlayPurchaseVerifierMock extends GooglePlayPurchaseVerifier {
    private boolean fakeVerification;

    @Override
    public boolean isSignatureValid(String data, String signature) {
        if (fakeVerification) {
            return true;
        }
        return super.isSignatureValid(data, signature);
    }

    public void givenSignatureVerificationSucceeds() {
        fakeVerification = true;
    }
}
