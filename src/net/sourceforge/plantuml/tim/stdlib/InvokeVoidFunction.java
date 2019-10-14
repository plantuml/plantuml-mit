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
package net.sourceforge.plantuml.tim.stdlib;

import java.util.List;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterFunctionCall;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TFunctionType;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

public class InvokeVoidFunction implements TFunction {

	public TFunctionSignature getSignature() {
		return new TFunctionSignature("%invoke_void_func", 1);
	}

	public boolean canCover(int nbArg) {
		return nbArg > 0;
	}

	public TFunctionType getFunctionType() {
		return TFunctionType.VOID;
	}

	public void executeVoid(TContext context, TMemory memory, String s) throws EaterException {
		final EaterFunctionCall call = new EaterFunctionCall(s, false, isUnquoted());
		call.execute((TContext) context, memory);
		final List<TValue> values = call.getValues();
		final String fname = values.get(0).toString();
		final List<TValue> args = values.subList(1, values.size());
		final TFunctionSignature signature = new TFunctionSignature(fname, args.size());
		final TFunction func = context.getFunctionSmart(signature);
		if (func == null) {
			throw new EaterException("Cannot find void function " + fname);
		}
		func.executeVoidInternal(context, memory, args);
	}

	public void executeVoidInternal(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		throw new UnsupportedOperationException();
	}

	public TValue executeReturn(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		throw new UnsupportedOperationException();
	}

	public boolean isUnquoted() {
		return false;
	}

}
