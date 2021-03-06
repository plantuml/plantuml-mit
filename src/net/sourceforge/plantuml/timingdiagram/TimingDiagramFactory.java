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
package net.sourceforge.plantuml.timingdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.timingdiagram.command.CommandAnalog;
import net.sourceforge.plantuml.timingdiagram.command.CommandAtPlayer;
import net.sourceforge.plantuml.timingdiagram.command.CommandAtTime;
import net.sourceforge.plantuml.timingdiagram.command.CommandBinary;
import net.sourceforge.plantuml.timingdiagram.command.CommandChangeStateByPlayerCode;
import net.sourceforge.plantuml.timingdiagram.command.CommandChangeStateByTime;
import net.sourceforge.plantuml.timingdiagram.command.CommandClock;
import net.sourceforge.plantuml.timingdiagram.command.CommandConstraint;
import net.sourceforge.plantuml.timingdiagram.command.CommandDefineStateLong;
import net.sourceforge.plantuml.timingdiagram.command.CommandDefineStateShort;
import net.sourceforge.plantuml.timingdiagram.command.CommandHideTimeAxis;
import net.sourceforge.plantuml.timingdiagram.command.CommandHighlight;
import net.sourceforge.plantuml.timingdiagram.command.CommandModeCompact;
import net.sourceforge.plantuml.timingdiagram.command.CommandNote;
import net.sourceforge.plantuml.timingdiagram.command.CommandNoteLong;
import net.sourceforge.plantuml.timingdiagram.command.CommandPixelHeight;
import net.sourceforge.plantuml.timingdiagram.command.CommandRobustConcise;
import net.sourceforge.plantuml.timingdiagram.command.CommandScalePixel;
import net.sourceforge.plantuml.timingdiagram.command.CommandTicks;
import net.sourceforge.plantuml.timingdiagram.command.CommandTimeMessage;

public class TimingDiagramFactory extends UmlDiagramFactory {

	@Override
	public TimingDiagram createEmptyDiagram() {
		return new TimingDiagram();
	}

	@Override
	protected List<Command> createCommands() {

		final List<Command> cmds = new ArrayList<Command>();

		addCommonCommands1(cmds);
		cmds.add(new CommandFootboxIgnored());
		cmds.add(new CommandRobustConcise());
		cmds.add(new CommandClock());
		cmds.add(new CommandAnalog());
		cmds.add(new CommandBinary());
		cmds.add(new CommandDefineStateShort());
		cmds.add(new CommandDefineStateLong());
		cmds.add(new CommandChangeStateByPlayerCode());
		cmds.add(new CommandChangeStateByTime());
		cmds.add(new CommandAtTime());
		cmds.add(new CommandAtPlayer());
		cmds.add(new CommandTimeMessage());
		cmds.add(new CommandNote());
		cmds.add(new CommandNoteLong());
		cmds.add(new CommandConstraint());
		cmds.add(new CommandScalePixel());
		cmds.add(new CommandHideTimeAxis());
		cmds.add(new CommandHighlight());
		cmds.add(new CommandModeCompact());
		cmds.add(new CommandTicks());
		cmds.add(new CommandPixelHeight());

		return cmds;
	}

}
