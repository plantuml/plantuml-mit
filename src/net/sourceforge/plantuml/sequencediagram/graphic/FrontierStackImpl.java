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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FrontierStackImpl implements FrontierStack {

	class Stack {
		final private FrontierComplex current;
		final private FrontierComplex envelop;

		Stack(FrontierComplex current) {
			this(current, null);
		}

		private Stack(FrontierComplex current, FrontierComplex envelop) {
			this.current = current;
			this.envelop = envelop;
		}

		Stack addEnvelop(FrontierComplex env) {
			if (this.envelop == null) {
				return new Stack(this.current, env);
			}
			return new Stack(this.current, this.envelop.mergeMax(env));
		}
	}

	final private List<Stack> all;

	public FrontierStackImpl(double freeY, int rangeEnd) {
		final Stack s = new Stack(new FrontierComplex(freeY, rangeEnd));
		all = Collections.singletonList(s);
	}

	private FrontierStackImpl(List<Stack> all) {
		this.all = Collections.unmodifiableList(all);
	}

	private FrontierComplex getLast() {
		return all.get(all.size() - 1).current;
	}

	public double getFreeY(ParticipantRange range) {
		return getLast().getFreeY(range);
	}

	public FrontierStackImpl add(double delta, ParticipantRange range) {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack s = new Stack(getLast().add(delta, range));
		result.set(result.size() - 1, s);
		return new FrontierStackImpl(result);
	}

	public FrontierStack openBar() {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack s = new Stack(getLast().copy());
		result.add(s);
		return new FrontierStackImpl(result);
	}

	public FrontierStack restore() {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack openedBar = result.get(result.size() - 2);
		final Stack lastStack = result.get(result.size() - 1);
		result.set(result.size() - 2, openedBar.addEnvelop(lastStack.current));
		result.remove(result.size() - 1);
		final Stack s = new Stack(openedBar.current.copy());
		result.add(s);
		return new FrontierStackImpl(result);
	}

	public FrontierStack closeBar() {
		final List<Stack> result = new ArrayList<Stack>(all);
		final Stack openedBar = result.get(result.size() - 2);
		final Stack lastStack = result.get(result.size() - 1);
		final Stack merge = openedBar.addEnvelop(lastStack.current);
		result.set(result.size() - 2, new Stack(merge.envelop));
		result.remove(result.size() - 1);
		return new FrontierStackImpl(result);
	}

	public FrontierStackImpl copy() {
		// return new FrontierStackImpl(all);
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "nb=" + all.size() + " " + getLast().toString();
	}

}
