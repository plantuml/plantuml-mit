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
package net.sourceforge.plantuml.cucadiagram;


public abstract class EntityUtils {

	public static boolean groupRoot(IGroup g) {
		if (g == null) {
			throw new IllegalStateException();
		}
		return g instanceof GroupRoot;
	}

	private static boolean isParent(IGroup groupToBeTested, IGroup parentGroup) {
		if (groupToBeTested.isGroup() == false) {
			// Very strange!
			return false;
		}
		if (groupToBeTested.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		while (EntityUtils.groupRoot(groupToBeTested) == false) {
			if (groupToBeTested == parentGroup) {
				return true;
			}
			groupToBeTested = groupToBeTested.getParentContainer();
			if (groupToBeTested.isGroup() == false) {
				throw new IllegalStateException();
			}
		}
		return false;
	}

	public static boolean isPureInnerLink12(IGroup group, Link link) {
		if (group.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		final IEntity e1 = link.getEntity1();
		final IEntity e2 = link.getEntity2();
		final IGroup group1 = e1.getParentContainer();
		final IGroup group2 = e2.getParentContainer();
		if (isParent(group1, group) && isParent(group2, group)) {
			return true;
		}
		return false;
	}

	public static boolean isPureInnerLink3(IGroup group, Link link) {
		if (group.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		final IEntity e1 = link.getEntity1();
		final IEntity e2 = link.getEntity2();
		final IGroup group1 = e1.getParentContainer();
		final IGroup group2 = e2.getParentContainer();
		if (isParent(group2, group) == isParent(group1, group)) {
			return true;
		}
		return false;
	}
}
