package org.hcmus.premiere.util.security;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import org.apache.commons.lang3.SerializationUtils;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityUtils")
public class SecurityUtils {

  private final Cipher cipher;

  private final PublicKey publicKey;

  private final PrivateKey privateKey;

  private static final String PREFIX = "ROLE_";

  public SecurityUtils(Environment env, Cipher cipher)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    this.cipher = cipher;
    KeyFactory keyFactory = KeyFactory.getInstance(
        Objects.requireNonNull(env.getProperty("spring.security.rsa.algorithm")));

    byte[] publicBytes = Base64.getDecoder()
        .decode(env.getProperty("spring.security.rsa.public-key"));
    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicBytes);
    this.publicKey = keyFactory.generatePublic(pubKeySpec);

    byte[] privateBytes = Base64.getDecoder()
        .decode(env.getProperty("spring.security.rsa.private-key"));
    PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateBytes);
    this.privateKey = keyFactory.generatePrivate(priKeySpec);
  }

  public static boolean hasRole(PremiereRole role) {
    return SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(PREFIX + role));
  }

  public static boolean containsRoles(PremiereRole... roles) {
    return Arrays.stream(roles).anyMatch(SecurityUtils::hasRole);
  }

  public static boolean isCustomer() {
    return hasRole(PremiereRole.CUSTOMER);
  }

  public static boolean isStaff() {
    return containsRoles(PremiereRole.EMPLOYEE, PremiereRole.PREMIERE_ADMIN);
  }

  public String encrypt(Object object)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] objectInBytes = SerializationUtils.serialize((Serializable) object);
    byte[] encryptedObjectInBytes = cipher.doFinal(objectInBytes);
    return Base64.getEncoder().encodeToString(encryptedObjectInBytes);
  }

  public String decrypt(String cipherObject) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    cipher.init(Cipher.DECRYPT_MODE, privateKey);

    byte[] cipherObjectInBytes = Base64.getDecoder().decode(cipherObject);
    byte[] decryptedObjectInBytes = cipher.doFinal(cipherObjectInBytes);
    return SerializationUtils.deserialize(decryptedObjectInBytes);
  }
}
