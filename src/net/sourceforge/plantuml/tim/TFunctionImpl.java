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
package net.sourceforge.plantuml.tim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.tim.expression.TValue;

public class TFunctionImpl implements TFunction {

	private final TFunctionSignature signature;
	private final List<TFunctionArgument> args;
	private final List<StringLocated> body = new ArrayList<StringLocated>();
	private final boolean unquoted;
	private TFunctionType functionType = TFunctionType.VOID;
	private String legacyDefinition;

	public TFunctionImpl(String functionName, List<TFunctionArgument> args, boolean unquoted) {
		this.signature = new TFunctionSignature(functionName, args.size());
		this.args = args;
		this.unquoted = unquoted;
	}

	public boolean canCover(int nbArg) {
		if (nbArg > args.size()) {
			return false;
		}
		for (int i = nbArg; i < args.size(); i++) {
			if (args.get(i).getOptionalDefaultValue() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "FUNCTION " + signature + " " + args;
	}

	public void addBody(StringLocated s) {
		body.add(s);
		if (TLineType.getFromLine(s.getString()) == TLineType.RETURN) {
			this.functionType = TFunctionType.RETURN;
		}
	}

	public void executeVoid(TContext context, TMemory memory, String s) throws EaterException {
		final EaterFunctionCall call = new EaterFunctionCall(s, context.isLegacyDefine(signature.getFunctionName()),
				unquoted);
		call.execute(context, memory);
		final String endOfLine = call.getEndOfLine();
		final List<TValue> args = call.getValues();
		executeVoidInternal(context, memory, args);
		context.appendEndOfLine(endOfLine);
	}

	public void executeVoidInternal(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		if (functionType != TFunctionType.VOID && functionType != TFunctionType.LEGACY_DEFINELONG) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args);
		for (StringLocated sl : body) {
			context.executeOneLine(copy, TLineType.getFromLine(sl.getString()), sl, TFunctionType.VOID);
		}
	}

	private TMemory getNewMemory(TMemory memory, List<TValue> values) {
		final Map<String, TVariable> foo = new HashMap<String, TVariable>();
		for (int i = 0; i < args.size(); i++) {
			final TValue tmp = i < values.size() ? values.get(i) : args.get(i).getOptionalDefaultValue();
			foo.put(args.get(i).getName(), new TVariable(tmp));
		}
		final TMemory copy = memory.forkFromGlobal(foo);
		return copy;
	}

	public TValue executeReturn(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		if (functionType == TFunctionType.LEGACY_DEFINE) {
			return executeReturnLegacyDefine(context, memory, args);
		}
		if (functionType != TFunctionType.RETURN) {
			throw new EaterException("Illegal call here");
		}
		final TMemory copy = getNewMemory(memory, args);

		for (StringLocated sl : body) {
			final TLineType lineType = TLineType.getFromLine(sl.getString());
			final ConditionalContext conditionalContext = copy.peekConditionalContext();
			if ((conditionalContext == null || conditionalContext.conditionIsOkHere()) && lineType == TLineType.RETURN) {
				// System.err.println("s2=" + sl.getString());
				final EaterReturn eaterReturn = new EaterReturn(sl.getString());
				eaterReturn.execute(context, copy);
				// System.err.println("s3=" + eaterReturn.getValue2());
				return eaterReturn.getValue2();
			}
			context.executeOneLine(copy, lineType, sl, TFunctionType.RETURN);
		}
		throw new EaterException("no return");
		// return TValue.fromString("(NONE)");
	}

	private TValue executeReturnLegacyDefine(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		if (legacyDefinition == null) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args);
		final String tmp = context.applyFunctionsAndVariables(copy, legacyDefinition);
		if (tmp == null) {
			return TValue.fromString("");
		}
		return TValue.fromString(tmp);
		// eaterReturn.execute(context, copy);
		// // System.err.println("s3=" + eaterReturn.getValue2());
		// return eaterReturn.getValue2();
	}

	public final TFunctionType getFunctionType() {
		return functionType;
	}

	public final TFunctionSignature getSignature() {
		return signature;
	}

	public void setFunctionType(TFunctionType type) {
		this.functionType = type;
	}

	public void setLegacyDefinition(String legacyDefinition) {
		this.legacyDefinition = legacyDefinition;
	}

	public boolean isUnquoted() {
		return unquoted;
	}

	public boolean hasBody() {
		return body.size() > 0;
	}

	public void finalizeEnddefinelong() {
		if (functionType != TFunctionType.LEGACY_DEFINELONG) {
			throw new UnsupportedOperationException();
		}
		if (body.size() == 1) {
			this.functionType = TFunctionType.LEGACY_DEFINE;
			this.legacyDefinition = body.get(0).getString();
		}
	}

}
