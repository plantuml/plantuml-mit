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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontPosition;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.ImgValign;
import net.sourceforge.plantuml.math.ScientificEquationSafe;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.utils.CharHidder;

public class StripeSimple implements Stripe {

	final private Atom header;

	final private List<Atom> atoms = new ArrayList<Atom>();
	final private List<Command> commands = new ArrayList<Command>();
	private HorizontalAlignment align = HorizontalAlignment.LEFT;

	public void setCellAlignment(HorizontalAlignment align) {
		this.align = align;
	}

	public HorizontalAlignment getCellAlignment() {
		return align;
	}

	private FontConfiguration fontConfiguration;

	final private StripeStyle style;
	final private ISkinSimple skinParam;

	@Override
	public String toString() {
		return super.toString() + " " + atoms.toString();
	}

	public Atom getHeader() {
		return header;
	}

	public StripeSimple(FontConfiguration fontConfiguration, StripeStyle style, CreoleContext context,
			ISkinSimple skinParam, CreoleMode modeSimpleLine) {
		this.fontConfiguration = fontConfiguration;
		this.style = style;
		this.skinParam = skinParam;

		// class Splitter
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.PLAIN));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.PLAIN));
		if (modeSimpleLine == CreoleMode.FULL) {
			this.commands.add(CommandCreoleStyle.createCreole(FontStyle.UNDERLINE));
		}
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.UNDERLINE));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.UNDERLINE));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.STRIKE));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.STRIKE));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.STRIKE));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.WAVE));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.WAVE));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.WAVE));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.BACKCOLOR));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BACKCOLOR));
		this.commands.add(CommandCreoleSizeChange.create());
		this.commands.add(CommandCreoleSizeChange.createEol());
		this.commands.add(CommandCreoleColorChange.create());
		this.commands.add(CommandCreoleColorChange.createEol());
		this.commands.add(CommandCreoleColorAndSizeChange.create());
		this.commands.add(CommandCreoleColorAndSizeChange.createEol());
		this.commands.add(CommandCreoleExposantChange.create(FontPosition.EXPOSANT));
		this.commands.add(CommandCreoleExposantChange.create(FontPosition.INDICE));
		this.commands.add(CommandCreoleImg.create());
		this.commands.add(CommandCreoleQrcode.create());
		this.commands.add(CommandCreoleOpenIcon.create(skinParam.getIHtmlColorSet()));
		final double scale = skinParam.getDpi() / 96.0;
		this.commands.add(CommandCreoleMath.create(scale));
		this.commands.add(CommandCreoleLatex.create(scale));
		this.commands.add(CommandCreoleSprite.create(skinParam.getIHtmlColorSet()));
		this.commands.add(CommandCreoleSpace.create());
		this.commands.add(CommandCreoleFontFamilyChange.create());
		this.commands.add(CommandCreoleFontFamilyChange.createEol());
		this.commands.add(CommandCreoleMonospaced.create(skinParam.getMonospacedFamily()));
		this.commands.add(CommandCreoleUrl.create(skinParam));
		this.commands.add(CommandCreoleSvgAttributeChange.create());

		this.header = style.getHeader(fontConfiguration, context);

		if (this.header != null) {
			this.atoms.add(this.header);
		}
	}

	public List<Atom> getAtoms() {
		if (atoms.size() == 0) {
			atoms.add(AtomText.create(" ", fontConfiguration));
		}
		return Collections.unmodifiableList(atoms);
	}

	public FontConfiguration getActualFontConfiguration() {
		return fontConfiguration;
	}

	public void setActualFontConfiguration(FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
	}

	public void analyzeAndAdd(String line) {
		if (line == null) {
			throw new IllegalArgumentException();
		}
		if (line.contains("" + BackSlash.hiddenNewLine())) {
			throw new IllegalArgumentException(line);
		}
		line = CharHidder.hide(line);
		if (style.getType() == StripeStyleType.HEADING) {
			fontConfiguration = fontConfigurationForHeading(fontConfiguration, style.getOrder());
			modifyStripe(line);
		} else if (style.getType() == StripeStyleType.HORIZONTAL_LINE) {
			atoms.add(CreoleHorizontalLine.create(fontConfiguration, line, style.getStyle(), skinParam));
		} else {
			modifyStripe(line);
		}
	}

	private static FontConfiguration fontConfigurationForHeading(FontConfiguration fontConfiguration, int order) {
		if (order == 0) {
			fontConfiguration = fontConfiguration.bigger(4).bold();
		} else if (order == 1) {
			fontConfiguration = fontConfiguration.bigger(2).bold();
		} else if (order == 2) {
			fontConfiguration = fontConfiguration.bigger(1).bold();
		} else {
			fontConfiguration = fontConfiguration.italic();
		}
		return fontConfiguration;
	}

	public void addImage(String src, double scale) {
		atoms.add(AtomImg.create(src, ImgValign.TOP, 0, scale, null));
	}

	public void addQrcode(String src, double scale) {
		atoms.add(AtomImg.createQrcode(src, scale));
	}

	public void addSpace(int size) {
		atoms.add(AtomSpace.create(size));
	}

	public void addUrl(Url url) {
		atoms.add(AtomText.createUrl(url, fontConfiguration, skinParam));
	}

	public void addSprite(String src, double scale, HtmlColor color) {
		final Sprite sprite = skinParam.getSprite(src);
		if (sprite != null) {
			atoms.add(new AtomSprite(color, scale, fontConfiguration, sprite, null));
		}
	}

	public void addOpenIcon(String src, double scale, HtmlColor color) {
		final OpenIcon openIcon = OpenIcon.retrieve(src);
		if (openIcon != null) {
			atoms.add(new AtomOpenIcon(color, scale, openIcon, fontConfiguration, null));
		}
	}

	public void addMath(ScientificEquationSafe math, double scale) {
		atoms.add(new AtomMath(math, fontConfiguration.getColor(), fontConfiguration.getExtendedColor(), scale,
				skinParam.getColorMapper()));
	}

	private void modifyStripe(String line) {
		final StringBuilder pending = new StringBuilder();

		while (line.length() > 0) {
			final Command cmd = searchCommand(line);
			if (cmd == null) {
				pending.append(line.charAt(0));
				line = line.substring(1);
			} else {
				addPending(pending);
				line = cmd.executeAndGetRemaining(line, this);
			}
		}
		addPending(pending);
	}

	private void addPending(StringBuilder pending) {
		if (pending.length() == 0) {
			return;
		}
		atoms.add(AtomText.create(pending.toString(), fontConfiguration));
		pending.setLength(0);
	}

	private Command searchCommand(String line) {
		for (Command cmd : commands) {
			final int i = cmd.matchingSize(line);
			if (i != 0) {
				return cmd;
			}
		}
		return null;
	}

}
