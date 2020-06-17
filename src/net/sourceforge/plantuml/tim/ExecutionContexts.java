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
package net.sourceforge.plantuml.tim;

import java.util.Deque;
import java.util.LinkedList;

public abstract class ExecutionContexts {

	private final Deque<ExecutionContextIf> allIfs = new LinkedList<ExecutionContextIf>();
	private final Deque<ExecutionContextWhile> allWhiles = new LinkedList<ExecutionContextWhile>();
	private final Deque<ExecutionContextForeach> allForeachs = new LinkedList<ExecutionContextForeach>();

	public void addIf(ExecutionContextIf value) {
		allIfs.addLast(value);
	}

	public void addWhile(ExecutionContextWhile value) {
		allWhiles.addLast(value);
	}

	public void addForeach(ExecutionContextForeach value) {
		allForeachs.addLast(value);
	}

	public ExecutionContextIf peekIf() {
		return allIfs.peekLast();
	}

	public ExecutionContextWhile peekWhile() {
		return allWhiles.peekLast();
	}

	public ExecutionContextForeach peekForeach() {
		return allForeachs.peekLast();
	}

	public ExecutionContextIf pollIf() {
		return allIfs.pollLast();
	}

	public ExecutionContextWhile pollWhile() {
		return allWhiles.pollLast();
	}

	public ExecutionContextForeach pollForeach() {
		return allForeachs.pollLast();
	}

	public boolean areAllIfOk(TContext context, TMemory memory) throws EaterException {
		for (ExecutionContextIf conditionalContext : allIfs) {
			if (conditionalContext.conditionIsOkHere() == false) {
				return false;
			}
		}
		return true;
	}

}
