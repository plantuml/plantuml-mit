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
package net.sourceforge.plantuml.html;

import java.io.PrintWriter;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;

public final class LinkHtmlPrinter {

	private final Link link;
	// private final Entity entity;
	private final boolean chiral;

	public LinkHtmlPrinter(Link link, IEntity entity) {
		this.link = link;
		if (link.getEntity1() == entity) {
			this.chiral = false;
		} else if (link.getEntity2() == entity) {
			this.chiral = true;
		} else {
			throw new IllegalArgumentException();
		}
	}

	void printLink(PrintWriter pw) {
		final String ent1h;
		final String ent2h;
		if (chiral) {
			ent1h = htmlLink(link.getEntity1());
			ent2h = "<i>" + StringUtils.unicodeForHtml(link.getEntity2().getCode().getFullName()) + "</i>";
		} else {
			ent1h = "<i>" + StringUtils.unicodeForHtml(link.getEntity1().getCode().getFullName()) + "</i>";
			ent2h = htmlLink(link.getEntity2());
		}
		String label = link.getLabel() == null ? null : StringUtils.unicodeForHtml(link.getLabel());
		String ent1 = ent1h;
		String ent2 = ent2h;
		if (link.getQualifier1() != null) {
			ent1 = ent1 + " (" + link.getQualifier1() + ")";
			if (label != null) {
				label = "(" + link.getQualifier1() + " " + ent1h + ") " + label;
			}
		}
		if (link.getQualifier2() != null) {
			ent2 = ent2 + " (" + link.getQualifier2() + ")";
			if (label != null) {
				label = label + " (" + link.getQualifier2() + " " + ent2h + ")";
			}
		}
		if (chiral) {
			pw.println(getHtmlChiral(ent1, ent2));
		} else {
			pw.println(getHtml(ent1, ent2));
		}
		if (label != null) {
			pw.println("&nbsp;:&nbsp;");
			pw.println(label);
		}
	}

	private String getHtml(String ent1, String ent2) {
		final LinkDecor decor1 = link.getType().getDecor1();
		final LinkDecor decor2 = link.getType().getDecor2();

		if (decor1 == LinkDecor.NONE && decor2 == LinkDecor.NONE) {
			return ent1 + " is linked to " + ent2;
		}
		if (decor1 == LinkDecor.NONE && decor2 == LinkDecor.EXTENDS) {
			return ent1 + " is extended by " + ent2;
		}
		if (decor1 == LinkDecor.EXTENDS && decor2 == LinkDecor.NONE) {
			return ent1 + " extends " + ent2;
		}
		if (decor2 == LinkDecor.AGREGATION) {
			return ent1 + " is aggregated by  " + ent2;
		}
		if (decor1 == LinkDecor.AGREGATION) {
			return ent1 + " aggregates " + ent2;
		}
		if (decor2 == LinkDecor.COMPOSITION) {
			return ent1 + " is composed by " + ent2;
		}
		if (decor1 == LinkDecor.COMPOSITION) {
			return ent1 + " composes " + ent2;
		}
		if (decor1 == LinkDecor.NONE && decor2 == LinkDecor.ARROW) {
			return ent1 + " is navigable from  " + ent2;
		}
		if (decor1 == LinkDecor.ARROW && decor2 == LinkDecor.NONE) {
			return ent1 + " navigates to  " + ent2;
		}
		return ent1 + " " + decor1 + "-" + decor2 + " " + ent2;

	}

	private String getHtmlChiral(String ent1, String ent2) {
		final LinkDecor decor1 = link.getType().getDecor1();
		final LinkDecor decor2 = link.getType().getDecor2();

		if (decor1 == LinkDecor.NONE && decor2 == LinkDecor.NONE) {
			return ent2 + " is linked to " + ent1;
		}
		if (decor1 == LinkDecor.NONE && decor2 == LinkDecor.EXTENDS) {
			return ent2 + " extends " + ent1;
		}
		if (decor1 == LinkDecor.EXTENDS && decor2 == LinkDecor.NONE) {
			return ent2 + " is extended by " + ent1;
		}
		if (decor2 == LinkDecor.AGREGATION) {
			return ent2 + " aggregates " + ent1;
		}
		if (decor1 == LinkDecor.AGREGATION) {
			return ent2 + " is aggregated by " + ent1;
		}
		if (decor2 == LinkDecor.COMPOSITION) {
			return ent2 + " composes " + ent1;
		}
		if (decor1 == LinkDecor.COMPOSITION) {
			return ent2 + " is composed by " + ent1;
		}
		if (decor1 == LinkDecor.NONE && decor2 == LinkDecor.ARROW) {
			return ent2 + " navigates to  " + ent1;
		}
		if (decor1 == LinkDecor.ARROW && decor2 == LinkDecor.NONE) {
			return ent2 + " is navigable from  " + ent1;
		}
		return ent1 + " " + decor1 + "-" + decor2 + " " + ent2;
	}

	static String htmlLink(IEntity ent) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"");
		sb.append(urlOf(ent));
		sb.append("\">");
		sb.append(StringUtils.unicodeForHtml(ent.getCode().getFullName()));
		sb.append("</a>");
		return sb.toString();
	}

	static String urlOf(IEntity ent) {
		if (ent.getLeafType() == LeafType.NOTE) {
			throw new IllegalArgumentException();
		}
		if (ent.getCode().getFullName().matches("[-\\w_ .]+")) {
			return StringUtils.unicodeForHtml(ent.getCode().getFullName()) + ".html";
		}
		return StringUtils.unicodeForHtml(ent.getUid()) + ".html";
	}

}
