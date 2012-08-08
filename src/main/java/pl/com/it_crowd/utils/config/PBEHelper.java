package pl.com.it_crowd.utils.config;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class PBEHelper {
// ------------------------------ FIELDS ------------------------------

    private static final String CHARSET_NAME = "UTF8";

    private Cipher cipher;

    private Cipher ecipher;

// --------------------------- CONSTRUCTORS ---------------------------

    public PBEHelper(String encryptionAlgorithm, String passPhrase, String salt, int iterationCount)
    {
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), toSalt(salt), iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(encryptionAlgorithm).generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            cipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(toSalt(salt), iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("Cannot initialize PBEHelper", e);
            }
        }
    }

// -------------------------- OTHER METHODS --------------------------

    public String decrypt(String str)
    {
        try {
            byte[] dec = Hex.decodeHex(str.toCharArray());
            return new String(cipher.doFinal(dec), CHARSET_NAME);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("Problems during decrypting", e);
            }
        }
    }

    public String encrypt(String str)
    {
        try {
            byte[] bytes = str.getBytes(CHARSET_NAME);
            return Hex.encodeHexString(ecipher.doFinal(bytes));
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("Problems during encrypting", e);
            }
        }
    }

    private byte[] toSalt(String phrase)
    {
        return Arrays.copyOf(phrase.getBytes(), 8);
    }
}