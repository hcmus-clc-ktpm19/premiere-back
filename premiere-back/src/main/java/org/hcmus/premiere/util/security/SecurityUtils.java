package org.hcmus.premiere.util.security;

import static org.hcmus.premiere.model.enums.PremiereRole.CUSTOMER;
import static org.hcmus.premiere.model.enums.PremiereRole.EMPLOYEE;
import static org.hcmus.premiere.model.enums.PremiereRole.PREMIERE_ADMIN;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityUtils")
public class SecurityUtils {

  private final EncryptionUtils asymmetricEncryptionUtils;

  private final EncryptionUtils symmetricEncryptionUtils;

  private final Environment env;

  private final HmacUtils hmacUtils;

  private static final String PREFIX = "ROLE_";

  public SecurityUtils(Environment env,
      @Qualifier("AsymmetricEncryptionUtils") EncryptionUtils asymmetricEncryptionUtils,
      @Qualifier("SymmetricEncryptionUtils") EncryptionUtils symmetricEncryptionUtils) {
    this.asymmetricEncryptionUtils = asymmetricEncryptionUtils;
    this.symmetricEncryptionUtils = symmetricEncryptionUtils;
    this.hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_MD5, env.getProperty("system-auth.secret-key"));
    this.env = env;
  }

  @RequiredArgsConstructor
  public enum Algorithm {
    RSA("RSA");

    final String value;
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
    return hasRole(CUSTOMER);
  }

  public static boolean isEmployeeOrAdmin() {
    return containsRoles(EMPLOYEE, PREMIERE_ADMIN);
  }

  public static List<PremiereRole> getStaffRoles() {
    return List.of(EMPLOYEE, PREMIERE_ADMIN);
  }

  public String encrypt(Object o, boolean isAsymmetric)
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    return isAsymmetric ? asymmetricEncryptionUtils.encrypt(o) : encrypt(o);
  }

  public String encryptV2(String input)
      throws InvalidAlgorithmParameterException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
    return asymmetricEncryptionUtils.encryptV2(input);
  }

  public boolean verify(String input, String signature, String bankPublicKey)
      throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
    return asymmetricEncryptionUtils.verify(input, signature, bankPublicKey);
  }

  public String encrypt(Object object)
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    return symmetricEncryptionUtils.encrypt(object);
  }

  public Object decrypt(String cipherObject, boolean isAsymmetric)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    return isAsymmetric ? asymmetricEncryptionUtils.decrypt(cipherObject) : decrypt(cipherObject);
  }

  public Object decrypt(String cipherObject)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    return symmetricEncryptionUtils.decrypt(cipherObject);
  }

  public String hash(Object o) {
    return hmacUtils.hmacHex(SerializationUtils.serialize((Serializable) o));
  }

  public String getSecretKey() {
    return env.getProperty("system-auth.secret-key");
  }

  public String getExternalSecretKey() {
    return env.getProperty("external-bank.secret-key");
  }

  public String hashMd5(String data) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(data.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      StringBuilder hashText = new StringBuilder(no.toString(16));
      while (hashText.length() < 32) {
        hashText.insert(0, "0");
      }
      return hashText.toString();
    }
    catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
