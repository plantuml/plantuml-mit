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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteAcrossCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteOnArrowCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteOverSeveralCommand;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate2;
import net.sourceforge.plantuml.sequencediagram.command.CommandArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutoNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutoactivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumber;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumberIncrement;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumberResume;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumberStop;
import net.sourceforge.plantuml.sequencediagram.command.CommandBoxEnd;
import net.sourceforge.plantuml.sequencediagram.command.CommandBoxStart;
import net.sourceforge.plantuml.sequencediagram.command.CommandDeactivateShort;
import net.sourceforge.plantuml.sequencediagram.command.CommandDelay;
import net.sourceforge.plantuml.sequencediagram.command.CommandDivider;
import net.sourceforge.plantuml.sequencediagram.command.CommandExoArrowLeft;
import net.sourceforge.plantuml.sequencediagram.command.CommandExoArrowRight;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootbox;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootboxOld;
import net.sourceforge.plantuml.sequencediagram.command.CommandGrouping;
import net.sourceforge.plantuml.sequencediagram.command.CommandHSpace;
import net.sourceforge.plantuml.sequencediagram.command.CommandIgnoreNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandLinkAnchor;
import net.sourceforge.plantuml.sequencediagram.command.CommandNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA2;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA3;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA4;
import net.sourceforge.plantuml.sequencediagram.command.CommandReferenceMultilinesOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandReferenceOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandReturn;
import net.sourceforge.plantuml.sequencediagram.command.CommandUrl;

public class SequenceDiagramFactory extends UmlDiagramFactory {

	private final ISkinSimple skinParam;

	public SequenceDiagramFactory(ISkinSimple skinParam) {
		this.skinParam = skinParam;
	}

	@Override
	public SequenceDiagram createEmptyDiagram() {
		return new SequenceDiagram(skinParam);
	}

	@Override
	protected List<Command> createCommands() {

		final List<Command> cmds = new ArrayList<Command>();

		addCommonCommands1(cmds);

		cmds.add(new CommandActivate());
		cmds.add(new CommandDeactivateShort());

		cmds.add(new CommandParticipantA());
		cmds.add(new CommandParticipantA2());
		cmds.add(new CommandParticipantA3());
		cmds.add(new CommandParticipantA4());
		cmds.add(new CommandArrow());
		// addCommand(new CommandArrowCrossX());
		cmds.add(new CommandExoArrowLeft());
		cmds.add(new CommandExoArrowRight());

		final FactorySequenceNoteCommand factorySequenceNoteCommand = new FactorySequenceNoteCommand();
		cmds.add(factorySequenceNoteCommand.createSingleLine());

		final FactorySequenceNoteOverSeveralCommand factorySequenceNoteOverSeveralCommand = new FactorySequenceNoteOverSeveralCommand();
		cmds.add(factorySequenceNoteOverSeveralCommand.createSingleLine());
		final FactorySequenceNoteAcrossCommand factorySequenceNoteAccrossCommand = new FactorySequenceNoteAcrossCommand();
		cmds.add(factorySequenceNoteAccrossCommand.createSingleLine());

		cmds.add(new CommandBoxStart());
		cmds.add(new CommandBoxEnd());
		cmds.add(new CommandGrouping());

		cmds.add(new CommandActivate2());
		cmds.add(new CommandReturn());

		final FactorySequenceNoteOnArrowCommand factorySequenceNoteOnArrowCommand = new FactorySequenceNoteOnArrowCommand();
		cmds.add(factorySequenceNoteOnArrowCommand.createSingleLine());

		cmds.add(factorySequenceNoteCommand.createMultiLine(false));
		cmds.add(factorySequenceNoteOverSeveralCommand.createMultiLine(false));
		cmds.add(factorySequenceNoteOnArrowCommand.createMultiLine(false));
		cmds.add(factorySequenceNoteAccrossCommand.createMultiLine(false));

		cmds.add(new CommandNewpage());
		cmds.add(new CommandIgnoreNewpage());
		cmds.add(new CommandAutoNewpage());
		cmds.add(new CommandDivider());
		cmds.add(new CommandHSpace());
		cmds.add(new CommandReferenceOverSeveral());
		cmds.add(new CommandReferenceMultilinesOverSeveral());
		cmds.add(new CommandAutonumber());
		cmds.add(new CommandAutonumberStop());
		cmds.add(new CommandAutonumberResume());
		cmds.add(new CommandAutonumberIncrement());
		cmds.add(new CommandAutoactivate());
		cmds.add(new CommandFootbox());
		cmds.add(new CommandDelay());
		cmds.add(new CommandFootboxOld());
		cmds.add(new CommandUrl());
		cmds.add(new CommandLinkAnchor());

		return cmds;
	}

}