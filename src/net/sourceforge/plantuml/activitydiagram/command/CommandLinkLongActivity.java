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
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.graphic.color.ColorType;

public class CommandLinkLongActivity extends CommandMultilines2<ActivityDiagram> {

	public CommandLinkLongActivity() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^[%s]*([^%g]*)[%g](?:[%s]+as[%s]+([\\p{L}0-9][\\p{L}0-9_.]*))?[%s]*(\\<\\<.*\\>\\>)?[%s]*(?:in[%s]+([%g][^%g]+[%g]|\\S+))?[%s]*(#\\w+)?$";
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandLinkLongActivity.class.getName(), RegexLeaf.start(), //
				new RegexOptional(//
						new RegexOr("FIRST", //
								new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
								new RegexLeaf("CODE", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
								new RegexLeaf("BAR", "(?:==+)[%s]*([\\p{L}0-9_.]+)[%s]*(?:==+)"), //
								new RegexLeaf("QUOTED", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([\\p{L}0-9_.]+))?"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("BACKCOLOR", "(#\\w+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //

				new RegexLeaf("ARROW_BODY1", "([-.]+)"), //
				new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf("ARROW_DIRECTION", "(\\*|left|right|up|down|le?|ri?|up?|do?)?"), //
				new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf("ARROW_BODY2", "([-.]*)"), //
				new RegexLeaf("\\>"), //

				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("BRACKET", "\\[([^\\]*]+[^\\]]*)\\]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("[%g]"), //
				new RegexLeaf("DESC", "([^%g]*?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(final ActivityDiagram diagram, BlocLines lines) {
		lines = lines.trim(false);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst499().getTrimmed().getString());

		final IEntity entity1 = CommandLinkActivity.getEntity(diagram, line0, true);
		if (entity1 == null) {
			return CommandExecutionResult.error("No such entity");
		}

		if (line0.get("STEREOTYPE", 0) != null) {
			entity1.setStereotype(new Stereotype(line0.get("STEREOTYPE", 0)));
		}
		final String stringColor = line0.get("BACKCOLOR", 0);
		if (stringColor != null) {
			entity1.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet()
					.getColorIfValid(stringColor));
		}
		final StringBuilder sb = new StringBuilder();

		final String desc0 = line0.get("DESC", 0);
		Url urlActivity = null;
		if (StringUtils.isNotEmpty(desc0)) {
			urlActivity = extractUrlString(diagram, desc0);
			if (urlActivity == null) {
				sb.append(desc0);
				sb.append(BackSlash.BS_BS_N);
			}
		}
		int i = 0;
		for (StringLocated cs : lines.subExtract(1, 1)) {
			i++;
			if (i == 1 && urlActivity == null) {
				urlActivity = extractUrl(diagram, cs);
				if (urlActivity != null) {
					continue;
				}
			}
			sb.append(cs.getString());
			if (i < lines.size() - 2) {
				sb.append(BackSlash.BS_BS_N);
			}
		}

		final List<String> lineLast = StringUtils.getSplit(MyPattern.cmpile(getPatternEnd()), lines.getLast499()
				.getString());
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			if (sb.length() > 0 && sb.toString().endsWith(BackSlash.BS_BS_N) == false) {
				sb.append(BackSlash.BS_BS_N);
			}
			sb.append(lineLast.get(0));
		}

		final String display = sb.toString();
		final Code code = Code.of(lineLast.get(1) == null ? display : lineLast.get(1));

		String partition = null;
		if (lineLast.get(3) != null) {
			partition = lineLast.get(3);
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
		}
		if (partition != null) {
			diagram.gotoGroup2(Code.of(partition), Display.getWithNewlines(partition), GroupType.PACKAGE, null,
					NamespaceStrategy.SINGLE);
		}
		final IEntity entity2 = diagram.getOrCreate(code, Display.getWithNewlines(display), LeafType.ACTIVITY);
		if (entity2 == null) {
			return CommandExecutionResult.error("No such entity");
		}

		if (partition != null) {
			diagram.endGroup();
		}
		if (urlActivity != null) {
			entity2.addUrl(urlActivity);
		}

		if (lineLast.get(2) != null) {
			entity2.setStereotype(new Stereotype(lineLast.get(2)));
		}
		if (lineLast.get(4) != null) {
			entity2.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet()
					.getColorIfValid(lineLast.get(4)));
		}

		final String arrowBody1 = CommandLinkClass.notNull(line0.get("ARROW_BODY1", 0));
		final String arrowBody2 = CommandLinkClass.notNull(line0.get("ARROW_BODY2", 0));
		final String arrowDirection = CommandLinkClass.notNull(line0.get("ARROW_DIRECTION", 0));

		final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");

		final int lenght = arrow.length() - 1;

		final Display linkLabel = Display.getWithNewlines(line0.get("BRACKET", 0));

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if (arrow.contains(".")) {
			type = type.goDotted();
		}
		Link link = new Link(entity1, entity2, type, linkLabel, lenght, diagram.getSkinParam().getCurrentStyleBuilder());
		final Direction direction = StringUtils.getArrowDirection(arrowBody1 + arrowDirection + arrowBody2 + ">");
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		if (line0.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url urlLink = urlBuilder.getUrl(line0.get("URL", 0));
			link.setUrl(urlLink);
		}

		link.applyStyle(line0.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

	public Url extractUrl(final ActivityDiagram diagram, StringLocated string) {
		return extractUrlString(diagram, string.getString());
	}

	public Url extractUrlString(final ActivityDiagram diagram, String string) {
		final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
		return urlBuilder.getUrl(string);
	}

}
