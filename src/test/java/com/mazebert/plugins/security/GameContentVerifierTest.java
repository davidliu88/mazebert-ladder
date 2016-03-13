package com.mazebert.plugins.security;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class GameContentVerifierTest {
    private GameContentVerifier contentVerifier = new GameContentVerifier();

    @Test
    public void validContent() {
        assertTrue(contentVerifier.verify(
                a(inputStream().withString("{\"questId\":25,\"key\":\"32XQOD\",\"timezoneOffset\":-1,\"appVersion\":\"1.3.1\"}")),
                "946a8d4f91548f970668c129004958a9e66190ae5eccddd06c7c6286ea64557f"));

    }
}