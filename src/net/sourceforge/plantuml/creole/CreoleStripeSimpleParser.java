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
package net.sourceforge.plantuml.creole;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.utils.CharHidder;

public class CreoleStripeSimpleParser {

	final private String line;
	final private StripeStyle style;
	private final CreoleMode modeSimpleLine;

	private final FontConfiguration fontConfiguration;
	private final ISkinSimple skinParam;

	public CreoleStripeSimpleParser(String line, CreoleContext creoleContext, FontConfiguration fontConfiguration,
			ISkinSimple skinParam, CreoleMode modeSimpleLine) {
		if (line.contains("" + BackSlash.hiddenNewLine())) {
			throw new IllegalArgumentException(line);
		}
		this.fontConfiguration = fontConfiguration;
		this.modeSimpleLine = modeSimpleLine;
		this.skinParam = skinParam;
		if (skinParam == null) {
			throw new IllegalArgumentException();
		}

		final Pattern2 p4 = MyPattern.cmpile("^--([^-]*)--$");
		final Matcher2 m4 = p4.matcher(line);
		if (m4.find()) {
			this.line = m4.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '-');
			return;
		}

		final Pattern2 p5 = MyPattern.cmpile("^==([^=]*)==$");
		final Matcher2 m5 = p5.matcher(line);
		if (m5.find()) {
			this.line = m5.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}
		final Pattern2 p5b = MyPattern.cmpile("^===*==$");
		final Matcher2 m5b = p5b.matcher(line);
		if (m5b.find()) {
			this.line = "";
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}

		// if (modeSimpleLine == CreoleMode.FULL) {
		// final Pattern p6 = MyPattern.cmpile("^__([^_]*)__$");
		// final Matcher m6 = p6.matcher(line);
		// if (m6.find()) {
		// this.line = m6.group(1);
		// this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '_');
		// return;
		// }
		// }

		final Pattern2 p7 = MyPattern.cmpile("^\\.\\.([^\\.]*)\\.\\.$");
		final Matcher2 m7 = p7.matcher(line);
		if (m7.find()) {
			this.line = m7.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '.');
			return;
		}

		if (modeSimpleLine == CreoleMode.FULL) {
			final Pattern2 p1 = MyPattern.cmpile("^(\\*+)([^*]+(?:[^*]|\\*\\*[^*]+\\*\\*)*)$");
			final Matcher2 m1 = p1.matcher(line);
			if (m1.find()) {
				this.line = StringUtils.trin(m1.group(2));
				final int order = m1.group(1).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITHOUT_NUMBER, order, '\0');
				return;
			}
		}

		if (modeSimpleLine == CreoleMode.FULL) {
			final Pattern2 p2 = MyPattern.cmpile("^(#+)(.+)$");
			final Matcher2 m2 = p2.matcher(CharHidder.hide(line));
			if (m2.find()) {
				this.line = StringUtils.trin(CharHidder.unhide(m2.group(2)));
				final int order = CharHidder.unhide(m2.group(1)).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITH_NUMBER, order, '\0');
				return;
			}
		}

		final Pattern2 p3 = MyPattern.cmpile("^(=+)(.+)$");
		final Matcher2 m3 = p3.matcher(line);
		if (m3.find()) {
			this.line = StringUtils.trin(m3.group(2));
			final int order = m3.group(1).length() - 1;
			this.style = new StripeStyle(StripeStyleType.HEADING, order, '\0');
			return;
		}

		this.line = line;
		this.style = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	}

	public Stripe createStripe(CreoleContext context) {
		final StripeSimple result = new StripeSimple(fontConfiguration, style, context, skinParam, modeSimpleLine);
		result.analyzeAndAdd(line);
		return result;
	}

}
