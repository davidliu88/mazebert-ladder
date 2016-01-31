package com.mazebert.plugins.security;

import java.io.InputStream;

public interface ContentVerifier {
    boolean verify(InputStream content, String signature);
}
