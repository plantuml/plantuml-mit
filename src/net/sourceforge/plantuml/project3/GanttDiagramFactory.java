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
package net.sourceforge.plantuml.project3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.command.CommandScale;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.DiagramType;

public class GanttDiagramFactory extends UmlDiagramFactory {

	static private final List<SubjectPattern> subjects() {
		return Arrays.<SubjectPattern> asList(new SubjectTask(), new SubjectProject(), new SubjectDayOfWeek(),
				new SubjectDayAsDate(), new SubjectDaysAsDates(), new SubjectResource(), new SubjectToday());
	}

	public GanttDiagramFactory(DiagramType type) {
		super(type);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		addTitleCommands(cmds);
		// addCommonCommands(cmds);
		cmds.add(new CommandNope());
		// cmds.add(new CommandComment());
		// cmds.add(new CommandMultilinesComment());
		cmds.addAll(getLanguageCommands());
		cmds.add(new CommandGanttArrow());
		cmds.add(new CommandGanttArrow2());
		cmds.add(new CommandSeparator());

		cmds.add(new CommandScale());
		cmds.add(new CommandPage());
		// cmds.add(new CommandScaleWidthAndHeight());
		// cmds.add(new CommandScaleWidthOrHeight());
		// cmds.add(new CommandScaleMaxWidth());
		// cmds.add(new CommandScaleMaxHeight());
		// cmds.add(new CommandScaleMaxWidthAndHeight());

		return cmds;
	}

	static private final Collection<Command> cache = new ArrayList<Command>();

	private static Collection<Command> getLanguageCommands() {
		synchronized (cache) {
			if (cache.size() == 0) {
				for (SubjectPattern subject : subjects()) {
					for (VerbPattern verb : subject.getVerbs()) {
						for (ComplementPattern complement : verb.getComplements()) {
							cache.add(NaturalCommand.create(subject, verb, complement));
						}
					}
				}
				for (SubjectPattern subject : subjects()) {
					final Collection<VerbPattern> verbs = subject.getVerbs();
					for (VerbPattern verb1 : verbs) {
						for (VerbPattern verb2 : verbs) {
							if (verb1 == verb2) {
								continue;
							}
							for (ComplementPattern complement1 : verb1.getComplements()) {
								for (ComplementPattern complement2 : verb2.getComplements()) {
									cache.add(NaturalCommandAnd.create(subject, verb1, complement1, verb2, complement2));
								}
							}
						}
					}
				}
				for (SubjectPattern subject : subjects()) {
					final Collection<VerbPattern> verbs = subject.getVerbs();
					for (VerbPattern verb1 : verbs) {
						for (VerbPattern verb2 : verbs) {
							for (VerbPattern verb3 : verbs) {
								if (verb1 == verb2 || verb1 == verb3 || verb2 == verb3) {
									continue;
								}
								for (ComplementPattern complement1 : verb1.getComplements()) {
									for (ComplementPattern complement2 : verb2.getComplements()) {
										for (ComplementPattern complement3 : verb3.getComplements()) {
											cache.add(NaturalCommandAndAnd.create(subject, verb1, complement1, verb2,
													complement2, verb3, complement3));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return cache;
	}

	@Override
	public GanttDiagram createEmptyDiagram() {
		return new GanttDiagram();
	}

}
