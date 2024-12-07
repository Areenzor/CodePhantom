package com.codephantom.utils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * CryptoUtils provides utility methods for cryptographic operations such as encryption,
 * decryption, hashing, and key generation.
 */
public class CryptoUtils {

    private static final Logger LOGGER = Logger.getLogger(CryptoUtils.class.getName());

    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RSA = "RSA";
    private static final int AES_KEY_SIZE = 256;
    private static final int RSA_KEY_SIZE = 2048;

    /**
     * Generates a secure random AES key.
     *
     * @return The generated AES key.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     */
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    /**
     * Encrypts a plaintext string using AES encryption.
     *
     * @param plainText The plaintext to encrypt.
     * @param key       The AES key.
     * @return The Base64-encoded ciphertext.
     * @throws Exception If encryption fails.
     */
    public static String encryptAES(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec iv = generateIV();
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(iv.getIV()) + ":" + Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypts a ciphertext string using AES encryption.
     *
     * @param cipherText The Base64-encoded ciphertext (with IV prefix).
     * @param key        The AES key.
     * @return The decrypted plaintext.
     * @throws Exception If decryption fails.
     */
    public static String decryptAES(String cipherText, SecretKey key) throws Exception {
        String[] parts = cipherText.split(":");
        byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted);
    }

    /**
     * Generates a secure random IV (Initialization Vector).
     *
     * @return The generated IV.
     */
    private static IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Generates an RSA key pair.
     *
     * @return The RSA key pair.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA);
        keyGen.initialize(RSA_KEY_SIZE);
        return keyGen.generateKeyPair();
    }

    /**
     * Encrypts a plaintext string using RSA encryption.
     *
     * @param plainText The plaintext to encrypt.
     * @param publicKey The RSA public key.
     * @return The Base64-encoded ciphertext.
     * @throws Exception If encryption fails.
     */
    public static String encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypts a ciphertext string using RSA encryption.
     *
     * @param cipherText The Base64-encoded ciphertext.
     * @param privateKey The RSA private key.
     * @return The decrypted plaintext.
     * @throws Exception If decryption fails.
     */
    public static String decryptRSA(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted);
    }

    /**
     * Generates a secure hash for a given input using SHA-256.
     *
     * @param input The input string to hash.
     * @return The Base64-encoded hash.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     */
    public static String hashSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies the integrity of an input against its hash.
     *
     * @param input The input string.
     * @param hash  The Base64-encoded hash to verify against.
     * @return True if the input matches the hash, false otherwise.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     */
    public static boolean verifySHA256(String input, String hash) throws NoSuchAlgorithmException {
        String computedHash = hashSHA256(input);
        return computedHash.equals(hash);
    }

    /**
     * Main method for testing the CryptoUtils.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        try {
            // AES Test
            SecretKey aesKey = generateAESKey();
            String aesEncrypted = encryptAES("Hello, AES!", aesKey);
            String aesDecrypted = decryptAES(aesEncrypted, aesKey);
            System.out.println("AES Encrypted: " + aesEncrypted);
            System.out.println("AES Decrypted: " + aesDecrypted);

            // RSA Test
            KeyPair rsaKeyPair = generateRSAKeyPair();
            String rsaEncrypted = encryptRSA("Hello, RSA!", rsaKeyPair.getPublic());
            String rsaDecrypted = decryptRSA(rsaEncrypted, rsaKeyPair.getPrivate());
            System.out.println("RSA Encrypted: " + rsaEncrypted);
            System.out.println("RSA Decrypted: " + rsaDecrypted);

            // Hashing Test
            String hash = hashSHA256("Hello, Hashing!");
            System.out.println("Hash: " + hash);
            System.out.println("Hash Verified: " + verifySHA256("Hello, Hashing!", hash));

        } catch (Exception e) {
            LOGGER.severe("Error in cryptographic operations: " + e.getMessage());
        }
    }
}
