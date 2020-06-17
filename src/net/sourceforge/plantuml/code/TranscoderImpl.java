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

public class TranscoderImpl implements Transcoder {

	static enum Format {
		UTF8, UPF9;
	}

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;
	private final Format format;

	private TranscoderImpl(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression,
			Format format) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
		this.format = format;
	}

	public static Transcoder utf8(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		return new TranscoderImpl(urlEncoder, stringCompressor, compression, Format.UTF8);
	}

	public static Transcoder upf9(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		return new TranscoderImpl(urlEncoder, stringCompressor, compression, Format.UPF9);
	}

	public String encode(String text) throws IOException {
		final String stringAnnoted = stringCompressor.compress(text);
		final byte[] data;
		if (format == Format.UTF8)
			data = stringAnnoted.getBytes("UTF-8");
		else
			data = Upf9Encoder.getBytes(stringAnnoted);

		final byte[] compressedData = compression.compress(data);

		return urlEncoder.encode(compressedData);
	}

	public String decode(String code) throws NoPlantumlCompressionException {
		try {
			final byte compressedData[] = urlEncoder.decode(code);
			final ByteArray data = compression.decompress(compressedData);
			final String string = format == Format.UTF8 ? data.toUFT8String() : data.toUPF9String();
			return stringCompressor.decompress(string);
		} catch (Exception e) {
			System.err.println("Cannot decode string");
			throw new NoPlantumlCompressionException(e);
		}
	}

}
