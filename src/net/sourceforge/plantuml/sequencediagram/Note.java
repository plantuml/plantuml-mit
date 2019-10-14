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
package net.sourceforge.plantuml.sequencediagram;

import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.WithStyle;

final public class Note extends AbstractEvent implements Event, SpecificBackcolorable, WithStyle {

	private final Participant p;
	private final Participant p2;

	private final Display strings;

	private/* final */NotePosition position;

	public void temporaryProtectedUntilTeozIsStandard() {
		if (position == NotePosition.BOTTOM || position == NotePosition.TOP) {
			position = NotePosition.LEFT;
		}
	}

	private final StyleBuilder styleBuilder;
	private NoteStyle noteStyle = NoteStyle.NORMAL;
	private Colors colors = Colors.empty();

	private Url url;

	private Style style;

	public StyleSignature getDefaultStyleDefinition() {
		return noteStyle.getDefaultStyleDefinition();
	}

	public Style[] getUsedStyles() {
		if (style != null) {
			return new Style[] { style.eventuallyOverride(colors) };
		}
		return new Style[] { style };
	}

	public Note(Participant p, NotePosition position, Display strings, StyleBuilder styleBuilder) {
		this(p, null, position, strings, styleBuilder);
	}

	public Note(Display strings, NotePosition position, NoteStyle style, StyleBuilder styleBuilder) {
		this(null, null, position, strings, styleBuilder);
		this.noteStyle = style;
	}

	public Note(Participant p, Participant p2, Display strings, StyleBuilder styleBuilder) {
		this(p, p2, NotePosition.OVER_SEVERAL, strings, styleBuilder);
	}

	private Note(Participant p, Participant p2, NotePosition position, Display strings, StyleBuilder styleBuilder) {
		this.p = p;
		this.p2 = p2;
		this.styleBuilder = styleBuilder;
		this.position = position;
		this.strings = strings;
		if (SkinParam.USE_STYLES()) {
			this.style = getDefaultStyleDefinition().getMergedStyle(styleBuilder);
		}
	}

	public void setStereotype(Stereotype stereotype) {
		if (SkinParam.USE_STYLES()) {
			final List<Style> others = stereotype.getStyles(styleBuilder);
			this.style = getDefaultStyleDefinition().mergeWith(others).getMergedStyle(styleBuilder);
		}
	}

	public Note withPosition(NotePosition newPosition) {
		if (position == newPosition) {
			return this;
		}
		final Note result = new Note(p, p2, newPosition, strings, styleBuilder);
		result.noteStyle = this.noteStyle;
		result.url = this.url;
		result.colors = this.colors;
		result.parallel = this.parallel;
		return result;
	}

	public Participant getParticipant() {
		return p;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public Display getStrings() {
		return strings;
	}

	public NotePosition getPosition() {
		return position;
	}

	final public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public boolean dealWith(Participant someone) {
		return p == someone || p2 == someone;
	}

	public Url getUrl() {
		return url;
	}

	public boolean hasUrl() {
		return url != null;
	}

	public final NoteStyle getNoteStyle() {
		return noteStyle;
	}

	public final void setNoteStyle(NoteStyle style) {
		this.noteStyle = style;
	}

	public ISkinParam getSkinParamBackcolored(ISkinParam skinParam) {
		return colors.mute(skinParam);
	}

	@Override
	public String toString() {
		return super.toString() + " " + strings;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	private boolean parallel = false;

	public void goParallel() {
		this.parallel = true;
	}

	public boolean isParallel() {
		return parallel;
	}

}
