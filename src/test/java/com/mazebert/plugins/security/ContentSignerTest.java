package com.mazebert.plugins.security;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class ContentSignerTest {
    private static final String dummyPrivateKey = "-----BEGIN RSA PRIVATE KEY-----\nMIGpAgEAAiEAvf8f0oPqqjtK6IHjGof7YMpdQMr+gbfRxVTBdLXtv6UCAwEAAQIg\nSI7Z1EdUotYp8UlJNaSgttmcaFlVmPMoHewCPD8uDzECEQDercywq3diL8VDxGDS\nnnYPAhEA2m1baexnfQWyc20uDvuDCwIQaeQiK31Ohz2KRCCVnnLGVwIQHLnjGtQf\nbB7SsEF8nDXRrwIQEqQCE2S8hJ/N7GaCL7FdBA==\n-----END RSA PRIVATE KEY-----";
    private static final String dummyPublicKey = "-----BEGIN PUBLIC KEY-----\nMDwwDQYJKoZIhvcNAQEBBQADKwAwKAIhAL3/H9KD6qo7SuiB4xqH+2DKXUDK/oG3\n0cVUwXS17b+lAgMBAAE=\n-----END PUBLIC KEY-----";

    @Test
    public void contentSignatureRoundTrip() {
        String content = "{\"status\":\"ok\",\"totalPlayers\":0,\"nowPlaying\":0,\"nowPlayingPlayers\":[]}";

        ContentSigner signer = new ContentSigner(dummyPrivateKey);
        String signature = signer.sign(a(inputStream().withString(content)));

        ContentVerifier verifier = new ContentVerifier(dummyPublicKey);
        assertTrue(verifier.verify(a(inputStream().withString(content)), signature));
    }
}