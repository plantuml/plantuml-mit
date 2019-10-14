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
import java.io.OutputStream;
import java.math.BigInteger;

public class QBlock {

	private final BigInteger big;

	public static QBlock read(InputStream source, int size) throws IOException {
		final byte[] block = new byte[size + 1];
		for (int i = 0; i < size; i++) {
			final int read = source.read();
			if (read == -1) {
				if (i == 0) {
					return null;
				}
				break;
			}
			block[i + 1] = (byte) read;
		}
		return new QBlock(new BigInteger(block));
	}

	public static QBlock fromBuffer(final byte[] buffer) {
		final byte[] block = new byte[buffer.length + 1];
		System.arraycopy(buffer, 0, block, 1, buffer.length);
		final BigInteger big = new BigInteger(block);
		return new QBlock(big);
	}

	public QBlock(BigInteger number) {
		this.big = number;
	}

	public QBlock change(BigInteger E, BigInteger N) {
		final BigInteger changed = big.modPow(E, N);
		return new QBlock(changed);
	}

	public byte[] getData512() {
		final byte[] nb = big.toByteArray();
		if (nb.length == 512) {
			return nb;
		}
		final byte[] result = new byte[512];
		if (nb.length < 512) {
			System.arraycopy(nb, 0, result, 512 - nb.length, nb.length);
		} else {
			System.arraycopy(nb, nb.length - 512, result, 0, 512);
		}
		return result;
	}

	public byte[] getDataRaw() {
		return big.toByteArray();
	}

	@Override
	public String toString() {
		return big.toByteArray().length + " " + big.toString(36);
	}

	public void write(OutputStream os, int size) throws IOException {
		final byte[] data = big.toByteArray();
		final int start = data.length - size;
		if (start < 0) {
			for (int i = 0; i < -start; i++) {
				os.write(0);
			}
		}
		for (int i = Math.max(start, 0); i < data.length; i++) {
			int b = data[i];
			os.write(b);
		}

	}

}
