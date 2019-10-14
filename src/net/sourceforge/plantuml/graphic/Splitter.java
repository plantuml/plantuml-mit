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
package net.sourceforge.plantuml.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.sprite.SpriteUtils;

public class Splitter {

	static final String endFontPattern = "\\</font\\>|\\</color\\>|\\</size\\>|\\</text\\>";
	static final String endSupSub = "\\</sup\\>|\\</sub\\>";
	public static final String fontPattern = "\\<font(\\s+size[%s]*=[%s]*[%g]?\\d+[%g]?|[%s]+color[%s]*=\\s*[%g]?(#[0-9a-fA-F]{6}|\\w+)[%g]?)+[%s]*\\>";
	public static final String fontColorPattern2 = "\\<color[\\s:]+(#[0-9a-fA-F]{6}|#?\\w+)[%s]*\\>";
	public static final String fontSizePattern2 = "\\<size[\\s:]+(\\d+)[%s]*\\>";
	static final String fontSup = "\\<sup\\>";
	static final String fontSub = "\\<sub\\>";
	public static final String qrcodePattern = "\\<qrcode[\\s:]+([^>{}]+)" + "(\\{scale=(?:[0-9.]+)\\})?" + "\\>";
	static final String imgPattern = "\\<img\\s+(src[%s]*=[%s]*[%q%g]?[^\\s%g>]+[%q%g]?[%s]*|vspace\\s*=\\s*[%q%g]?\\d+[%q%g]?\\s*|valign[%s]*=[%s]*[%q%g]?(top|middle|bottom)[%q%g]?[%s]*)+\\>";
	public static final String imgPatternNoSrcColon = "\\<img[\\s:]+([^>{}]+)" + "(\\{scale=(?:[0-9.]+)\\})?" + "\\>";
	public static final String fontFamilyPattern = "\\<font[\\s:]+([^>]+)/?\\>";
	public static final String svgAttributePattern = "\\<text[\\s:]+([^>]+)/?\\>";

	private static final String scale2 = "(" + "(?:\\{scale=|\\*)[0-9.]+\\}?" + ")?";
	private static final String scale = "(" + //
			"[\\{,]?" + //
			"(?:(?:scale=|\\*)[0-9.]+)?" + //
			"(?:,color[= :](?:#[0-9a-fA-F]{6}|\\w+))?" + //
			"\\}?" + //
			")?";

	public static final String openiconPattern = "\\<&([-\\w]+)" + scale + "\\>";
	public static final String spritePattern2 = "\\<\\$(" + SpriteUtils.SPRITE_NAME + ")" + scale + "\\>";

	public static final String spritePatternForMatch = spritePattern2;
	// "\\<\\$" + SpriteUtils.SPRITE_NAME + "(?:\\{scale=(?:[0-9.]+)\\})?" + "\\>";

	static final String htmlTag;

	static final String linkPattern = "\\[\\[([^\\[\\]]+)\\]\\]";
	public static final String mathPattern = "\\<math\\>(.+?)\\</math\\>";
	public static final String latexPattern = "\\<latex\\>(.+?)\\</latex\\>";

	private static final Pattern2 tagOrText;

	static {
		final StringBuilder sb = new StringBuilder("(?i)");

		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
			sb.append(style.getActivationPattern());
			sb.append('|');
			sb.append(style.getDeactivationPattern());
			sb.append('|');
		}
		sb.append(fontPattern);
		sb.append('|');
		sb.append(fontColorPattern2);
		sb.append('|');
		sb.append(fontSizePattern2);
		sb.append('|');
		sb.append(fontSup);
		sb.append('|');
		sb.append(fontSub);
		sb.append('|');
		sb.append(endFontPattern);
		sb.append('|');
		sb.append(endSupSub);
		sb.append('|');
		sb.append(qrcodePattern);
		sb.append('|');
		sb.append(imgPattern);
		sb.append('|');
		sb.append(imgPatternNoSrcColon);
		sb.append('|');
		sb.append(fontFamilyPattern);
		sb.append('|');
		// sb.append(spritePattern);
		// sb.append('|');
		sb.append(linkPattern);
		sb.append('|');
		sb.append(svgAttributePattern);

		htmlTag = sb.toString();
		tagOrText = MyPattern.cmpile(htmlTag + "|.+?(?=" + htmlTag + ")|.+$", Pattern.CASE_INSENSITIVE);
	}

	private final List<String> splitted = new ArrayList<String>();

	public Splitter(String s) {
		final Matcher2 matcher = tagOrText.matcher(s);
		while (matcher.find()) {
			String part = matcher.group(0);
			part = StringUtils.showComparatorCharacters(part);
			splitted.add(part);
		}
	}

	List<String> getSplittedInternal() {
		return splitted;
	}

	public List<HtmlCommand> getHtmlCommands(boolean newLineAlone) {
		final HtmlCommandFactory factory = new HtmlCommandFactory();
		final List<HtmlCommand> result = new ArrayList<HtmlCommand>();
		for (String s : getSplittedInternal()) {
			final HtmlCommand cmd = factory.getHtmlCommand(s);
			if (newLineAlone && cmd instanceof Text) {
				result.addAll(splitText((Text) cmd));
			} else {
				result.add(cmd);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private Collection<Text> splitText(Text cmd) {
		String s = cmd.getText();
		final Collection<Text> result = new ArrayList<Text>();
		while (true) {
			final int x = s.indexOf(Text.TEXT_BS_BS_N.getText());
			if (x == -1) {
				result.add(new Text(s));
				return result;
			}
			if (x > 0) {
				result.add(new Text(s.substring(0, x)));
			}
			result.add(Text.TEXT_BS_BS_N);
			s = s.substring(x + 2);
		}
	}
}
