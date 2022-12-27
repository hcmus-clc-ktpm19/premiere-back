package org.hcmus.premiere.util.security;

import static org.hcmus.premiere.model.enums.TransactionStatus.CHECKING;
import static org.hcmus.premiere.model.enums.TransactionType.LOAN;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
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
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.dto.OTPDto;
import org.hcmus.premiere.model.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootTest
class SecurityUtilsTest {

  @Autowired
  private SecurityUtils securityUtils;

  @Autowired
  private Environment env;

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
  void testAsymmetricEncryptPlainText()
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    // Given
    String plainText = "Hello World";

    // When
    String encrypted = securityUtils.encrypt(plainText, true);
    String decrypted = securityUtils.decrypt(encrypted, true).toString();

    // Then
    Assertions.assertThat(decrypted).isEqualTo(plainText);
  }

  @Test
  void testAsymmetricEncryptObject()
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    // Given
    OTPDto object = new OTPDto();
    object.setOtp(anyString());
    object.setEmail(anyString());
    object.setCreatedAt(LocalDateTime.parse("2022-12-24T00:00:00"));

    // When
    String encrypted = securityUtils.encrypt(object, true);
    OTPDto decrypted = (OTPDto) securityUtils.decrypt(encrypted, true);

    // Then
    Assertions.assertThat(decrypted).isEqualTo(object);
  }

  @Test
  void generateSecretKey() throws NoSuchAlgorithmException {
    byte[] decodedKey = Base64.getDecoder().decode(env.getProperty("system-auth.secret-key"));
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    Assertions.assertThat(originalKey).isNotNull();
  }

  @Test
  void testSymmetricEncryptText()
      throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    // Given
    String plainText = "Hello World";

    // When
    String encrypted = securityUtils.encrypt(plainText, false);
    String decrypted = securityUtils.decrypt(encrypted, false).toString();

    // Then
    Assertions.assertThat(decrypted).isEqualTo(plainText);
  }

  @Test
  void testSymmetricEncryptObject()
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    TransactionDto object = new TransactionDto();
    object.setId(anyLong());
    object.setVersion(anyLong());
    object.setCreatedAt(LocalDateTime.parse("2022-12-24T10:15:30"));
    object.setUpdatedAt(LocalDateTime.parse("2022-12-24T10:15:30"));
    object.setAmount(BigDecimal.valueOf(anyLong()));
    object.setType(LOAN);
    object.setTime(LocalDateTime.parse("2022-12-24T10:15:30"));
    object.setTransactionRemark(anyString());
    object.setTotalBalance(BigDecimal.valueOf(anyLong()));
    object.setSenderCreditCardNumber(anyString());
    object.setReceiverCreditCardNumber(anyString());
    object.setFee(BigDecimal.valueOf(anyLong()));
    object.setSelfPaymentFee(anyBoolean());
    object.setStatus(CHECKING);
    object.setSenderBankId(anyLong());
    String encrypted = securityUtils.encrypt(object);
    log.info("encrypt: {}", encrypted);

    TransactionDto decrypt = (TransactionDto) securityUtils.decrypt(encrypted);

    Assertions.assertThat(encrypted).isEqualTo("N8i3ecq1YOEOYJcHRD/AS12zxKmP1eeOeCKtpCa12oG/TL5k9FElvBqO6xXiFWYJNeOzSvXEcgmTPvJJvY42bQAe22ky9pge7fn+tq7BPgPBL3QmsZ/FiCqEGPWWypAHOzX+VVVzxjzGXhgW798Qg112EchJIRXssM/qHaqgVrdjw7tqY8PKFouCt6OWc22eyH3dKQFCk62HSQlStulLljlq0uJ5kDq7pZoEgWKurmDaxH0NH4FnJ6RIFq7dFs6uLgOR6RNgIYNt6TLQqC9xkcS/PF+NVv2Tr5yYd72SKjYmZpc4DWSen5z4y/RzRkHWFsswnVWZwFTtvXNZNtFaJbbyBzJQOuYVKsQ0rGi+grr/yKNQ0owQFweEPIC7fDsJC+vUiH7RoaQ32NVkKLC8c6zXF9sEO73uSC+rlkfK3pKiC0QGAWlsh8d4NlFClnrdo8Br825vVCHsyYlP6TLsrfeeCMil0gLSQ9f+Or0vnTdI2gwqo40UDHrpjBBBVJtCJQOp9uxQjgSiLBDB8SaoB7BLbmiWdQKLhf06G0Qiz/Wofqbaz/XqDiezrHlOly7IPvrPxyTcRpqe5CyV/NhFhRNr+RTmI7x8yOF7X4zY6IxSczBcTDofV0MwHRlixjZLNiLaM8VXKmx4D27ThwjKAfQ8u9zIhlNyizA4Fu1mCPw4yYLzxCiLOuXLmmZ0+Uvola/0Wi4ouHEXqJlw7G8yoxg0j9jCajgaF65uL/rITF5ZlJoaeakwVE/S+tcrfiY34Sn2jWLFM6GR7ctGvkL8gOKPu6tPyBDjC5JrISxVRlkEWRGm3h2eCnUQxxl3UHitqujwBR4KrbqbTqCzqxinHuFog9VOwkcczgBzjfYLgp09TuoldpA2ngvBKuA0JqUTfk4IyCcgDe6CfRPDHIeqIdPGyRZOXMevDDEIZwK1B0BHc0W7IikSk6YZ4tJId57gGp3k6vvfzMbPCh+HzR7twrsi57oeCMUOO2YecKibg1RpVxxRnCIaDtzvSWDsyNyhTeYesGOXHW9KHJpR+jA234FPjo3rrW7efKnRGTV3PImJ8iOKeNxhLBDtDcywg4tEMkg781s5Gg6SfY9TY7rLGqKnk5QXJ3n/N3yodEMwC6LylKIwLt4oCeRUwDvB/wN98pisrCBEQFQg8xC5OHa4s6es5WW77RxkMuORjkgRzusKKORZlhJ1MaFpY8iuh7yOlAJLSsrBilS6FTo+bVTKSWXyUSoVE9TgzzNuUkEBT3VwVdTGJxuFwstfh0WnpIJ3gH2ZRY9wksjZzt19+sMk4IWtFfcGy56ucz0GdLfCQF0nNjaHHavDhjN29sSiePd1g3aK+wEJPkmpxme48s5pIZmG6oZgTcCVxF/VpXaIWNXY+8jezvr4RvSHfTg4+xFtsr7zbWHGyM+GerwzE4QKtjXgjyNswn47EB65/p1I83DExPEYTtezDVxNFofZe3v/Z+jgldsszflhBMdxpDk81QcES0BNwU2sgIOTPCxe0Bt18CBa9I6B6f/g+qiMBDu3zeG9asuFdI8TvbeTuRmogA==");
    Assertions.assertThat(decrypt).isEqualTo(object);
  }

  @Test
  void testHash() {
    TransactionDto object = new TransactionDto();
    object.setId(anyLong());
    object.setVersion(anyLong());
    object.setCreatedAt(LocalDateTime.parse("2022-12-24T10:15:30"));
    object.setUpdatedAt(LocalDateTime.parse("2022-12-24T10:15:30"));
    object.setAmount(BigDecimal.valueOf(anyLong()));
    object.setType(LOAN);
    object.setTime(LocalDateTime.parse("2022-12-24T10:15:30"));
    object.setTransactionRemark(anyString());
    object.setTotalBalance(BigDecimal.valueOf(anyLong()));
    object.setSenderCreditCardNumber(anyString());
    object.setReceiverCreditCardNumber(anyString());
    object.setFee(BigDecimal.valueOf(anyLong()));
    object.setSelfPaymentFee(anyBoolean());
    object.setStatus(CHECKING);
    object.setSenderBankId(anyLong());
    String encrypted = securityUtils.hash(object);

    Assertions.assertThat(encrypted).isEqualTo("153ac2a6a61c3a8f4736ac98cb622cb7");
  }
}