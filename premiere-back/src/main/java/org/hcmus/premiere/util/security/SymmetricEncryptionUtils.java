package org.hcmus.premiere.util.security;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("SymmetricEncryptionUtils")
class SymmetricEncryptionUtils implements EncryptionUtils {

  private final Cipher cipher;

  private final SecretKey secretKey;

  public static final String ALGORITHM = "AES";

  public SymmetricEncryptionUtils(Environment env)
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.cipher = Cipher.getInstance(ALGORITHM);

    byte[] decodedKey = Base64.getDecoder().decode(env.getProperty("system-auth.secret-key"));
    this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
  }

  @Override
  public String encrypt(Object o)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] cipherObject = cipher.doFinal(SerializationUtils.serialize((Serializable) o));
    return Base64.getEncoder().encodeToString(cipherObject);
  }

  @Override
  public Object decrypt(String cipherObject)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] cipherObjectBytes = cipher.doFinal(Base64.getDecoder().decode(cipherObject));
    return SerializationUtils.deserialize(cipherObjectBytes);
  }
}
