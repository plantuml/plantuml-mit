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

import java.io.IOException;

public class TranscoderImpl implements Transcoder {

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;

//	private TranscoderImpl() {
//		this(new AsciiEncoder(), new StringCompressorNone(), new CompressionHuffman());
//	}
//
//	private TranscoderImpl(Compression compression) {
//		this(new AsciiEncoder(), new StringCompressorNone(), compression);
//	}
//
//	private TranscoderImpl(URLEncoder urlEncoder, Compression compression) {
//		this(urlEncoder, new ArobaseStringCompressor(), compression);
//	}

	public TranscoderImpl(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
	}

	public String encode(String text) throws IOException {

		final String stringAnnoted = stringCompressor.compress(text);

		final byte[] data = stringAnnoted.getBytes("UTF-8");
		final byte[] compressedData = compression.compress(data);

		return urlEncoder.encode(compressedData);
	}

	public String decode(String code) throws IOException {
		final byte compressedData[] = urlEncoder.decode(code);
		final byte data[] = compression.decompress(compressedData);

		return stringCompressor.decompress(new String(data, "UTF-8"));
	}

}
