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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.Collection;

import net.sourceforge.plantuml.Log;

public class DotxMaker {

	private final Cluster root;
	private final Collection<Path> paths;

	public DotxMaker(Cluster root, Collection<Path> paths) {
		this.root = root;
		this.paths = paths;
	}

	public String createDotString(String... dotStrings) {
		final StringBuilder sb = new StringBuilder();
		sb.append("digraph unix {");

		for (String s : dotStrings) {
			sb.append(s);
		}

		sb.append("compound=true;");

		printCluster(sb, root);

		for (Path p : paths) {
			sb.append(getPathString(p) + ";");
		}

		sb.append("}");

		return sb.toString();
	}

	private void printCluster(StringBuilder sb, Cluster cl) {
		if (cl.getContents().size() == 0 && cl.getSubClusters().size() == 0) {
			throw new IllegalStateException(cl.toString());
		}
		for (Cluster sub : cl.getSubClusters()) {
			sb.append("subgraph cluster" + sub.getUid() + " {");
			if (sub.getTitleWidth() > 0 && sub.getTitleHeight() > 0) {
				sb.append("label=<<TABLE FIXEDSIZE=\"TRUE\" WIDTH=\"" + sub.getTitleWidth() + "\" HEIGHT=\""
						+ sub.getTitleHeight() + "\"><TR><TD></TD></TR></TABLE>>");
			}

			printCluster(sb, sub);
			sb.append("}");

		}
		for (Block b : cl.getContents()) {
			sb.append("b" + b.getUid() + getNodeAttibute(b) + ";");
		}

	}

	private String getPathString(Path p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		final StringBuilder sb = new StringBuilder("b" + p.getStart().getUid() + " -> b" + p.getEnd().getUid());
		sb.append(" [dir=none, arrowhead=none, headclip=true, tailclip=true");
		final int len = p.getLength();
		if (len >= 3) {
			sb.append(",minlen=" + (len - 1));
		}
		if (p.getLabel() == null) {
			sb.append("]");
		} else {
			final Dimension2D size = p.getLabel().getSize();
			sb.append(", label=<<TABLE FIXEDSIZE=\"TRUE\" WIDTH=\"" + size.getWidth() + "\" HEIGHT=\""
					+ size.getHeight() + "\"><TR><TD></TD></TR></TABLE>>]");
		}

		if (p.getLength() <= 1) {
			final boolean samePackage = p.getStart().getParent() == p.getEnd().getParent();
			if (samePackage) {
				sb.append("{rank=same; b" + p.getStart().getUid() + "; b" + p.getEnd().getUid() + "}");
			} else {
				Log.println("!!!!!!!!!!!!!!!!!TURNING ARROUND DOT BUG!!!!!!!!!!!!!!!!!!");
			}
		}

		return sb.toString();
	}

	private String getNodeAttibute(Block b) {
		final StringBuilder sb = new StringBuilder("[");
		sb.append("label=\"\",");
		sb.append("fixedsize=true,");
		sb.append("width=" + b.getSize().getWidth() / 72.0 + ",");
		sb.append("height=" + b.getSize().getHeight() / 72.0 + ",");
		sb.append("shape=rect");
		sb.append("]");
		return sb.toString();
	}

}
