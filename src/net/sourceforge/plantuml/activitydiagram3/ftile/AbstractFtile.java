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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public abstract class AbstractFtile extends AbstractTextBlock implements Ftile {

	private final ISkinParam skinParam;

	public AbstractFtile(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	final public ISkinParam skinParam() {
		if (skinParam == null) {
			throw new IllegalStateException();
		}
		return skinParam;
	}

	final public HColorSet getIHtmlColorSet() {
		return skinParam.getIHtmlColorSet();
	}

	public LinkRendering getInLinkRendering() {
		return LinkRendering.none();
	}

	public LinkRendering getOutLinkRendering() {
		return LinkRendering.none();
	}

	public Collection<Connection> getInnerConnections() {
		return Collections.emptyList();
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		throw new UnsupportedOperationException("" + getClass());
	}

	public final UStroke getThickness() {
		UStroke thickness = skinParam.getThickness(LineParam.activityBorder, null);
		if (thickness == null) {
			thickness = new UStroke(1.5);
		}
		return thickness;
	}

	public List<WeldingPoint> getWeldingPoints() {
		return Collections.emptyList();
	}

	public Collection<Ftile> getMyChildren() {
		throw new UnsupportedOperationException("" + getClass());
	}

	public HorizontalAlignment arrowHorizontalAlignment() {
		return skinParam.getHorizontalAlignment(AlignmentParam.arrowMessageAlignment, null, false);
	}

	private FtileGeometry cachedGeometry;

	final public FtileGeometry calculateDimension(StringBounder stringBounder) {
		if (cachedGeometry == null) {
			cachedGeometry = calculateDimensionFtile(stringBounder);
		}
		return cachedGeometry;
	}

	abstract protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder);

	@Override
	final public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
		// return getMinMaxFtile(stringBounder);
	}

	// protected MinMax getMinMaxFtile(StringBounder stringBounder) {
	// throw new UnsupportedOperationException();
	// }

}
