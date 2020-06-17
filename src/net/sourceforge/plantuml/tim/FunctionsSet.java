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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.StringLocated;

public class FunctionsSet {

	private final Map<TFunctionSignature, TFunction> functions = new HashMap<TFunctionSignature, TFunction>();
	private final Set<TFunctionSignature> functionsFinal = new HashSet<TFunctionSignature>();
	private final Trie functions3 = new TrieImpl();
	private TFunctionImpl pendingFunction;

	public TFunction getFunctionSmart(TFunctionSignature searched) {
		final TFunction func = this.functions.get(searched);
		if (func != null) {
			return func;
		}
		for (TFunction candidate : this.functions.values()) {
			if (candidate.getSignature().sameFunctionNameAs(searched) == false) {
				continue;
			}
			if (candidate.canCover(searched.getNbArg(), searched.getNamedArguments())) {
				return candidate;
			}
		}
		return null;
	}

	public int size() {
		return functions().size();
	}

	public Map<TFunctionSignature, TFunction> functions() {
		return Collections.unmodifiableMap(functions);
	}

	public String getLonguestMatchStartingIn(String s) {
		return functions3.getLonguestMatchStartingIn(s);
	}

	public TFunctionImpl pendingFunction() {
		return pendingFunction;
	}

	public void addFunction(TFunction func) {
		if (func.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
			((TFunctionImpl) func).finalizeEnddefinelong();
		}
		this.functions.put(func.getSignature(), func);
		this.functions3.add(func.getSignature().getFunctionName() + "(");
	}

	public void executeEndfunction() {
		this.addFunction(this.pendingFunction);
		this.pendingFunction = null;
	}

	public void executeLegacyDefine(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0048");
		}
		final EaterLegacyDefine legacyDefine = new EaterLegacyDefine(s);
		legacyDefine.analyze(context, memory);
		final TFunction function = legacyDefine.getFunction();
		this.functions.put(function.getSignature(), function);
		this.functions3.add(function.getSignature().getFunctionName() + "(");
	}

	public void executeLegacyDefineLong(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0068");
		}
		final EaterLegacyDefineLong legacyDefineLong = new EaterLegacyDefineLong(s);
		legacyDefineLong.analyze(context, memory);
		this.pendingFunction = legacyDefineLong.getFunction();
	}

	public void executeDeclareReturnFunction(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0068");
		}
		final EaterDeclareReturnFunction declareFunction = new EaterDeclareReturnFunction(s);
		declareFunction.analyze(context, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = this.functions.get(declaredSignature);
		if (previous != null && (finalFlag || this.functionsFinal.contains(declaredSignature))) {
			throw EaterException.located("This function is already defined");
		}
		if (finalFlag) {
			this.functionsFinal.add(declaredSignature);
		}
		if (declareFunction.getFunction().hasBody()) {
			this.addFunction(declareFunction.getFunction());
		} else {
			this.pendingFunction = declareFunction.getFunction();
		}
	}

	public void executeDeclareProcedure(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0068");
		}
		final EaterDeclareProcedure declareFunction = new EaterDeclareProcedure(s);
		declareFunction.analyze(context, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = this.functions.get(declaredSignature);
		if (previous != null && (finalFlag || this.functionsFinal.contains(declaredSignature))) {
			throw EaterException.located("This function is already defined");
		}
		if (finalFlag) {
			this.functionsFinal.add(declaredSignature);
		}
		if (declareFunction.getFunction().hasBody()) {
			this.addFunction(declareFunction.getFunction());
		} else {
			this.pendingFunction = declareFunction.getFunction();
		}
	}

}
