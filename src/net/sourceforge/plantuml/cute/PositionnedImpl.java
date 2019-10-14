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
package net.sourceforge.plantuml.cute;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PositionnedImpl implements Positionned {

	private final CuteShape cuteShape;
	private final HtmlColor color;
	private final UTranslate position;
	private final RotationZoom rotationZoom;

	@Override
	public String toString() {
		return "Positionned " + position + " " + cuteShape;
	}

	public PositionnedImpl(CuteShape cuteShape, VarArgs args) {
		this.cuteShape = cuteShape;
		this.color = args.getAsColor("color");
		this.position = args.getPosition();
		this.rotationZoom = RotationZoom.fromVarArgs(args);
	}

	private PositionnedImpl(CuteShape cuteShape, HtmlColor color, UTranslate position, RotationZoom rotationZoom) {
		this.cuteShape = cuteShape;
		this.color = color;
		this.position = position;
		this.rotationZoom = rotationZoom;
	}

	public PositionnedImpl(Group group, RotationZoom rotation) {
		this.cuteShape = group;
		this.color = HtmlColorUtils.BLACK;
		this.position = new UTranslate();
		this.rotationZoom = rotation;
	}

	public PositionnedImpl(Group group, UTranslate translation) {
		this.cuteShape = group;
		this.color = HtmlColorUtils.BLACK;
		this.position = translation;
		this.rotationZoom = RotationZoom.none();
	}

	private UGraphic applyColor(UGraphic ug) {
		return ug.apply(new UChangeBackColor(color)).apply(new UChangeColor(color));

	}

	public void drawU(UGraphic ug) {
		ug = applyColor(ug);
		ug = ug.apply(position);
		final UDrawable tmp = rotationZoom.isNone() ? cuteShape : cuteShape.rotateZoom(rotationZoom);
		// System.err.println("rotationZoom=" + rotationZoom + " tmp=" + tmp);
		tmp.drawU(ug);
	}

	public Positionned rotateZoom(RotationZoom other) {
		return new PositionnedImpl(cuteShape, color, other.getUTranslate(position), rotationZoom.compose(other));
	}

	public Positionned translate(UTranslate other) {
		return new PositionnedImpl(cuteShape, color, position.compose(other), rotationZoom);
	}

}
