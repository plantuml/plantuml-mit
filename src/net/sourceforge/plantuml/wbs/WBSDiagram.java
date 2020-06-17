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
package net.sourceforge.plantuml.wbs;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class WBSDiagram extends UmlDiagram {

	public DiagramDescription getDescription() {
		return new DiagramDescription("Work Breakdown Structure");
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.WBS;
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final Scale scale = getScale();

		final double dpiFactor = scale == null ? getScaleCoef(fileFormatOption) : scale.getScale(100, 100);
		final ISkinParam skinParam = getSkinParam();
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(10);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 10;
			margin2 = 10;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildB(skinParam.getColorMapper(), skinParam.handwritten(), ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2),
		null, fileFormatOption.isWithMetadata() ? getMetadata() : null, "", dpiFactor, skinParam.getBackgroundColor(false));
		TextBlock result = getTextBlock();

		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder()).addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed(), os);
	}

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return getDrawingElement().calculateDimension(stringBounder);

			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

	private void drawMe(UGraphic ug) {
		getDrawingElement().drawU(ug);

	}

	private TextBlock getDrawingElement() {
		return new Fork(getSkinParam(), root);
	}

	public final static Pattern2 patternStereotype = MyPattern
			.cmpile("^\\s*(.*?)(?:\\s*\\<\\<\\s*(.*)\\s*\\>\\>)\\s*$");

	public CommandExecutionResult addIdea(int level, String label, Direction direction, IdeaShape shape) {
		final Matcher2 m = patternStereotype.matcher(label);
		String stereotype = null;
		if (m.matches()) {
			label = m.group(1);
			stereotype = m.group(2);
		}
		if (level == 0) {
			if (root != null) {
				return CommandExecutionResult.error("Error 44");
			}
			initRoot(label, stereotype);
			return CommandExecutionResult.ok();
		}
		return add(level, label, stereotype, direction, shape);
	}

	private WElement root;
	private WElement last;

	private void initRoot(String label, String stereotype) {
		root = new WElement(Display.getWithNewlines(label), stereotype, getSkinParam().getCurrentStyleBuilder());
		last = root;
	}

	private WElement getParentOfLast(int nb) {
		WElement result = last;
		for (int i = 0; i < nb; i++) {
			result = result.getParent();
		}
		return result;
	}

	private CommandExecutionResult add(int level, String label, String stereotype, Direction direction,
			IdeaShape shape) {
		if (level == last.getLevel() + 1) {
			final WElement newIdea = last.createElement(level, Display.getWithNewlines(label), stereotype, direction,
					shape, getSkinParam().getCurrentStyleBuilder());
			last = newIdea;
			return CommandExecutionResult.ok();
		}
		if (level <= last.getLevel()) {
			final int diff = last.getLevel() - level + 1;
			final WElement newIdea = getParentOfLast(diff).createElement(level, Display.getWithNewlines(label),
					stereotype, direction, shape, getSkinParam().getCurrentStyleBuilder());
			last = newIdea;
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("error42L");
	}

}
