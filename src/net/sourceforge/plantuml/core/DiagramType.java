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
package net.sourceforge.plantuml.core;

import net.sourceforge.plantuml.utils.StartUtils;

public enum DiagramType {
	UML, BPM, DITAA, DOT, PROJECT, JCCKIT, SALT, FLOW, CREOLE, JUNGLE, CUTE, MATH, LATEX, DEFINITION, GANTT, NW, MINDMAP, WBS, UNKNOWN;

	static public DiagramType getTypeFromArobaseStart(String s) {
		s = s.toLowerCase();
		// if (s.startsWith("@startuml2")) {
		// return UML2;
		// }
		if (StartUtils.startsWithSymbolAnd("startbpm", s)) {
			return BPM;
		}
		if (StartUtils.startsWithSymbolAnd("startuml", s)) {
			return UML;
		}
		if (StartUtils.startsWithSymbolAnd("startdot", s)) {
			return DOT;
		}
		if (StartUtils.startsWithSymbolAnd("startjcckit", s)) {
			return JCCKIT;
		}
		if (StartUtils.startsWithSymbolAnd("startditaa", s)) {
			return DITAA;
		}
		if (StartUtils.startsWithSymbolAnd("startproject", s)) {
			return PROJECT;
		}
		if (StartUtils.startsWithSymbolAnd("startsalt", s)) {
			return SALT;
		}
		if (StartUtils.startsWithSymbolAnd("startflow", s)) {
			return FLOW;
		}
		if (StartUtils.startsWithSymbolAnd("startcreole", s)) {
			return CREOLE;
		}
		if (StartUtils.startsWithSymbolAnd("starttree", s)) {
			return JUNGLE;
		}
		if (StartUtils.startsWithSymbolAnd("startcute", s)) {
			return CUTE;
		}
		if (StartUtils.startsWithSymbolAnd("startmath", s)) {
			return MATH;
		}
		if (StartUtils.startsWithSymbolAnd("startlatex", s)) {
			return LATEX;
		}
		if (StartUtils.startsWithSymbolAnd("startdef", s)) {
			return DEFINITION;
		}
		if (StartUtils.startsWithSymbolAnd("startgantt", s)) {
			return GANTT;
		}
		if (StartUtils.startsWithSymbolAnd("startnwdiag", s)) {
			return NW;
		}
		if (StartUtils.startsWithSymbolAnd("startmindmap", s)) {
			return MINDMAP;
		}
		if (StartUtils.startsWithSymbolAnd("startwbs", s)) {
			return WBS;
		}
		return UNKNOWN;
	}
}
