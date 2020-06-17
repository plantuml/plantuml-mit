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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.tim.expression.TValue;

public class TMemoryGlobal extends ExecutionContexts implements TMemory {

	private final Map<String, TValue> globalVariables = new HashMap<String, TValue>();
	private final TrieImpl variables = new TrieImpl();

	public TValue getVariable(String varname) {
		return this.globalVariables.get(varname);
	}

	public void dumpDebug(String message) {
		Log.error("[MemGlobal] Start of memory_dump " + message);
		dumpMemoryInternal();
		Log.error("[MemGlobal] End of memory_dump");
	}

	void dumpMemoryInternal() {
		Log.error("[MemGlobal] Number of variable(s) : " + globalVariables.size());
		for (Entry<String, TValue> ent : new TreeMap<String, TValue>(globalVariables).entrySet()) {
			final String name = ent.getKey();
			final TValue value = ent.getValue();
			Log.error("[MemGlobal] " + name + " = " + value);
		}
	}

	public void putVariable(String varname, TValue value, TVariableScope scope) throws EaterException {
		Log.info("[MemGlobal] Setting " + varname);
		if (scope == TVariableScope.LOCAL) {
			throw EaterException.unlocated("Cannot use local variable here");
		}
		this.globalVariables.put(varname, value);
		this.variables.add(varname);
	}

	public void removeVariable(String varname) {
		this.globalVariables.remove(varname);
		this.variables.remove(varname);
	}

	public boolean isEmpty() {
		return globalVariables.isEmpty();
	}

	public Set<String> variablesNames() {
		return Collections.unmodifiableSet(globalVariables.keySet());
	}

	public Trie variablesNames3() {
		return variables;
	}

	public TMemory forkFromGlobal(Map<String, TValue> input) {
		return new TMemoryLocal(this, input);
	}

}
