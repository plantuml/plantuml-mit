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
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockVertical2;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class BodyEnhanced extends AbstractTextBlock implements TextBlock, WithPorts {

	private TextBlock area2;
	private final FontConfiguration titleConfig;
	private final List<CharSequence> rawBody;
	private final FontParam fontParam;
	private final ISkinParam skinParam;
	private final boolean lineFirst;
	private final HorizontalAlignment align;
	private final boolean manageHorizontalLine;
	private final boolean manageModifier;
	private final List<Url> urls = new ArrayList<Url>();
	private final Stereotype stereotype;
	private final ILeaf entity;
	private final boolean inEllipse;

	public BodyEnhanced(List<String> rawBody, FontParam fontParam, ISkinParam skinParam, boolean manageModifier,
			Stereotype stereotype, ILeaf entity) {
		this.rawBody = new ArrayList<CharSequence>(rawBody);
		this.stereotype = stereotype;
		this.fontParam = fontParam;
		this.skinParam = skinParam;

		this.titleConfig = new FontConfiguration(skinParam, fontParam, stereotype);
		this.lineFirst = true;
		this.align = skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT);
		this.manageHorizontalLine = true;
		this.manageModifier = manageModifier;
		this.entity = entity;
		this.inEllipse = false;
	}

	public BodyEnhanced(Display display, FontParam fontParam, ISkinParam skinParam, HorizontalAlignment align,
			Stereotype stereotype, boolean manageHorizontalLine, boolean manageModifier, ILeaf entity) {
		this.entity = entity;
		this.stereotype = stereotype;
		this.rawBody = new ArrayList<CharSequence>();
		this.fontParam = fontParam;
		this.skinParam = skinParam;

		this.titleConfig = new FontConfiguration(skinParam, fontParam, stereotype);
		this.lineFirst = false;
		this.align = skinParam.getDefaultTextAlignment(align);
		this.manageHorizontalLine = manageHorizontalLine;
		this.manageModifier = manageModifier;
		this.inEllipse = fontParam == FontParam.USECASE;

		if (manageHorizontalLine && inEllipse && display.size() > 0 && isBlockSeparator(display.get(0).toString())) {
			this.rawBody.add("");
		}
		for (CharSequence s : display) {
			this.rawBody.add(s);
		}

	}

	private TextBlock decorate(StringBounder stringBounder, TextBlock b, char separator, TextBlock title) {
		if (separator == 0) {
			return b;
		}
		if (title == null) {
			return new TextBlockLineBefore(TextBlockUtils.withMargin(b, 6, 4), separator);
		}
		final Dimension2D dimTitle = title.calculateDimension(stringBounder);
		final TextBlock raw = new TextBlockLineBefore(TextBlockUtils.withMargin(b, 6, 6, dimTitle.getHeight() / 2, 4),
				separator, title);
		return TextBlockUtils.withMargin(raw, 0, 0, dimTitle.getHeight() / 2, 0);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getArea(stringBounder).calculateDimension(stringBounder);
	}

	private TextBlock getArea(StringBounder stringBounder) {
		if (area2 != null) {
			return area2;
		}
		urls.clear();
		final List<TextBlock> blocks = new ArrayList<TextBlock>();

		char separator = lineFirst ? '_' : 0;
		TextBlock title = null;
		List<Member> members = new ArrayList<Member>();
		// final LineBreakStrategy lineBreakStrategy = skinParam.wrapWidth();
		for (ListIterator<CharSequence> it = rawBody.listIterator(); it.hasNext();) {
			final CharSequence s2 = it.next();
			if (s2 instanceof EmbeddedDiagram) {
				blocks.add(((EmbeddedDiagram) s2).asDraw(skinParam));
			} else {
				final String s = s2.toString();
				if (manageHorizontalLine && isBlockSeparator(s)) {
					blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam, align,
							stereotype, entity), separator, title));
					separator = s.charAt(0);
					title = getTitle(s, skinParam);
					members = new ArrayList<Member>();
				} else if (CreoleParser.isTreeStart(s)) {
					if (members.size() > 0) {
						blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam,
								align, stereotype, entity), separator, title));
					}
					members = new ArrayList<Member>();
					final List<CharSequence> allTree = buildAllTree(s, it);
					final TextBlock bloc = Display.create(allTree).create(fontParam.getFontConfiguration(skinParam),
							align, skinParam, CreoleMode.FULL);
					blocks.add(bloc);
				} else {
					final Member m = new MemberImpl(s, MemberImpl.isMethod(s), manageModifier);
					members.add(m);
					if (m.getUrl() != null) {
						urls.add(m.getUrl());
					}
				}
			}
		}
		if (inEllipse && members.size() == 0) {
			members.add(new MemberImpl("", false, false));
		}
		blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam, align, stereotype,
				entity), separator, title));

		if (blocks.size() == 1) {
			this.area2 = blocks.get(0);
		} else {
			this.area2 = new TextBlockVertical2(blocks, align);
		}

		return area2;
	}

	private static List<CharSequence> buildAllTree(String init, ListIterator<CharSequence> it) {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		result.add(init);
		while (it.hasNext()) {
			final CharSequence s = it.next();
			if (CreoleParser.isTreeStart(StringUtils.trinNoTrace(s))) {
				result.add(s);
			} else {
				it.previous();
				return result;
			}

		}
		return result;
	}

	public static boolean isBlockSeparator(String s) {
		if (s.startsWith("--") && s.endsWith("--")) {
			return true;
		}
		if (s.startsWith("==") && s.endsWith("==")) {
			return true;
		}
		if (s.startsWith("..") && s.endsWith("..") && s.equals("...") == false) {
			return true;
		}
		if (s.startsWith("__") && s.endsWith("__")) {
			return true;
		}
		return false;
	}

	private TextBlock getTitle(String s, ISkinSimple spriteContainer) {
		if (s.length() <= 4) {
			return null;
		}
		s = StringUtils.trin(s.substring(2, s.length() - 2));
		return Display.getWithNewlines(s).create(titleConfig, HorizontalAlignment.LEFT, spriteContainer);
	}

	public Ports getPorts(StringBounder stringBounder) {
		final TextBlock area = getArea(stringBounder);
		if (area instanceof WithPorts) {
			return ((WithPorts) area).getPorts(stringBounder);
		}
		return new Ports();
	}

	public void drawU(UGraphic ug) {
		getArea(ug.getStringBounder()).drawU(ug);
	}

	public List<Url> getUrls() {
		return Collections.unmodifiableList(urls);
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return getArea(stringBounder).getInnerPosition(member, stringBounder, strategy);
	}

}
