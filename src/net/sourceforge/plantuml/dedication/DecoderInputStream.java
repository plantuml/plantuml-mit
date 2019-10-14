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
package net.sourceforge.plantuml.dedication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DecoderInputStream extends InputStream {

	private final TurningBytes message;
	private final TurningBytes sha;
	private final Random rnd;
	private final InputStream source;

	public DecoderInputStream(InputStream source, String s) {
		this.source = source;
		try {
			final byte[] text = s.getBytes("UTF-8");
			final byte[] key = getSignatureSha512(text);
			this.rnd = new Random(getSeed(key));
			this.message = new TurningBytes(text);
			this.sha = new TurningBytes(key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	private static byte[] getSignatureSha512(byte[] bytes) {
		try {
			final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			msgDigest.update(bytes);
			return msgDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	private long getSeed(byte[] bytes) {
		long result = 17;
		for (byte b : bytes) {
			result = result * 37 + b;
		}
		return result;
	}

	private byte getNextByte() {
		return (byte) (rnd.nextInt() ^ message.nextByte() ^ sha.nextByte());
	}

	@Override
	public void close() throws IOException {
		source.close();
	}

	@Override
	public int read() throws IOException {
		int b = source.read();
		if (b == -1) {
			return -1;
		}
		b = (b ^ getNextByte()) & 0xFF;
		return b;
	}

}
