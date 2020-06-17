/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.code;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Upf9Decoder {

	private Upf9Decoder() {
	}

	static int decodeChar(InputStream is) throws IOException {
		final int read0 = is.read();
		if (read0 == -1) {
			return -1;
		}
		if (read0 == 0x0B) {
			final int read1 = is.read();
			if (read1 >= 0x80)
				return (char) read1;
			return (char) ((0xE0 << 8) + read1);
		}
		if (read0 == 0x0C) {
			final int read1 = is.read();
			final int read2 = is.read();
			return (char) ((read1 << 8) + read2);
		}
		if (read0 >= 0x01 && read0 <= 0x08) {
			final int read1 = is.read();
			return (char) ((read0 << 8) + read1);
		}
		if (read0 >= 0x80 && read0 <= 0xFF) {
			final int read1 = is.read();
			return (char) (((read0 - 0x60) << 8) + read1);
		}
		return (char) read0;
	}

	public static String decodeString(byte[] data, int length) throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, length);
		final StringBuilder result = new StringBuilder();
		int read;
		while ((read = decodeChar(bais)) != -1) {
			result.append((char) read);
		}
		bais.close();
		return result.toString();
	}

}
