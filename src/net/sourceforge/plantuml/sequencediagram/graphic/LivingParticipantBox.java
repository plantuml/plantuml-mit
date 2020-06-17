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
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class LivingParticipantBox implements InGroupable {

	private final ParticipantBox participantBox;
	private final LifeLine lifeLine;

	public LivingParticipantBox(ParticipantBox participantBox, LifeLine lifeLine) {
		this.participantBox = participantBox;
		this.lifeLine = lifeLine;
	}

	/**
	 * @deprecated a virer
	 */
	public ParticipantBox getParticipantBox() {
		return participantBox;
	}

	/**
	 * @deprecated a virer
	 */
	public LifeLine getLifeLine() {
		return lifeLine;
	}

	public SegmentColored getLiveThicknessAt(StringBounder stringBounder, double y) {
		final double left = lifeLine.getLeftShift(y);
		assert left >= 0;
		final double right = lifeLine.getRightShift(y);
		assert right >= 0 : "right=" + right;
		final double centerX = participantBox.getCenterX(stringBounder);
		// Log.println("AZERTY " + y + " centerX=" + centerX + " left=" + left + " right=" + right);
		// Log.println("Attention, null for segment");
		final SymbolContext colors = lifeLine.getColors();
		return new SegmentColored(centerX - left, centerX + right, colors, lifeLine.shadowing());
	}

	public void drawLineU22(UGraphic ug, double startingY, double endingY, boolean showTail, double myDelta) {
		if (endingY <= startingY) {
			return;
		}
		final double destroy = lifeLine.getDestroy();
		if (destroy != 0 && destroy > startingY && destroy < endingY) {
			endingY = destroy;
		}
		participantBox.drawLineU22(ug, startingY, endingY, showTail, myDelta);
	}

	public double magicMargin(StringBounder stringBounder) {
		return participantBox.magicMargin(stringBounder);
	}

	public void create(double ypos) {
		lifeLine.setCreate(ypos);
	}

	public double getCreate() {
		return lifeLine.getCreate();
	}

	public double getMaxX(StringBounder stringBounder) {
		return participantBox.getMaxX(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return participantBox.getStartingX();
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

}
