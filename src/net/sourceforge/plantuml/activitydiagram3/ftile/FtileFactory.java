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
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ForkStyle;
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public interface FtileFactory {

	public StringBounder getStringBounder();

	public ISkinParam skinParam();

	public Ftile start(Swimlane swimlane);

	public Ftile stop(Swimlane swimlane);

	public Ftile end(Swimlane swimlane);

	public Ftile spot(Swimlane swimlane, String spot, HColor color);

	public Ftile activity(Display label, Swimlane swimlane, BoxStyle style, Colors colors);

	public Ftile addNote(Ftile ftile, Swimlane swimlane, Collection<PositionedNote> notes);

	public Ftile addUrl(Ftile ftile, Url url);

	public Ftile decorateIn(Ftile ftile, LinkRendering linkRendering);

	public Ftile decorateOut(Ftile ftile, LinkRendering linkRendering);

	public Ftile assembly(Ftile tile1, Ftile tile2);

	public Ftile repeat(BoxStyle boxStyleIn, Swimlane swimlane, Swimlane swimlaneOut, Display startLabel, Ftile repeat,
			Display test, Display yes, Display out, Colors colors, LinkRendering backRepeatLinkRendering,
			Ftile backward, boolean noOut);

	public Ftile createWhile(Swimlane swimlane, Ftile whileBlock, Display test, Display yes, Display out,
			LinkRendering afterEndwhile, HColor color, Instruction specialOut, Ftile backward);

	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Url url);

	public Ftile createSwitch(Swimlane swimlane, List<Branch> branches, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Display labelTest);

	public Ftile createParallel(List<Ftile> all, ForkStyle style, String label, Swimlane in, Swimlane out);

	public Ftile createGroup(Ftile list, Display name, HColor backColor, HColor titleColor, PositionedNote note,
			HColor borderColor, USymbol type, double roundCorner);

}
