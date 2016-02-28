package com.mazebert.plugins.security;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class ContentSignerTest {
    private static final String dummyPrivateKey = "-----BEGIN RSA PRIVATE KEY-----\nProc-Type: 4,ENCRYPTED\nDEK-Info: DES-EDE3-CBC,76244C066BE5CCE1\n\nfTXJn4rTsk57AxPyQg3QwI22LhX+pfcULBfG7E5OFuwglFu8VfjW53LEMUAvjvWh\nX22EtZ7gpwfo2VjHaKSJlINI59oH7mbd5AQHBVZVGYBmHQ0yD2rUqg7JdI08sMpg\nvfxUPnCVD0FCD+nmzN+beW2U+cCrQ/zDO53+Ncyvte4Ejb4pCl1rHxecFEH0nZK1\nrmoB0a7cvRQmfzUIJIk+GjYF6bjxv4HrT6ixHCzYwo0=\n-----END RSA PRIVATE KEY-----";
    private static final String dummyPrivateKeyPassphrase = "development";
    private static final String dummyPublicKey = "-----BEGIN PUBLIC KEY-----\nMDwwDQYJKoZIhvcNAQEBBQADKwAwKAIhAL3/H9KD6qo7SuiB4xqH+2DKXUDK/oG3\n0cVUwXS17b+lAgMBAAE=\n-----END PUBLIC KEY-----";

    @Test
    public void contentSignatureRoundTrip() {
        String content = "{\"status\":\"ok\",\"totalPlayers\":0,\"nowPlaying\":0,\"nowPlayingPlayers\":[]}";

        ContentSigner signer = new ContentSigner(dummyPrivateKey, dummyPrivateKeyPassphrase);
        String signature = signer.sign(a(inputStream().withString(content)));

        ContentVerifier verifier = new ContentVerifier(dummyPublicKey);
        assertTrue(verifier.verify(a(inputStream().withString(content)), signature));
    }
}