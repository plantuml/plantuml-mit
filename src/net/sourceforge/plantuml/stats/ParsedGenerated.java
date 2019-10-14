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
package net.sourceforge.plantuml.stats;

import java.util.prefs.Preferences;

import net.sourceforge.plantuml.api.NumberAnalyzed;
import net.sourceforge.plantuml.api.NumberAnalyzedDated;

public class ParsedGenerated {

	private final NumberAnalyzed parsed;
	private final NumberAnalyzed generated;

	private ParsedGenerated(NumberAnalyzed parsed, NumberAnalyzed generated) {
		if (parsed == null || generated == null) {
			throw new IllegalArgumentException();
		}
		this.parsed = parsed;
		this.generated = generated;

	}

	public void reset() {
		parsed.reset();
		generated.reset();
	}

	public static ParsedGenerated createVolatile() {
		return new ParsedGenerated(new NumberAnalyzed(), new NumberAnalyzed());
	}

	public static ParsedGenerated createVolatileDated() {
		return new ParsedGenerated(new NumberAnalyzedDated(), new NumberAnalyzedDated());
	}

	public static ParsedGenerated loadDated(Preferences prefs, String name) {
		NumberAnalyzedDated parsed = NumberAnalyzedDated.load(name + ".p", prefs);
		if (parsed == null) {
			parsed = new NumberAnalyzedDated(name + ".p");
		}
		NumberAnalyzedDated generated = NumberAnalyzedDated.load(name + ".g", prefs);
		if (generated == null) {
			generated = new NumberAnalyzedDated(name + ".g");
		}
		return new ParsedGenerated(parsed, generated);
	}

	public NumberAnalyzed parsed() {
		return parsed;
	}

	public NumberAnalyzed generated() {
		return generated;
	}

	public NumberAnalyzedDated parsedDated() {
		return (NumberAnalyzedDated) parsed;
	}

	public NumberAnalyzedDated generatedDated() {
		return (NumberAnalyzedDated) generated;
	}

	public long getId() {
		final String comment = parsedDated().getComment();
		final int x = comment.indexOf('/');
		if (x == -1) {
			return -1;
		}
		return Long.parseLong(comment.substring(0, x), 36);
	}

	public String getVersion() {
		final String comment = parsedDated().getComment();
		final int x = comment.indexOf('/');
		if (x == -1) {
			return " ";
		}
		return comment.substring(x + 1);
	}

}
