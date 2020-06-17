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
package net.sourceforge.plantuml.salt.factory;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.Dictionary;
import net.sourceforge.plantuml.salt.Positionner2;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.Terminator;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementPyramid;
import net.sourceforge.plantuml.salt.element.ElementText;
import net.sourceforge.plantuml.salt.element.TableStrategy;

public class ElementFactoryPyramid extends AbstractElementFactoryComplex {

	public ElementFactoryPyramid(DataSource dataSource, Dictionary dictionary) {
		super(dataSource, dictionary);
	}

	public Terminated<Element> create() {
		if (ready() == false) {
			throw new IllegalStateException();
		}
		final Terminated<String> tmp = getDataSource().next();
		final String header = tmp.getElement();
		assert header.startsWith("{");
		String title = null;

		TableStrategy strategy = TableStrategy.DRAW_NONE;
		if (header.length() == 2) {
			strategy = TableStrategy.fromChar(header.charAt(1));
		}
		if (strategy == TableStrategy.DRAW_OUTSIDE_WITH_TITLE && tmp.getTerminator() == Terminator.NEWCOL) {
			title = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(getDataSource().next().getElement(), "\"");
		}

		final Positionner2 positionner = new Positionner2();

		while (getDataSource().peek(0).getElement().equals("}") == false) {
			final Terminated<Element> next = getNextElement();
			if (isStar(next.getElement())) {
				positionner.mergeLeft(next.getTerminator());
			} else {
				positionner.add(next);
			}
		}
		final Terminated<String> next = getDataSource().next();
		return new Terminated<Element>(new ElementPyramid(positionner, strategy, title, getDictionary()),
				next.getTerminator());
	}

	private boolean isStar(Element element) {
		if (element instanceof ElementText == false) {
			return false;
		}
		return "*".equals(((ElementText) element).getText());
	}

	public boolean ready() {
		final String text = getDataSource().peek(0).getElement();
		if (text.equals("{") || text.equals("{+") || text.equals("{^") || text.equals("{#") || text.equals("{!")
				|| text.equals("{-")) {
			final String text1 = getDataSource().peek(1).getElement();
			if (text1.matches("[NSEW]=|T")) {
				return false;
			}
			return true;
		}
		return false;
	}
}
