package com.mazebert.plugins.random;

public class SecureRandomNumberGeneratorTest extends RandomNumberGeneratorTest {
    @Override
    protected SecureRandomNumberGenerator createGenerator() {
        return new SecureRandomNumberGenerator();
    }
}