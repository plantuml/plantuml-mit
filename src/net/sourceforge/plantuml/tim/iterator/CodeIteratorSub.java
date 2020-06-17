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

import java.util.Collections;
import java.util.Map;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc.Sub;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterStartsub;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;

public class CodeIteratorSub extends AbstractCodeIterator {

	private final Map<String, Sub> subs;

	private CodeIterator readingInProgress;

	private final TMemory memory;
	private final TContext context;

	public CodeIteratorSub(CodeIterator source, Map<String, Sub> subs, TContext context, TMemory memory) {
		super(source);
		this.context = context;
		this.memory = memory;
		this.subs = subs;
	}

	public Map<String, Sub> getSubs() {
		return Collections.unmodifiableMap(subs);
	}

	public StringLocated peek() throws EaterException, EaterExceptionLocated {
		StringLocated result = source.peek();
		if (result == null) {
			return null;
		}
		if (result.getType() == TLineType.STARTSUB) {
			final EaterStartsub eater = new EaterStartsub(result.getTrimmed());
			eater.analyze(context, memory);
			final Sub created = new Sub(eater.getSubname());
			this.subs.put(eater.getSubname(), created);
			source.next();
			StringLocated s = null;
			while ((s = source.peek()) != null) {
				if (s.getType() == TLineType.STARTSUB) {
					throw EaterException.located("Cannot nest sub");
				} else if (s.getType() == TLineType.ENDSUB) {
					source.next();
					readingInProgress = new CodeIteratorImpl(created.lines());
					break;
				} else {
					created.add(s);
					source.next();
				}
			}
		}
		if (readingInProgress != null) {
			return readingInProgress.peek();
		}
		return result;
	}

	@Override
	public void next() throws EaterException, EaterExceptionLocated {
		if (readingInProgress == null) {
			source.next();
			return;
		}
		readingInProgress.next();
		if (readingInProgress.peek() == null) {
			readingInProgress = null;
		}
	}

}
