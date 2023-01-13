package org.hcmus.premiere.util.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

interface EncryptionUtils {

  String encrypt(Object o)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException;

  String encryptV2(String input)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, SignatureException;

  boolean verify(String input, String signature, String bankPublicKey)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException;

  Object decrypt(String cipherObject)
      throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException;
}
