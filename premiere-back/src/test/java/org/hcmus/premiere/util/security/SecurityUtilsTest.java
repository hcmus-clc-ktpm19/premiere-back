package org.hcmus.premiere.util.security;

import static org.mockito.ArgumentMatchers.anyString;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.dto.OTPDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SecurityUtilsTest {

  @Autowired
  private SecurityUtils securityUtils;

  @Test
  void testGetRsaPubSubKey() throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair keyPair = generator.generateKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();

    log.info("private key: {}", privateKey);
    log.info("public key: {}", publicKey);

    Assertions.assertThat(privateKey).isNotNull();
    Assertions.assertThat(publicKey).isNotNull();
  }

  @Test
  void testRsaEncryptPlainText()
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String plainText = "Hello World";

    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair keyPair = generator.generateKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] secretMessageBytes = plainText.getBytes(StandardCharsets.UTF_8);
    byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);

    String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
    log.info("encoded message: {}", encodedMessage);

    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes);
    String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
    log.info("decrypted message: {}", decryptedMessage);

    Assertions.assertThat(decryptedMessage).isEqualTo(plainText);
  }

  @Test
  void testRsaEncryptObject()
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    OTPDto object = new OTPDto();
    object.setOtp(anyString());
    object.setEmail(anyString());
    object.setCreatedAt(LocalDateTime.now());

    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair keyPair = generator.generateKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();

    log.info("private key: {}", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    log.info("public key: {}", Base64.getEncoder().encodeToString(publicKey.getEncoded()));

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] secretMessageBytes = SerializationUtils.serialize(object);
    byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);

    String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
    log.info("encoded message: {}", encodedMessage);

    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes);
    OTPDto decryptedMessage = SerializationUtils.deserialize(decryptedMessageBytes);
    log.info("decrypted message: {}", decryptedMessage);

    Assertions.assertThat(decryptedMessage).isEqualTo(object);
  }


  @Test
  void testEncrypt()
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    String encrypted = securityUtils.encrypt("Hello World");
    String decrypted = securityUtils.decrypt(encrypted);

    Assertions.assertThat(decrypted).isEqualTo("Hello World");
  }
}