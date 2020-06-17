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
package net.sourceforge.plantuml.dedication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class QBlocks {

	private final List<QBlock> all = new ArrayList<QBlock>();

	private QBlocks() {

	}

	public static QBlocks readFrom(InputStream source, int size) throws IOException {
		final QBlocks result = new QBlocks();
		while (true) {
			final QBlock block = QBlock.read(source, size);
			if (block == null) {
				return result;
			}
			result.all.add(block);
		}
	}

	public QBlocks change(BigInteger E, BigInteger N) {
		final QBlocks result = new QBlocks();
		for (QBlock rsa : all) {
			result.all.add(rsa.change(E, N));
		}
		return result;
	}

	public void writeTo(OutputStream os, int size) throws IOException {
		for (QBlock rsa : all) {
			rsa.write(os, size);
		}
	}

//	public String encodeAscii() {
//		final StringBuilder sb = new StringBuilder();
//		final AsciiEncoder encoder = new AsciiEncoder();
//		for (QBlock rsa : all) {
//			sb.append(encoder.encode(rsa.getDataRaw()));
//			sb.append("!");
//		}
//		return sb.toString();
//	}

//	public static QBlocks descodeAscii(String s) {
//		final QBlocks result = new QBlocks();
//		final AsciiEncoder encoder = new AsciiEncoder();
//		for (String bl : s.split("!")) {
//			final BigInteger bigInteger = new BigInteger(encoder.decode(bl));
//			result.all.add(new QBlock(bigInteger));
//
//		}
//		return result;
//	}
}
