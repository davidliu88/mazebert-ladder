package com.mazebert.plugins.security;

import com.mazebert.plugins.system.EnvironmentPlugin;

import javax.inject.Inject;

public class ServerContentSigner extends ContentSigner {
    @Inject
    public ServerContentSigner(EnvironmentPlugin environmentPlugin) {
        super(
                getPrivateKey(environmentPlugin),
                getPassphrase(environmentPlugin)
        );
    }

    private static String getPrivateKey(EnvironmentPlugin environmentPlugin) {
        String key = environmentPlugin.getEnvironmentVariable("MAZEBERT_PRIVATE_KEY");
        return key == null ? getDevelopmentKey() : key;
    }

    private static String getPassphrase(EnvironmentPlugin environmentPlugin) {
        String passphrase = environmentPlugin.getEnvironmentVariable("MAZEBERT_PRIVATE_KEY_PASSPHRASE");
        return passphrase == null ? getDevelopmentPassphrase() : passphrase;
    }

    private static String getDevelopmentKey() {
        return "-----BEGIN RSA PRIVATE KEY-----\nProc-Type: 4,ENCRYPTED\nDEK-Info: DES-EDE3-CBC,76244C066BE5CCE1\n\nfTXJn4rTsk57AxPyQg3QwI22LhX+pfcULBfG7E5OFuwglFu8VfjW53LEMUAvjvWh\nX22EtZ7gpwfo2VjHaKSJlINI59oH7mbd5AQHBVZVGYBmHQ0yD2rUqg7JdI08sMpg\nvfxUPnCVD0FCD+nmzN+beW2U+cCrQ/zDO53+Ncyvte4Ejb4pCl1rHxecFEH0nZK1\nrmoB0a7cvRQmfzUIJIk+GjYF6bjxv4HrT6ixHCzYwo0=\n-----END RSA PRIVATE KEY-----";
    }

    public static String getDevelopmentPassphrase() {
        return "development";
    }
}
