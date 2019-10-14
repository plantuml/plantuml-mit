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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.Styleable;

public enum ParticipantType implements Styleable {
	PARTICIPANT(ColorParam.participantBackground), //
	ACTOR(ColorParam.actorBackground), //
	BOUNDARY(ColorParam.boundaryBackground), //
	CONTROL(ColorParam.controlBackground), //
	ENTITY(ColorParam.entityBackground), //
	QUEUE(ColorParam.queueBackground), //
	DATABASE(ColorParam.databaseBackground), //
	COLLECTIONS(ColorParam.collectionsBackground);

	private final ColorParam background;

	private ParticipantType(ColorParam background) {
		this.background = background;
	}

	public ColorParam getBackgroundColorParam() {
		return background;
	}

	public StyleSignature getDefaultStyleDefinition() {
		if (this == PARTICIPANT) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.participant);
		}
		if (this == ACTOR) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.actor);
		}
		if (this == BOUNDARY) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.boundary);
		}
		if (this == CONTROL) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.control);
		}
		if (this == ENTITY) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.entity);
		}
		if (this == QUEUE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.queue);
		}
		if (this == DATABASE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.database);
		}
		if (this == COLLECTIONS) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.collections);
		}
		return null;
	}

}
