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

import java.io.IOException;

public class TranscoderSmartAttic implements Transcoder {

	// Legacy encoder
	private final Transcoder oldOne = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionHuffman());
	private final Transcoder zlib = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionZlib());
	private final Transcoder brotli = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionBrotli());

	private final Transcoder zlibBase64 = TranscoderImpl.utf8(new AsciiEncoderBase64(), new ArobaseStringCompressor(),
			new CompressionZlib());
	private final Transcoder brotliBase64 = TranscoderImpl.utf8(new AsciiEncoderBase64(), new ArobaseStringCompressor(),
			new CompressionBrotli());
	private final Transcoder base64only = TranscoderImpl.utf8(new AsciiEncoderBase64(), new ArobaseStringCompressor(),
			new CompressionNone());
	private final Transcoder hexOnly = TranscoderImpl.utf8(new AsciiEncoderHex(), new ArobaseStringCompressor(),
			new CompressionNone());

	public String decode(String code) throws NoPlantumlCompressionException {
		// Work in progress
		// See https://github.com/plantuml/plantuml/issues/117

		// Two char headers
		if (code.startsWith("0A")) {
			return zlibBase64.decode(code.substring(2));
		}
		if (code.startsWith("0B")) {
			return brotliBase64.decode(code.substring(2));
		}
		if (code.startsWith("0C")) {
			return base64only.decode(code.substring(2));
		}
		if (code.startsWith("0D")) {
			return hexOnly.decode(code.substring(2));
		}
		// Text prefix
		// Just a wild try: use them only for testing
		if (code.startsWith("-deflate-")) {
			return zlibBase64.decode(code.substring("-deflate-".length()));
		}
		if (code.startsWith("-brotli-")) {
			return brotliBase64.decode(code.substring("-brotli-".length()));
		}
		if (code.startsWith("-base64-")) {
			return base64only.decode(code.substring("-base64-".length()));
		}
		if (code.startsWith("-hex-")) {
			return hexOnly.decode(code.substring("-hex-".length()));
		}

		// Legacy decoding : you should not use it any more.
		if (code.startsWith("0")) {
			return brotli.decode(code.substring(1));
		}
		try {
			return zlib.decode(code);
		} catch (Exception ex) {
			return oldOne.decode(code);
		}
		// return zlib.decode(code);
	}

	public String encode(String text) throws IOException {
		// Right now, we still use the legacy encoding.
		// This will be changed in the incoming months
		return zlib.encode(text);
	}
}
