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
package net.sourceforge.plantuml.eps;

import java.io.UnsupportedEncodingException;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UFontContext;

public class EpsGraphicsMacroAndText extends EpsGraphicsMacro {

	public void drawText(String text, FontConfiguration fontConfiguration, double x, double y) {
		append(format(x) + " " + format(y) + " moveto", true);
		appendColor(getColor());
		final UFont font = fontConfiguration.getFont();
		final int size = font.getSize();
		append("/" + getPSName(fontConfiguration) + " findfont " + size + " scalefont setfont", true);
		append("100 -100 scale", true);
		append("(" + getTextAsEps(text) + ") show", false);
		append(".01 -.01 scale", true);
	}

	private String getPSName(FontConfiguration fontConfiguration) {
		final UFont font = fontConfiguration.getFont();
		final StringBuilder sb = new StringBuilder(font.getFamily(UFontContext.EPS));
		// final int style = fontConfiguration.getFont().getStyle();
		// final boolean bold = (style & Font.BOLD) != 0 || fontConfiguration.containsStyle(FontStyle.BOLD);
		// final boolean italic = (style & Font.ITALIC) != 0 || fontConfiguration.containsStyle(FontStyle.ITALIC);
		// if (bold && italic) {
		// sb.append("-BoldItalic");
		// } else if (bold) {
		// sb.append("-Bold");
		// } else if (italic) {
		// sb.append("-Italic");
		// }
		return sb.toString();
	}

	private String getTextAsEps(String text) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			if (c == '\\') {
				sb.append("\\\\");
			} else if (c == '(') {
				sb.append("\\(");
			} else if (c == ')') {
				sb.append("\\)");
			} else if (c < ' ') {
				sb.append("?");
			} else if (c >= ' ' && c <= 127) {
				sb.append(c);
			} else {
				final String s = "" + c;
				try {
					final byte b[] = s.getBytes("ISO-8859-1");
					if (b.length == 1) {
						final int code = b[0] & 0xFF;
						sb.append("\\" + Integer.toOctalString(code));
					} else {
						sb.append('?');
					}
				} catch (UnsupportedEncodingException e) {
					sb.append('?');
				}
			}
		}
		return sb.toString();
	}

}
