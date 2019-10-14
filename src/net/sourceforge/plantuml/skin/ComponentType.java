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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Styleable;
import net.sourceforge.plantuml.style.StyleSignature;

public enum ComponentType implements Styleable {

	ARROW,

	ACTOR_HEAD, ACTOR_TAIL,

	BOUNDARY_HEAD, BOUNDARY_TAIL, CONTROL_HEAD, CONTROL_TAIL, ENTITY_HEAD, ENTITY_TAIL, QUEUE_HEAD, QUEUE_TAIL, DATABASE_HEAD, DATABASE_TAIL, COLLECTIONS_HEAD, COLLECTIONS_TAIL,

	//
	ALIVE_BOX_CLOSE_CLOSE, ALIVE_BOX_CLOSE_OPEN, ALIVE_BOX_OPEN_CLOSE, ALIVE_BOX_OPEN_OPEN,

	DELAY_TEXT, DESTROY,

	DELAY_LINE, PARTICIPANT_LINE, CONTINUE_LINE,

	//
	GROUPING_ELSE, GROUPING_HEADER, GROUPING_SPACE,
	//
	NEWPAGE, NOTE, NOTE_HEXAGONAL, NOTE_BOX, DIVIDER, REFERENCE, ENGLOBER,

	//
	PARTICIPANT_HEAD, PARTICIPANT_TAIL

	//
	/* TITLE, SIGNATURE */;

	public boolean isArrow() {
		return this == ARROW;
	}

	public StyleSignature getDefaultStyleDefinition() {
		if (this == PARTICIPANT_HEAD || this == PARTICIPANT_TAIL) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.participant);
		}
		if (this == PARTICIPANT_LINE || this == CONTINUE_LINE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.lifeLine);
		}
		if (this == ALIVE_BOX_CLOSE_CLOSE || this == ALIVE_BOX_CLOSE_OPEN || this == ALIVE_BOX_OPEN_CLOSE
				|| this == ALIVE_BOX_OPEN_OPEN) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.lifeLine);
		}
		if (this == DESTROY) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.lifeLine);
		}
		if (this == DIVIDER) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.separator);
		}
		if (this == ENGLOBER) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.box);
		}
		if (this == NOTE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.note);
		}
		if (this == DELAY_TEXT) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.delay);
		}
		if (this == DELAY_LINE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.delay);
		}
//		if (this == REFERENCE) {
//			return StyleSignature.of(SName.root, SName.element,
//					SName.sequenceDiagram, SName.reference);
//		}
		if (SkinParam.USE_STYLES()) {
			throw new UnsupportedOperationException(toString());

		}
		return StyleSignature.of(SName.root);
	}
}
