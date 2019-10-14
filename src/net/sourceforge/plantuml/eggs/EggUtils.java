/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * Licensed under The MIT License (Massachusetts Institute of Technology License)
 * 
 * See http://opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml.eggs;

import java.math.BigInteger;

import net.sourceforge.plantuml.StringUtils;

public class EggUtils {

	public static String fromByteArrays(byte data[]) {
		final StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			final String hex = Integer.toHexString(b & 0xFF);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static byte[] toByteArrays(String s) {
		final byte[] result = new byte[s.length() / 2];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
		}
		return result;
	}

	public static BigInteger fromSecretSentence(String s) {
		BigInteger result = BigInteger.ZERO;
		final BigInteger twentySix = BigInteger.valueOf(26);
		s = s.replace('\u00E9', 'e');
		s = s.replace('\u00EA', 'e');
		for (char c : s.toCharArray()) {
			final int num = convertChar(c);
			if (num != -1) {
				result = result.multiply(twentySix);
				result = result.add(BigInteger.valueOf(num));

			}
		}
		return result;

	}

	private static int convertChar(char c) {
		c = StringUtils.goLowerCase(c);
		if (c >= 'a' && c <= 'z') {
			return c - 'a';
		}
		return -1;
	}

	public static byte[] xor(byte data[], byte key[]) {
		final byte[] result = new byte[data.length];
		int pos = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (data[i] ^ key[pos++]);
			if (pos == key.length) {
				pos = 0;
			}

		}
		return result;
	}

}
