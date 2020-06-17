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
package net.sourceforge.plantuml.tim.iterator;

import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterWhile;
import net.sourceforge.plantuml.tim.ExecutionContextWhile;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.expression.TokenStack;

public class CodeIteratorWhile extends AbstractCodeIterator {

	private final TContext context;
	private final TMemory memory;
	private final List<StringLocated> logs;

	public CodeIteratorWhile(CodeIterator source, TContext context, TMemory memory, List<StringLocated> logs) {
		super(source);
		this.context = context;
		this.memory = memory;
		this.logs = logs;
	}

	public StringLocated peek() throws EaterException, EaterExceptionLocated {
		int level = 0;
		while (true) {
			final StringLocated result = source.peek();
			if (result == null) {
				return null;
			}

			final ExecutionContextWhile currentWhile = memory.peekWhile();
			if (currentWhile != null && currentWhile.isSkipMe()) {
				if (result.getType() == TLineType.WHILE) {
					level++;
				} else if (result.getType() == TLineType.ENDWHILE) {
					level--;
					if (level == -1) {
						memory.pollWhile();
						level = 0;
					}
				}
				next();
				continue;
			}

			if (result.getType() == TLineType.WHILE) {
				logs.add(result);
				executeWhile(memory, result.getTrimmed());
				next();
				continue;
			} else if (result.getType() == TLineType.ENDWHILE) {
				logs.add(result);
				if (currentWhile == null) {
					throw EaterException.located("No while related to this endwhile");
				}
				final TValue value = currentWhile.conditionValue(result.getLocation(), context, memory);
				if (value.toBoolean()) {
					source.jumpToCodePosition(currentWhile.getStartWhile());
				} else {
					memory.pollWhile();
				}
				next();
				continue;
			}

			return result;
		}
	}

	private void executeWhile(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterWhile condition = new EaterWhile(s);
		condition.analyze(context, memory);
		final TokenStack whileExpression = condition.getWhileExpression();
		final ExecutionContextWhile theWhile = ExecutionContextWhile.fromValue(whileExpression,
				source.getCodePosition());
		final TValue value = theWhile.conditionValue(s.getLocation(), context, memory);
		if (value.toBoolean() == false) {
			theWhile.skipMe();
		}
		memory.addWhile(theWhile);
	}

}
