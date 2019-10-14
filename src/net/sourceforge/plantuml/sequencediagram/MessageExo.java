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

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.StyleBuilder;

public class MessageExo extends AbstractMessage {

	final private MessageExoType type;
	final private Participant participant;
	final private boolean shortArrow;

	public MessageExo(StyleBuilder styleBuilder, Participant p, MessageExoType type, Display label,
			ArrowConfiguration arrowConfiguration, String messageNumber, boolean shortArrow) {
		super(styleBuilder, label, arrowConfiguration, messageNumber);
		this.participant = p;
		this.type = type;
		this.shortArrow = shortArrow;
	}

	public boolean isShortArrow() {
		return shortArrow;
	}

	@Override
	protected NotePosition overideNotePosition(NotePosition notePosition) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			return NotePosition.RIGHT;
		}
		if (type == MessageExoType.FROM_RIGHT || type == MessageExoType.TO_RIGHT) {
			return NotePosition.LEFT;
		}
		throw new IllegalStateException();
	}

	public Participant getParticipant() {
		return participant;
	}

	public final MessageExoType getType() {
		return type;
	}

	public boolean dealWith(Participant someone) {
		return participant == someone;
	}

	@Override
	public boolean compatibleForCreate(Participant p) {
		return p == participant;
	}

	public boolean isSelfMessage() {
		return false;
	}

}
