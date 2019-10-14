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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.Collection;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;

public class Branch {

	private final InstructionList list;
	private final Display labelTest;
	private final Display labelPositive;
	private final Display inlabel;
	private final HtmlColor color;
	private LinkRendering inlinkRendering = LinkRendering.none();

	private Ftile ftile;

	public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public boolean containsBreak() {
		return list.containsBreak();
	}

	public Branch(StyleBuilder styleBuilder, Swimlane swimlane, Display labelPositive, Display labelTest,
			HtmlColor color, Display inlabel) {
		if (labelPositive == null) {
			throw new IllegalArgumentException();
		}
		if (labelTest == null) {
			throw new IllegalArgumentException();
		}
		if (inlabel == null) {
			throw new IllegalArgumentException();
		}
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionDiamond().getMergedStyle(styleBuilder);
			this.color = color == null ? style.value(PName.BackGroundColor).asColor(
					styleBuilder.getSkinParam().getIHtmlColorSet()) : color;
		} else {
			this.color = color;
		}

		this.inlabel = inlabel;
		this.list = new InstructionList(swimlane);
		this.labelTest = labelTest;
		this.labelPositive = labelPositive;
	}

	public Collection<WeldingPoint> getWeldingPoints() {
		return ftile.getWeldingPoints();
	}

	public void add(Instruction ins) {
		list.add(ins);
	}

	public boolean kill() {
		return list.kill();
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		return list.addNote(note, position, type, colors, swimlaneNote);
	}

	public final void setInlinkRendering(LinkRendering inlinkRendering) {
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
		this.inlinkRendering = inlinkRendering;
	}

	public void updateFtile(FtileFactory factory) {
		this.ftile = factory.decorateOut(list.createFtile(factory), inlinkRendering);
	}

	public Collection<? extends Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public final Display getLabelPositive() {
		final LinkRendering in = ftile.getInLinkRendering();
		if (in != null && Display.isNull(in.getDisplay()) == false) {
			return in.getDisplay();
		}
		return labelPositive;
	}

	public final Display getLabelTest() {
		return labelTest;
	}

	public final Rainbow getInlinkRenderingColorAndStyle() {
		return inlinkRendering == null ? null : inlinkRendering.getRainbow();
	}

	public Display getInlabel() {
		return inlabel;
	}

	public final Ftile getFtile() {
		return ftile;
	}

	public ISkinParam skinParam() {
		return ftile.skinParam();
	}

	public final HtmlColor getColor() {
		return color;
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Instruction getLast() {
		return list.getLast();
	}

	public boolean isOnlySingleStopOrSpot() {
		return list.isOnlySingleStopOrSpot();
	}

	private LinkRendering special;

	public void setSpecial(LinkRendering link) {
		this.special = link;
	}

	public final LinkRendering getSpecial() {
		return special;
	}

}
