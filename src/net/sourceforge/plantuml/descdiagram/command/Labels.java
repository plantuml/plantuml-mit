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
 * Contribution :  Hisashi Miyashita
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LinkArrow;

public class Labels {

	private String firstLabel;
	private String secondLabel;
	private final StringWithArrow stringWithArrow;

	public Labels(RegexResult arg) {
		this.firstLabel = arg.get("FIRST_LABEL", 0);
		this.secondLabel = arg.get("SECOND_LABEL", 0);
		String labelLink = arg.get("LABEL_LINK", 0);

		if (labelLink != null) {
			labelLink = init(labelLink);
		}

		this.stringWithArrow = new StringWithArrow(labelLink);

	}

	private String init(String labelLink) {
		if (firstLabel == null && secondLabel == null) {
			final Pattern2 p1 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)[%g]([^%g]+)[%g]$");
			final Matcher2 m1 = p1.matcher(labelLink);
			if (m1.matches()) {
				firstLabel = m1.group(1);
				secondLabel = m1.group(3);
				return StringUtils
						.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m1.group(2))));
			}
			final Pattern2 p2 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)$");
			final Matcher2 m2 = p2.matcher(labelLink);
			if (m2.matches()) {
				firstLabel = m2.group(1);
				secondLabel = null;
				return StringUtils
						.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m2.group(2))));
			}
			final Pattern2 p3 = MyPattern.cmpile("^([^%g]+)[%g]([^%g]+)[%g]$");
			final Matcher2 m3 = p3.matcher(labelLink);
			if (m3.matches()) {
				firstLabel = null;
				secondLabel = m3.group(2);
				return StringUtils
						.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m3.group(1))));
			}
		}
		return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink, "\"");
	}

	public final String getFirstLabel() {
		return firstLabel;
	}

	public final String getSecondLabel() {
		return secondLabel;
	}

	public final String getLabelLink() {
		return stringWithArrow.getLabel();
	}

	public final LinkArrow getLinkArrow() {
		return stringWithArrow.getLinkArrow();
	}

	public Display getDisplay() {
		return stringWithArrow.getDisplay();
	}

}
