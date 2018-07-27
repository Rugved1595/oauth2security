package com.afour.oauthsecurity.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CustomPasswordEncoder is written to use custom password encoder technique
 * using PBKDF2. The purpose of this in the application is because it'll help
 * generating token with fast response time. If we use default methods of Spring
 * Boot classes then observation is seen that an OAuth token takes large
 * response time to generate.
 * 
 * @author rugved.m
 *
 */

@Component("passwordEncoder")
public class CustomPasswordEncoder implements PasswordEncoder {

	public static final String TOKEN_PREFIX = "$3creat$";

	private static final Pattern layout = Pattern.compile("\\$3creat\\$(\\d\\d?)\\$(.{43})");

	private static final int SIZE = 128;

	private final SecureRandom random = new SecureRandom();;

	private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

	public static final int DEFAULT_ITERATIONS = 5;

	private static int iterations(int iterations) {
		if ((iterations & ~0x1F) != 0)
			throw new IllegalArgumentException("iterations: " + iterations);
		return 1 << iterations;
	}

	/**
	 * To hash a password for the first time, call the hashpw method with a random
	 * salt.
	 * 
	 * @param rawPassword
	 *            meaning plain password.
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		char[] pwd = new char[rawPassword.length()];
		for (int i = 0; i < rawPassword.length(); i++) {
			pwd[i] = rawPassword.charAt(i);
		}

		byte[] salt = new byte[SIZE / 8];
		random.nextBytes(salt);
		byte[] dk = pbkdf2(pwd, salt, 1 << DEFAULT_ITERATIONS);
		byte[] hash = new byte[salt.length + dk.length];
		System.arraycopy(salt, 0, hash, 0, salt.length);
		System.arraycopy(dk, 0, hash, salt.length, dk.length);
		Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
		return TOKEN_PREFIX + DEFAULT_ITERATIONS + '$' + enc.encodeToString(hash);
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
		KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
			return f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
		} catch (InvalidKeySpecException ex) {
			throw new IllegalStateException("Invalid SecretKeyFactory", ex);
		}
	}
	
	/**
	 * To check whether a plain text password matches one that has been hashed
	 * previously
	 * 
	 * @param rawPassword
	 * @param encodedPassword
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String rawPasswordStr = rawPassword.toString();
		char[] rawPasswordCharArr = rawPasswordStr.toCharArray();
		Matcher m = layout.matcher(encodedPassword);
		if (!m.matches())
			throw new IllegalArgumentException("Invalid format");
		int iterations = iterations(Integer.parseInt(m.group(1)));
		byte[] hash = Base64.getUrlDecoder().decode(m.group(2));
		byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
		byte[] check = pbkdf2(rawPasswordCharArr, salt, iterations);
		int zero = 0;
		for (int idx = 0; idx < check.length; ++idx)
			zero |= hash[salt.length + idx] ^ check[idx];
		return zero == 0;
	}
}
