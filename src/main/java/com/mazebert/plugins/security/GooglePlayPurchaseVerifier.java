package com.mazebert.plugins.security;

import com.mazebert.error.InternalServerError;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;

import javax.inject.Inject;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class GooglePlayPurchaseVerifier {
    private final AsymmetricBlockCipher cipher;
    private final Logger logger;

    @Inject
    public GooglePlayPurchaseVerifier(Logger logger) {
        this.logger = logger;
        cipher = createCipher("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvv8DXa2BDiES39Fiq+b/S/nENjCu+XYZVqVGpfqXAlPjLWn9pVIuKzqz3i3f3UhpyZb5CAhJsGZV48jhDcfgI8ljLGJGyI4JXw0RXL77M/7c/cmZHo6puSSQeQMSCDXYEKcz3RV2plIXMRmuc+n7pGzaiwQLsNAf5xZLvGp0PNDjxNcSCMCOfQoo0brSqo0oxuYZsMzh8TMsGarrs0nZkBddLfGdvEJFPDM6oDiRX9JniacKo8dOeEM6ZABnjZtMorOJXLuqAptH+6qhKhcmy+kWSh2zke1YFxdAhpB3y5D09LDFrK4y16vwNIQkFT/7su+WeA2tkxuFCdIfWaCcBwIDAQAB");
    }

    private AsymmetricBlockCipher createCipher(String publicKey) {
        PKCS1Encoding cipher = new PKCS1Encoding(new RSAEngine());
        cipher.init(false, parseKey(convertToPemKey(publicKey)));
        return cipher;
    }

    private String convertToPemKey(String key) {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PUBLIC KEY-----\n");

        int index = 0;
        int length = key.length();
        while (index < length) {
            pem.append(key.substring(index, Math.min(length, index += 64)));
            pem.append('\n');
        }

        pem.append("-----END PUBLIC KEY-----");
        return pem.toString();
    }

    private RSAKeyParameters parseKey(String key) {
        try (InputStreamReader reader = new InputStreamReader(a(inputStream().withString(key)))) {
            PEMParser parser = new PEMParser(reader);
            return (RSAKeyParameters) PublicKeyFactory.createKey((SubjectPublicKeyInfo)parser.readObject());
        } catch (Throwable e) {
            throw new InternalServerError("Failed to parse public key for Google Play verification.", e);
        }
    }

    public boolean isSignatureValid(String data, String signature) {
        try {
            byte[] expectedHash = DigestUtils.sha1(data);
            byte[] receivedHash = decrypt(signature);

            int offset = receivedHash.length - expectedHash.length;
            for (int i = 0; i < expectedHash.length; ++i) {
                if (expectedHash[i] != receivedHash[i + offset]) {
                    return false;
                }
            }
            return true;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Unexpected exception when verifying GooglePlay signature. (Exception Message: " + e.getMessage() + ")");
            logger.log(Level.SEVERE, "Data: " + data);
            logger.log(Level.SEVERE, "Signature: " + signature);
            return false;
        }
    }

    private byte[] decrypt(String signature) throws Throwable {
        byte[] signatureBytes = Base64.decodeBase64(signature);
        return cipher.processBlock(signatureBytes, 0, signatureBytes.length);
    }
}
