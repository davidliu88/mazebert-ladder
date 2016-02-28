package com.mazebert.plugins.system;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class EnvironmentPluginTest {
    private EnvironmentPlugin plugin = new EnvironmentPlugin();

    @Test
    public void sanityCheck() {
        assertNotNull(plugin.getEnvironmentVariable("PATH"));
    }
}
