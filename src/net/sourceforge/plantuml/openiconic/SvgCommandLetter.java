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
package net.sourceforge.plantuml.openiconic;

import net.sourceforge.plantuml.StringUtils;

public class SvgCommandLetter implements SvgCommand {

	final private char letter;

	public SvgCommandLetter(String letter) {
		if (letter.matches("[a-zA-Z]") == false) {
			throw new IllegalArgumentException();
		}
		this.letter = letter.charAt(0);
	}

	@Override
	public String toString() {
		return super.toString() + " " + letter;
	}

	public String toSvg() {
		return "" + letter;
	}

	public int argumentNumber() {
		switch (StringUtils.goLowerCase(letter)) {
		case 'm':
		case 'M':
		case 'l':
			return 2;
		case 'z':
			return 0;
		case 'c':
			return 6;
		case 's':
			return 4;
		case 'a':
			return 7;
		}
		throw new UnsupportedOperationException("" + letter);
	}

//	public UGraphic drawMe(UGraphic ug, Iterator<SvgCommand> it) {
//		System.err.println("drawMe " + letter);
//		final List<SvgCommandNumber> numbers = new ArrayList<SvgCommandNumber>();
//		for (int i = 0; i < argumentNumber(); i++) {
//			numbers.add((SvgCommandNumber) it.next());
//		}
//		return drawMe(ug, numbers);
//	}
//
//	private UGraphic drawMe(UGraphic ug, List<SvgCommandNumber> numbers) {
//		switch (letter) {
//		case 'M':
//			final double x = numbers.get(0).getDouble();
//			final double y = numbers.get(1).getDouble();
//			return ug.apply(new UTranslate(x, y));
//		}
//		return ug;
//
//	}

	public boolean isUpperCase() {
		return Character.isUpperCase(letter);
	}

	public boolean is(char c) {
		return this.letter == c;
	}

	public char getLetter() {
		return letter;
	}
}
