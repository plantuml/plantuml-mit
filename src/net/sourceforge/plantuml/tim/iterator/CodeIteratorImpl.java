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

public class CodeIteratorImpl implements CodeIterator {

	private final List<StringLocated> list;
	private int current = 0;
	private int countJump = 0;

	static class Position implements CodePosition {
		final int pos;

		Position(int pos) {
			this.pos = pos;
		}

//		@Override
//		public String toString() {
//			return "-->" + list.get(pos);
//		}
	}

	public CodeIteratorImpl(List<StringLocated> list) {
		this.list = list;
	}

	public StringLocated peek() {
		if (current == list.size()) {
			return null;
		}
		if (current > list.size()) {
			throw new IllegalStateException();
		}
		return list.get(current);
	}

	public void next() {
		if (current >= list.size()) {
			throw new IllegalStateException();
		}
		assert current < list.size();
		current++;
		assert current <= list.size();
	}

	public CodePosition getCodePosition() {
		return new Position(current);
	}

	public void jumpToCodePosition(CodePosition newPosition) throws EaterException {
		this.countJump++;
		if (this.countJump > 999) {
			throw EaterException.unlocated("Infinite loop?");
		}
		final Position pos = (Position) newPosition;
		this.current = pos.pos;

	}

}
