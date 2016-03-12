package com.mazebert.plugins.security;

import com.mazebert.error.InternalServerError;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class ContentSigner {
    private final AsymmetricBlockCipher cipher;

    public ContentSigner(String key) {
        this.cipher = createCipher(key);
    }

    private AsymmetricBlockCipher createCipher(String key) {
        PKCS1Encoding cipher = new PKCS1Encoding(new RSAEngine());
        cipher.init(true, parseKey(key));
        return cipher;
    }

    private RSAKeyParameters parseKey(String key) {
        try (InputStreamReader reader = new InputStreamReader(a(inputStream().withString(key)))) {
            PEMParser parser = new PEMParser(reader);
            PEMKeyPair keyPair = (PEMKeyPair)parser.readObject();
            return (RSAKeyParameters) PrivateKeyFactory.createKey(keyPair.getPrivateKeyInfo());
        } catch (Throwable e) {
            throw new InternalServerError("Failed to parse private key for content signing.", e);
        }
    }

    public String sign(InputStream content) {
        try {
            try {
                byte[] hash = DigestUtils.md5(content);
                return encrypt(hash);
            } finally {
                content.close();
            }
        } catch (Throwable e) {
            throw new InternalServerError("Failed to sign content with private key.", e);
        }
    }

    private String encrypt(byte[] hash) throws Throwable {
        byte[] signature = cipher.processBlock(hash, 0, hash.length);
        return new String(Hex.encodeHex(signature, true));
    }
}
