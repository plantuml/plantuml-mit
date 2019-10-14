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
package net.sourceforge.plantuml.command.note;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.utils.UniqueSequence;

public final class FactoryNoteActivityCommand implements SingleMultiFactoryCommand<ActivityDiagram> {

	private IRegex getRegexConcatMultiLine() {
		return RegexConcat.build(FactoryNoteActivityCommand.class.getName() + "multi", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	private IRegex getRegexConcatSingleLine() {
		return RegexConcat.build(FactoryNoteActivityCommand.class.getName() + "single", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("NOTE", "(.*)"), RegexLeaf.end());
	}

	public Command<ActivityDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<ActivityDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^[%s]*end[%s]?note$";
			}

			public final CommandExecutionResult executeNow(final ActivityDiagram system, BlocLines lines) {
				// StringUtils.trim(lines, true);
				final RegexResult arg = getStartingPattern().matcher(lines.getFirst499().getTrimmed().getString());
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();

				Display strings = lines.toDisplay();

				Url url = null;
				if (strings.size() > 0) {
					final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"),
							ModeUrl.STRICT);
					url = urlBuilder.getUrl(strings.get(0).toString());
				}
				if (url != null) {
					strings = strings.subList(1, strings.size());
				}

				// final String s = StringUtils.getMergedLines(strings);

				final IEntity note = system.createLeaf(UniqueSequence.getCode("GMN"), strings, LeafType.NOTE, null);
				if (url != null) {
					note.addUrl(url);
				}
				return executeInternal(system, arg, note);
			}
		};
	}

	public Command<ActivityDiagram> createSingleLine() {
		return new SingleLineCommand2<ActivityDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final ActivityDiagram system, LineLocation location,
					RegexResult arg) {
				final IEntity note = system.createNote(UniqueSequence.getCode("GN"),
						Display.getWithNewlines(arg.get("NOTE", 0)));
				return executeInternal(system, arg, note);
			}
		};
	}

	private CommandExecutionResult executeInternal(ActivityDiagram diagram, RegexResult arg, IEntity note) {

		note.setSpecificColorTOBEREMOVED(ColorType.BACK,
				diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));

		IEntity activity = diagram.getLastEntityConsulted();
		if (activity == null) {
			activity = diagram.getStart();
		}

		final Link link;

		final Position position = Position.valueOf(StringUtils.goUpperCase(arg.get("POSITION", 0))).withRankdir(
				diagram.getSkinParam().getRankdir());

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).goDashed();

		if (position == Position.RIGHT) {
			link = new Link(activity, note, type, Display.NULL, 1, diagram.getSkinParam().getCurrentStyleBuilder());
		} else if (position == Position.LEFT) {
			link = new Link(note, activity, type, Display.NULL, 1, diagram.getSkinParam().getCurrentStyleBuilder());
		} else if (position == Position.BOTTOM) {
			link = new Link(activity, note, type, Display.NULL, 2, diagram.getSkinParam().getCurrentStyleBuilder());
		} else if (position == Position.TOP) {
			link = new Link(note, activity, type, Display.NULL, 2, diagram.getSkinParam().getCurrentStyleBuilder());
		} else {
			throw new IllegalArgumentException();
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
