package com.mazebert.plugins.security;


import com.mazebert.error.InternalServerError;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class ContentVerifier {
    private final AsymmetricBlockCipher cipher;

    public ContentVerifier(String key) {
        this.cipher = createCipher(key);
    }

    private AsymmetricBlockCipher createCipher(String key) {
        PKCS1Encoding cipher = new PKCS1Encoding(new RSAEngine());
        cipher.init(false, parseKey(key));
        return cipher;
    }

    private RSAKeyParameters parseKey(String key) {
        try (InputStreamReader reader = new InputStreamReader(a(inputStream().withString(key)))) {
            PEMParser parser = new PEMParser(reader);
            return (RSAKeyParameters)PublicKeyFactory.createKey((SubjectPublicKeyInfo)parser.readObject());
        } catch (Throwable e) {
            throw new InternalServerError("Failed to parse public key for content verification.", e);
        }
    }

    public boolean verify(InputStream content, String signature) {
        try {
            try {
                byte[] expectedHash = DigestUtils.md5(content);
                byte[] receivedHash = decrypt(signature);
                return Arrays.equals(expectedHash, receivedHash);
            } finally {
                content.close();
            }
        } catch (Throwable e) {
            return false;
        }
    }

    private byte[] decrypt(String signature) throws Throwable {
        byte[] signatureBytes = Hex.decodeHex(signature.toCharArray());
        return cipher.processBlock(signatureBytes, 0, signatureBytes.length);
    }
}
