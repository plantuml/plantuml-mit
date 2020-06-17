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
package net.sourceforge.plantuml.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {

	public static <E> Collection<List<E>> selectUpTo(List<E> original, int nb) {
		final List<List<E>> result = new ArrayList<List<E>>();
		for (int i = 1; i <= nb; i++) {
			result.addAll(selectExactly(original, i));
		}
		return Collections.unmodifiableList(result);
	}

	public static <E> Collection<List<E>> selectExactly(List<E> original, int nb) {
		if (nb < 0) {
			throw new IllegalArgumentException();
		}
		if (nb == 0) {
			return Collections.emptyList();
		}
		if (nb == 1) {
			final List<List<E>> result = new ArrayList<List<E>>();
			for (E element : original) {
				result.add(Collections.singletonList(element));
			}
			return result;

		}
		if (nb > original.size()) {
			return Collections.emptyList();
		}
		if (nb == original.size()) {
			return Collections.singletonList(original);
		}
		final List<List<E>> result = new ArrayList<List<E>>();

		for (List<E> subList : selectExactly(original.subList(1, original.size()), nb - 1)) {
			final List<E> newList = new ArrayList<E>();
			newList.add(original.get(0));
			newList.addAll(subList);
			result.add(Collections.unmodifiableList(newList));
		}
		result.addAll(selectExactly(original.subList(1, original.size()), nb));

		return Collections.unmodifiableList(result);
	}
}
