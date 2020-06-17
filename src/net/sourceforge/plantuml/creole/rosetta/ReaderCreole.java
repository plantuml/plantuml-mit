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
import java.util.Collections;
import java.util.List;

public class ReaderCreole extends ReaderAbstractWiki implements ReaderWiki {

	@Override
	protected String singleLineFormat(String wiki) {

		// Legacy HTML
		wiki = wiki.replace("<b>", WikiLanguage.UNICODE.tag("strong"));
		wiki = wiki.replace("</b>", WikiLanguage.UNICODE.slashTag("strong"));
		wiki = wiki.replace("<i>", WikiLanguage.UNICODE.tag("em"));
		wiki = wiki.replace("</i>", WikiLanguage.UNICODE.slashTag("em"));

		// Em & Strong
		wiki = wiki.replaceAll("\\*\\*(.+?)\\*\\*",
				WikiLanguage.UNICODE.tag("strong") + "$1" + WikiLanguage.UNICODE.slashTag("strong"));
		wiki = wiki.replaceAll("//(.+?)//",
				WikiLanguage.UNICODE.tag("em") + "$1" + WikiLanguage.UNICODE.slashTag("em"));

		// Strike
		wiki = wiki.replaceAll("--([^-]+?)--",
				WikiLanguage.UNICODE.tag("strike") + "$1" + WikiLanguage.UNICODE.slashTag("strike"));

		return wiki;
	}

	public List<String> transform(List<String> raw) {
		final List<String> uhtml = new ArrayList<String>();
		for (int i = 0; i < raw.size(); i++) {
			String current = raw.get(i);
			uhtml.add(singleLineFormat(current));
		}
		return Collections.unmodifiableList(uhtml);
	}

}
