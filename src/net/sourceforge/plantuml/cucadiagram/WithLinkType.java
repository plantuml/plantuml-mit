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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public abstract class WithLinkType {

	protected LinkType type;
	protected boolean hidden = false;
	private boolean single = false;

	private Colors colors = Colors.empty();

	private List<Colors> supplementary = new ArrayList<Colors>();

	public final HtmlColor getSpecificColor() {
		return colors.getColor(ColorType.LINE);
	}

	public final void setSpecificColor(HtmlColor specificColor) {
		setSpecificColor(specificColor, 0);
	}

	public final void setSpecificColor(HtmlColor specificColor, int i) {
		if (i == 0) {
			colors = colors.add(ColorType.LINE, specificColor);
		} else {
			supplementary.add(colors.add(ColorType.LINE, specificColor));
		}
	}

	public List<Colors> getSupplementaryColors() {
		return Collections.unmodifiableList(supplementary);
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public final Colors getColors() {
		return colors;
	}

	final public void goDashed() {
		type = type.goDashed();
	}

	final public void goDotted() {
		type = type.goDotted();
	}

	final public void goThickness(double thickness) {
		type = type.goThickness(thickness);
	}

	final public void goHidden() {
		this.hidden = true;
	}

	public abstract void goNorank();

	final public void goBold() {
		type = type.goBold();
	}

	public final void goSingle() {
		this.single = true;
	}

	public boolean isSingle() {
		return single;
	}

	public void applyStyle(String arrowStyle) {
		if (arrowStyle == null) {
			return;
		}
		final StringTokenizer st = new StringTokenizer(arrowStyle, ";");
		int i = 0;
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			applyOneStyle(s, i);
			i++;
		}
	}

	private void applyOneStyle(String arrowStyle, int i) {
		final StringTokenizer st = new StringTokenizer(arrowStyle, ",");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			if (s.equalsIgnoreCase("dashed")) {
				this.goDashed();
			} else if (s.equalsIgnoreCase("bold")) {
				this.goBold();
			} else if (s.equalsIgnoreCase("dotted")) {
				this.goDotted();
			} else if (s.equalsIgnoreCase("hidden")) {
				this.goHidden();
			} else if (s.equalsIgnoreCase("single")) {
				this.goSingle();
			} else if (s.equalsIgnoreCase("plain")) {
				// Do nothing
			} else if (s.equalsIgnoreCase("norank")) {
				this.goNorank();
			} else if (s.startsWith("thickness=")) {
				this.goThickness(Double.parseDouble(s.substring("thickness=".length())));
			} else {
				final HtmlColor tmp = HtmlColorSet.getInstance().getColorIfValid(s);
				setSpecificColor(tmp, i);
			}
		}
	}

	public LinkType getType() {
		return type;
	}

}
