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
package net.sourceforge.plantuml.graphic;

import java.util.EnumSet;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

class HtmlCommandFactory {

	static final Pattern2 addStyle;
	static final Pattern2 removeStyle;

	static {
		final StringBuilder sbAddStyle = new StringBuilder();
		final StringBuilder sbRemoveStyle = new StringBuilder();

		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
			if (sbAddStyle.length() > 0) {
				sbAddStyle.append('|');
				sbRemoveStyle.append('|');
			}
			sbAddStyle.append(style.getActivationPattern());
			sbRemoveStyle.append(style.getDeactivationPattern());
		}

		addStyle = MyPattern.cmpile(sbAddStyle.toString(), Pattern.CASE_INSENSITIVE);
		removeStyle = MyPattern.cmpile(sbRemoveStyle.toString(), Pattern.CASE_INSENSITIVE);
	}

	private Pattern2 htmlTag = MyPattern.cmpile(Splitter.htmlTag, Pattern.CASE_INSENSITIVE);

	HtmlCommand getHtmlCommand(String s) {
		if (htmlTag.matcher(s).matches() == false) {
			return new Text(s);
		}
		if (MyPattern.mtches(s, Splitter.imgPattern)) {
			return Img.getInstance(s, true);
		}

		if (MyPattern.mtches(s, Splitter.imgPatternNoSrcColon)) {
			return Img.getInstance(s, false);
		}

		if (addStyle.matcher(s).matches()) {
			return new AddStyle(s);
		}
		if (removeStyle.matcher(s).matches()) {
			return new RemoveStyle(FontStyle.getStyle(s));
		}

		if (MyPattern.mtches(s, Splitter.fontPattern)) {
			return new ColorAndSizeChange(s);
		}

		if (MyPattern.mtches(s, Splitter.fontColorPattern2)) {
			return new ColorChange(s);
		}

		if (MyPattern.mtches(s, Splitter.fontSizePattern2)) {
			return new SizeChange(s);
		}

		if (MyPattern.mtches(s, Splitter.fontSup)) {
			return new ExposantChange(FontPosition.EXPOSANT);
		}

		if (MyPattern.mtches(s, Splitter.fontSub)) {
			return new ExposantChange(FontPosition.INDICE);
		}

		if (MyPattern.mtches(s, Splitter.endFontPattern)) {
			return new ResetFont();
		}

		if (MyPattern.mtches(s, Splitter.endSupSub)) {
			return new ExposantChange(FontPosition.NORMAL);
		}

		if (MyPattern.mtches(s, Splitter.fontFamilyPattern)) {
			return new FontFamilyChange(s);
		}

		if (MyPattern.mtches(s, Splitter.spritePatternForMatch)) {
			return new SpriteCommand(s);
		}

		if (MyPattern.mtches(s, Splitter.linkPattern)) {
			final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(s);
			url.setMember(true);
			return new TextLink(url);
		}

		if (MyPattern.mtches(s, Splitter.svgAttributePattern)) {
			return new SvgAttributesChange(s);
		}

		return null;
	}

}
