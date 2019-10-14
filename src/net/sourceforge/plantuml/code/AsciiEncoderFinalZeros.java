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
package net.sourceforge.plantuml.code;

public class AsciiEncoderFinalZeros {

	public String encode(byte data[]) {
		if (data == null) {
			return "";
		}
		final StringBuilder result = new StringBuilder((data.length * 4 + 2) / 3);
		for (int i = 0; i < data.length; i += 3) {
			append3bytes(result, data[i] & 0xFF, i + 1 < data.length ? data[i + 1] & 0xFF : 0,
					i + 2 < data.length ? data[i + 2] & 0xFF : 0);
		}
		while (result.length() > 0 && result.charAt(result.length() - 1) == '0') {
			result.setLength(result.length() - 1);
		}
		return result.toString();
	}

	private void append3bytes(StringBuilder sb, int b1, int b2, int b3) {
		final int c1 = b1 >> 2;
		final int c2 = ((b1 & 0x3) << 4) | (b2 >> 4);
		final int c3 = ((b2 & 0xF) << 2) | (b3 >> 6);
		final int c4 = b3 & 0x3F;
		sb.append(AsciiEncoder.encode6bit[c1 & 0x3F]);
		sb.append(AsciiEncoder.encode6bit[c2 & 0x3F]);
		sb.append(AsciiEncoder.encode6bit[c3 & 0x3F]);
		sb.append(AsciiEncoder.encode6bit[c4 & 0x3F]);
	}

}
