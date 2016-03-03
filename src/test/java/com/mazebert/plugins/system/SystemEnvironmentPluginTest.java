package com.mazebert.plugins.system;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SystemEnvironmentPluginTest {
    private EnvironmentPlugin plugin = new SystemEnvironmentPlugin();

    @Test
    public void sanityCheck() {
        assertNotNull(plugin.getEnvironmentVariable("PATH"));
    }
}
