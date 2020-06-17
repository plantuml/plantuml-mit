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
package net.sourceforge.plantuml.asciiart;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingSpace;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;

public class TextSkin extends Rose {

	private final FileFormat fileFormat;

	public TextSkin(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	@Override
	public ArrowComponent createComponentArrow(Style[] styles, ArrowConfiguration config, ISkinParam param, Display stringsToDisplay) {
		if (config.getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL
				|| config.getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE
				|| config.getArrowDirection() == ArrowDirection.BOTH_DIRECTION) {
			return new ComponentTextArrow(ComponentType.ARROW, config, stringsToDisplay, fileFormat,
					param.maxAsciiMessageLength());
		}
		if (config.isSelfArrow()) {
			return new ComponentTextSelfArrow(ComponentType.ARROW, config, stringsToDisplay, fileFormat);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Component createComponent(Style style[], ComponentType type, ArrowConfiguration config,
			ISkinParam param, Display stringsToDisplay) {
		if (type == ComponentType.ACTOR_HEAD || type == ComponentType.ACTOR_TAIL) {
			return new ComponentTextActor(type, stringsToDisplay, fileFormat,
					fileFormat == FileFormat.UTXT ? AsciiShape.STICKMAN_UNICODE : AsciiShape.STICKMAN);
		}
		if (type == ComponentType.BOUNDARY_HEAD || type == ComponentType.BOUNDARY_TAIL) {
			return new ComponentTextShape(type, stringsToDisplay, AsciiShape.BOUNDARY);
		}
		if (type == ComponentType.DATABASE_HEAD || type == ComponentType.DATABASE_TAIL) {
			return new ComponentTextShape(type, stringsToDisplay, AsciiShape.DATABASE);
		}
		if (type.name().endsWith("_HEAD") || type.name().endsWith("_TAIL")) {
			return new ComponentTextParticipant(type, stringsToDisplay, fileFormat);
		}
		if (type.isArrow()
				&& (config.getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL
						|| config.getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE || config
						.getArrowDirection() == ArrowDirection.BOTH_DIRECTION)) {
			return new ComponentTextArrow(type, config, stringsToDisplay, fileFormat, param.maxAsciiMessageLength());
		}
		if (type.isArrow() && config.isSelfArrow()) {
			return new ComponentTextSelfArrow(type, config, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentTextLine(type, fileFormat);
		}
		if (type == ComponentType.CONTINUE_LINE) {
			return new ComponentTextLine(type, fileFormat);
		}
		if (type == ComponentType.DELAY_LINE) {
			return new ComponentTextLine(type, fileFormat);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
			return new ComponentTextActiveLine(fileFormat);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN) {
			return new ComponentTextActiveLine(fileFormat);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			return new ComponentTextActiveLine(fileFormat);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN) {
			return new ComponentTextActiveLine(fileFormat);
		}
		if (type == ComponentType.NOTE || type == ComponentType.NOTE_BOX) {
			return new ComponentTextNote(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentTextDivider(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentTextGroupingHeader(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.GROUPING_SPACE) {
			return new ComponentRoseGroupingSpace(1);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentTextGroupingElse(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentTextNewpage(fileFormat);
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentTextDelay(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentTextDestroy();
		}
		throw new UnsupportedOperationException(type.toString());
	}

}
