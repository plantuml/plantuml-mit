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

public class TranscoderSmartProtected implements Transcoder {

	// Legacy encoder
	private final Transcoder oldOne = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionHuffman());
	private final Transcoder zlib = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionZlib());
	private final Transcoder hexOnly = TranscoderImpl.utf8(new AsciiEncoderHex(), new ArobaseStringCompressor(),
			new CompressionNone());

	public String decode(String code) throws NoPlantumlCompressionException {
		// Work in progress
		// See https://github.com/plantuml/plantuml/issues/117

		if (code.startsWith("~0")) {
			return decodeZlib(code.substring(2));
		}
		if (code.startsWith("~1")) {
			return decodeHuffman(code.substring(2));
		}
		if (code.startsWith("~h")) {
			return hexOnly.decode(code.substring(2));
		}

		return decodeZlib(code);
	}

	private String decodeZlib(String code) {
		try {
			return zlib.decode(code);
		} catch (Exception ex) {
			try {
				oldOne.decode(code);
				return textProtectedDeflate2(code);
			} catch (Exception ex2) {
				return textProtectedDeflate(code);
			}
		}
	}

	private String decodeHuffman(String code) {
		try {
			return oldOne.decode(code);
		} catch (Exception ex) {
			return textProtectedHuffman(code);
		}
	}

	private String textProtectedHuffman(String code) {
		final StringBuilder result = new StringBuilder();
		appendLine(result, "@startuml");
		appendLine(result, "legend");
		appendLine(result, "The plugin you are using seems to generated a bad URL.");
		appendLine(result, "This URL does not look like HUFFMAN data.");
		appendLine(result, "");
		appendLine(result, "See https://plantuml.com/pte");
		appendLine(result, "");
		appendLine(result, "You may contact the PlantUML team at plantuml@gmail.com");
		appendLine(result,
				"But you should also probably contact the plugin authors you are currently using and send them this image");
		appendLine(result, "");
		appendLine(result, "For the record, here is your data:");
		appendLine(result, "");
		appendURL(result, code);
		appendLine(result, "endlegend");
		appendLine(result, "@enduml");

		return result.toString();
	}

	private String textProtectedDeflate2(String code) {
		final StringBuilder result = new StringBuilder();
		final String codeshort = code.length() > 30 ? code.substring(0, 30) + "..." : code;
		appendLine(result, "@startuml");
		appendLine(result, "legend");
		appendLine(result, "The plugin you are using seems to generated a bad URL.");
		appendLine(result, "This URL does not look like DEFLATE data.");
		appendLine(result, "It looks like your plugin is using HUFFMAN encoding.");
		appendLine(result, "");
		appendLine(result,
				"This means you have now to add an header ~1 to your data. For example, you have to change:");
		appendLine(result, "http://www.plantuml.com/plantuml/png/" + codeshort);
		appendLine(result, "to");
		appendLine(result, "http://www.plantuml.com/plantuml/png/~1" + codeshort);
		appendLine(result, "");
		appendLine(result, "It will work this way");
		appendLine(result, "You may contact the PlantUML team at plantuml@gmail.com");
		appendLine(result,
				"But you should also probably contact the plugin authors you are currently using and send them this image");
		appendLine(result, "");
		appendLine(result, "For the record, here is your data:");
		appendLine(result, "");
		appendURL(result, code);
		appendLine(result, "endlegend");
		appendLine(result, "@enduml");

		return result.toString();
	}

	private String textProtectedDeflate(String code) {
		final StringBuilder result = new StringBuilder();
		appendLine(result, "@startuml");
		appendLine(result, "legend");
		appendLine(result, "The plugin you are using seems to generated a bad URL.");
		appendLine(result, "This URL does not look like DEFLATE data.");
		appendLine(result, "");
		appendLine(result, "See https://plantuml.com/pte");
		appendLine(result, "");
		appendLine(result, "You may contact the PlantUML team at plantuml@gmail.com");
		appendLine(result,
				"But you should also probably contact the plugin authors you are currently using and send them this image");
		appendLine(result, "");
		appendLine(result, "For the record, here is your data:");
		appendLine(result, "");
		appendURL(result, code);
		appendLine(result, "endlegend");
		appendLine(result, "@enduml");

		return result.toString();
	}

	private void appendURL(StringBuilder result, String url) {
		while (url.length() > 80) {
			appendLine(result, url.substring(0, 80));
			url = url.substring(80);
		}
		if (url.length() > 0) {
			appendLine(result, url);
		}
	}

	private void appendLine(StringBuilder sb, String s) {
		sb.append(s);
		sb.append("\n");
	}

	public String encode(String text) throws IOException {
		// Right now, we still use the legacy encoding.
		// This will be changed in the incoming months
		return zlib.encode(text);
	}
}
