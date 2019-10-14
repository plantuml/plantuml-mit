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
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.ugraphic.UFont;

public abstract class CommandParticipant extends SingleLineCommand2<SequenceDiagram> {

	public CommandParticipant(IRegex pattern) {
		super(pattern);
	}

	static IRegex getOrderRegex() {
		return new RegexOptional( //
				new RegexConcat( //
						new RegexLeaf("order"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("ORDER", "(-?\\d{1,7})") //
				));
	}

	static IRegex getRegexType() {
		return new RegexOr(//
				new RegexLeaf("TYPE", "(participant|actor|create|boundary|control|entity|queue|database|collections)"), //
				new RegexLeaf("CREATE",
						"create[%s](participant|actor|boundary|control|entity|queue|database|collections)"));
	}

	@Override
	final protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg) {
		final String code = arg.get("CODE", 0);
		if (diagram.participantsContainsKey(code)) {
			diagram.putParticipantInLast(code);
			return CommandExecutionResult.ok();
		}

		Display strings = Display.NULL;
		if (arg.get("FULL", 0) != null) {
			strings = Display.getWithNewlines(arg.get("FULL", 0));
		}

		final String typeString1 = arg.get("TYPE", 0);
		final String typeCreate1 = arg.get("CREATE", 0);
		final ParticipantType type;
		final boolean create;
		if (typeCreate1 != null) {
			type = ParticipantType.valueOf(StringUtils.goUpperCase(typeCreate1));
			create = true;
		} else if (typeString1.equalsIgnoreCase("CREATE")) {
			type = ParticipantType.PARTICIPANT;
			create = true;
		} else {
			type = ParticipantType.valueOf(StringUtils.goUpperCase(typeString1));
			create = false;
		}
		final String orderString = arg.get("ORDER", 0);
		final int order = orderString == null ? 0 : Integer.parseInt(orderString);
		final Participant participant = diagram.createNewParticipant(type, code, strings, order);

		final String stereotype = arg.get("STEREO", 0);

		if (stereotype != null) {
			final ISkinParam skinParam = diagram.getSkinParam();
			final boolean stereotypePositionTop = skinParam.stereotypePositionTop();
			final UFont font = skinParam.getFont(null, false, FontParam.CIRCLED_CHARACTER);
			participant.setStereotype(new Stereotype(stereotype, skinParam.getCircledCharacterRadius(), font, diagram
					.getSkinParam().getIHtmlColorSet()), stereotypePositionTop);
		}
		participant.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("COLOR", 0)));

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			participant.setUrl(url);
		}

		if (create) {
			final String error = diagram.activate(participant, LifeEventType.CREATE, null);
			if (error != null) {
				return CommandExecutionResult.error(error);
			}

		}

		return CommandExecutionResult.ok();
	}

}
