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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class UrlBuilder {

	public static enum ModeUrl {
		STRICT, ANYWHERE
	}

	// private static String level0() {
	// return "(?:[^{}]|\\{[^{}]*\\})+";
	// }
	//
	// private static String levelN(int n) {
	// if (n == 0) {
	// return level0();
	// }
	// return "(?:[^{}]|\\{" + levelN(n - 1) + "\\})+";
	// }

	// private static final String URL_PATTERN_OLD =
	// "\\[\\[([%g][^%g]+[%g]|[^{}%s\\]\\[]*)(?:[%s]*\\{((?:[^{}]|\\{[^{}]*\\})+)\\})?(?:[%s]*([^\\]\\[]+))?\\]\\]";
	private static final String URL_PATTERN = "\\[\\[([%g][^%g]+[%g])?([\\w\\W]*?)\\]\\]";

	// private static final String URL_PATTERN_BAD = "\\[\\[([%g][^%g]+[%g]|[^{}%s\\]\\[]*)(?:[%s]*\\{" + "(" +
	// levelN(3)
	// + ")" + "\\})?(?:[%s]*([^\\]\\[]+))?\\]\\]";

	private final String topurl;
	private ModeUrl mode;

	public UrlBuilder(String topurl, ModeUrl mode) {
		this.topurl = topurl;
		this.mode = mode;
	}

//	private static String multilineTooltip(String label) {
//		final Pattern2 p = MyPattern.cmpile("(?i)^(" + URL_PATTERN + ")?(.*)$");
//		final Matcher2 m = p.matcher(label);
//		if (m.matches() == false) {
//			return label;
//		}
//		String gr1 = m.group(1);
//		if (gr1 == null) {
//			return label;
//		}
//		final String gr2 = m.group(m.groupCount());
//		gr1 = gr1.replaceAll("\\\\n", BackSlash.BS_N);
//		return gr1 + gr2;
//	}

	public Url getUrl(String s) {
		final Pattern2 p;
		if (mode == ModeUrl.STRICT) {
			p = MyPattern.cmpile("(?i)^" + URL_PATTERN + "$");
		} else if (mode == ModeUrl.ANYWHERE) {
			p = MyPattern.cmpile("(?i).*" + URL_PATTERN + ".*");
		} else {
			throw new IllegalStateException();
		}
		final Matcher2 m = p.matcher(StringUtils.trinNoTrace(s));
		if (m.matches() == false) {
			return null;
		}
		// String url = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m.group(1));
		// if (url.startsWith("http:") == false && url.startsWith("https:") == false) {
		// // final String top = getSystem().getSkinParam().getValue("topurl");
		// if (topurl != null) {
		// url = topurl + url;
		// }
		// }

		final String quotedPart = m.group(1);
		final String full = m.group(2);
		final int openBracket = full.indexOf('{');
		final int closeBracket = full.lastIndexOf('}');
		if (quotedPart == null) {
			if (openBracket != -1 && closeBracket != -1) {
				return new Url(withTopUrl(full.substring(0, openBracket)),
						full.substring(openBracket + 1, closeBracket), full.substring(closeBracket + 1).trim());
			}
			final int firstSpace = full.indexOf(' ');
			if (firstSpace == -1) {
				return new Url(full, null, null);
			}
			return new Url(withTopUrl(full.substring(0, firstSpace)), null, full.substring(firstSpace + 1).trim());
		}
		if (openBracket != -1 && closeBracket != -1) {
			return new Url(withTopUrl(quotedPart), full.substring(openBracket + 1, closeBracket), full.substring(
					closeBracket + 1).trim());
		}
		return new Url(withTopUrl(quotedPart), null, null);
	}

	private String withTopUrl(String url) {
		if (url.startsWith("http:") == false && url.startsWith("https:") == false && topurl != null) {
			return topurl + url;
		}
		return url;
	}

	public static String getRegexp() {
		return URL_PATTERN;
	}

	// private static String purgeUrl(final String label) {
	// final Pattern2 p = MyPattern.cmpile("[%s]*" + URL_PATTERN + "[%s]*");
	// final Matcher2 m = p.matcher(label);
	// if (m.find() == false) {
	// return label;
	// }
	// final String url = m.group(0);
	// final int x = label.indexOf(url);
	// return label.substring(0, x) + label.substring(x + url.length());
	// }

}
