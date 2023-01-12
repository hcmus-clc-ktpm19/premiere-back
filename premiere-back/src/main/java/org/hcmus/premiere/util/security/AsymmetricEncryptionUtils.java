package org.hcmus.premiere.util.security;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("AsymmetricEncryptionUtils")
class AsymmetricEncryptionUtils implements EncryptionUtils {

  private final Cipher cipher;

  private final PublicKey publicKey;

  private final PrivateKey privateKey;

  private static final String ALGORITHM = "RSA";

  public AsymmetricEncryptionUtils(Environment env)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {

    Security.addProvider(new BouncyCastleProvider());
    this.cipher = Cipher.getInstance(ALGORITHM + "/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

    byte[] publicBytes = Base64.getDecoder()
        .decode(env.getProperty("system-auth.public-key"));
    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicBytes);
    this.publicKey = keyFactory.generatePublic(pubKeySpec);

    byte[] privateBytes = Base64.getDecoder()
        .decode(env.getProperty("system-auth.private-key"));
    PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateBytes);
    this.privateKey = keyFactory.generatePrivate(priKeySpec);
  }

  @Override
  public String encrypt(Object o)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] objectInBytes = SerializationUtils.serialize((Serializable) o);
    byte[] encryptedObjectInBytes = cipher.doFinal(objectInBytes);
    return Base64.getEncoder().encodeToString(encryptedObjectInBytes);
  }

  @Override
  public String encryptV2(String input)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(privateKey);
    privateSignature.update(input.getBytes(StandardCharsets.UTF_8));
    byte[] s = privateSignature.sign();
    BigInteger no = new BigInteger(1, s);
    StringBuilder hashText = new StringBuilder(no.toString(16));
    while (hashText.length() < 32) {
      hashText.insert(0, "0");
    }
    return hashText.toString();
  }

  @Override
  public boolean verify(String input, String signature, String bankPublicKey)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
    String publicKey = bankPublicKey.replace("\\n","");
    byte[] publicBytes = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(keyFactory.generatePublic(pubKeySpec));
    publicSignature.update(input.getBytes(StandardCharsets.UTF_8));
    byte[] signatureBytes = new BigInteger(signature, 16).toByteArray();
    return publicSignature.verify(signatureBytes);
  }

  @Override
  public Object decrypt(String cipherObject)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    cipher.init(Cipher.DECRYPT_MODE, privateKey);

    byte[] cipherObjectInBytes = Base64.getDecoder().decode(cipherObject);
    byte[] decryptedObjectInBytes = cipher.doFinal(cipherObjectInBytes);
    return SerializationUtils.deserialize(decryptedObjectInBytes);
  }
}
