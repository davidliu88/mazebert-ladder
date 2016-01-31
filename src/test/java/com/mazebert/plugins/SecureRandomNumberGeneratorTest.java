package com.mazebert.plugins;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecureRandomNumberGeneratorTest extends RandomNumberGeneratorTest {
    @Override
    protected SecureRandomNumberGenerator createGenerator() {
        return new SecureRandomNumberGenerator();
    }
}