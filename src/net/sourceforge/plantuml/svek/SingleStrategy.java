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
package net.sourceforge.plantuml.svek;


public enum SingleStrategy {

	SQUARRE, HLINE, VLINE;

//	private Collection<Link> generateLinks(List<ILeaf> standalones) {
//		return putInSquare(standalones);
//	}

//	private Collection<Link> putInSquare(List<ILeaf> standalones) {
//		final List<Link> result = new ArrayList<Link>();
//		final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();
//		final int branch = computeBranch(standalones.size());
//		int headBranch = 0;
//		for (int i = 1; i < standalones.size(); i++) {
//			final int dist = i - headBranch;
//			final IEntity ent2 = standalones.get(i);
//			final Link link;
//			if (dist == branch) {
//				final IEntity ent1 = standalones.get(headBranch);
//				link = new Link(ent1, ent2, linkType, Display.NULL, 2);
//				headBranch = i;
//			} else {
//				final IEntity ent1 = standalones.get(i - 1);
//				link = new Link(ent1, ent2, linkType, Display.NULL, 1);
//			}
//			result.add(link);
//		}
//		return Collections.unmodifiableCollection(result);
//	}

	static int computeBranch(int size) {
		final double sqrt = Math.sqrt(size);
		final int r = (int) sqrt;
		if (r * r == size) {
			return r;
		}
		return r + 1;
	}

}
