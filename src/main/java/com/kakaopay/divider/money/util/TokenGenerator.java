package com.kakaopay.divider.money.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Component
public class TokenGenerator {
	private final int tokenLength;
	private final char[] tokenLetters;
	private final SecureRandom secureRandom;

	/**
	 * @param tokenLength - 토큰 생성 길이 지정 (default: 3)
	 */
	public TokenGenerator(@Value("${token.generate-length:3}") int tokenLength) {
		this.tokenLength = tokenLength;
		this.tokenLetters = this.initializeToKenLetters();
		this.secureRandom = this.initializeSecureRandom();
	}

	/**
	 * 토큰 생성 시 사용할 문자 후보군 생성
	 *  - 출력가능한 특수문자, 알파벳, 숫자 포함
	 * @return - char[]
	 */
	private char[] initializeToKenLetters() {
		String letters = IntStream.rangeClosed(0x21, 0x7E)  // Ascii 기준 출력가능한 문자코드
				.mapToObj(i -> String.valueOf((char)i))
				.collect(Collectors.joining());

		return letters.toCharArray();
	}

	/**
	 * 랜덤 인덱스 생성을 위해 SecureRandom 생성
	 * @return - SecureRandom
	 */
	private SecureRandom initializeSecureRandom() {
		try {
			return SecureRandom.getInstance("SHA1PRNG");
		}
		catch (NoSuchAlgorithmException e) {
			return new SecureRandom();
		}
	}

	/**
	 * 랜덤 인덱스 생성
	 * @return - int
	 */
	private int nextRandomIndex() {
		this.secureRandom.setSeed(System.nanoTime());
		return Math.abs(this.secureRandom.nextInt()) % this.tokenLetters.length;
	}

	/**
	 * 랜덤 토큰 생성
	 * @return - String
	 */
	public String generateToken() {
		StringBuilder builder = new StringBuilder(this.tokenLength);
		for(int i = 0; i < this.tokenLength; i++) {
			int nextIdx = this.nextRandomIndex();
			builder.append(this.tokenLetters[nextIdx]);
		}
		return builder.toString();
	}
}
