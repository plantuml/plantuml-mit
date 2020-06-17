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

import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.tim.expression.TValue;

public interface TMemory {

	public TValue getVariable(String varname);

	public void putVariable(String varname, TValue value, TVariableScope scope) throws EaterException;

	public void removeVariable(String varname);

	public boolean isEmpty();

	public Set<String> variablesNames();

	public Trie variablesNames3();

	public TMemory forkFromGlobal(Map<String, TValue> input);

	public ExecutionContextIf peekIf();

	public boolean areAllIfOk(TContext context, TMemory memory) throws EaterException;

	public void addIf(ExecutionContextIf context);

	public void addWhile(ExecutionContextWhile value);

	public void addForeach(ExecutionContextForeach value);

	public ExecutionContextIf pollIf();

	public ExecutionContextWhile pollWhile();

	public ExecutionContextWhile peekWhile();

	public ExecutionContextForeach pollForeach();

	public ExecutionContextForeach peekForeach();

	public void dumpDebug(String message);

}
