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
package net.sourceforge.plantuml.creole.rosetta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;

public class Rosetta {

	private final List<String> unicodeHtml;

	public static Rosetta fromUnicodeHtml(List<String> lines) {
		return new Rosetta(lines);
	}

	public static Rosetta fromSyntax(WikiLanguage syntaxSource, String... wiki) {
		return new Rosetta(syntaxSource, Arrays.asList(wiki));
	}

	public static Rosetta fromSyntax(WikiLanguage syntaxSource, List<String> wiki) {
		return new Rosetta(syntaxSource, wiki);
	}

	public static Rosetta fromSyntax(WikiLanguage syntaxSource, Display display) {
		return new Rosetta(syntaxSource, from(display));
	}

	private static List<String> from(Display display) {
		final List<String> result = new ArrayList<String>();
		for (CharSequence cs : display) {
			result.add(cs.toString());
		}
		return result;
	}

	private Rosetta(List<String> lines) {
		this.unicodeHtml = new ArrayList<String>(lines);
	}

	private Rosetta(WikiLanguage syntaxSource, List<String> wiki) {
		final ReaderWiki reader;
		if (syntaxSource == WikiLanguage.DOKUWIKI) {
			reader = new ReaderDokuwiki();
		} else if (syntaxSource == WikiLanguage.CREOLE) {
			reader = new ReaderCreole();
//			} else if (syntaxSource == WikiLanguage.MARKDOWN) {
//			reader = new ReaderMarkdown();
//		} else if (syntaxSource == WikiLanguage.ASCIIDOC) {
//			reader = new ReaderAsciidoc();
		} else {
			throw new UnsupportedOperationException();
		}
		this.unicodeHtml = reader.transform(wiki);
	}

	public List<String> translateTo(WikiLanguage syntaxDestination) {
		final List<String> html = new ArrayList<String>();
		final WriterWiki writer = new WriterWiki(syntaxDestination);
		html.addAll(writer.transform(unicodeHtml));
		return Collections.unmodifiableList(html);
	}

}
