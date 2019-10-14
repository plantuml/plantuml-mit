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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;

public class TeXIconBuilder {

	private Icon icon;

	public TeXIconBuilder(String tex, Color foregroundColor) throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// TeXFormula formula = new TeXFormula(latex);
		final Class<?> clTeXFormula = Class.forName("org.scilab.forge.jlatexmath.TeXFormula");
		final Object formula = clTeXFormula.getConstructor(String.class).newInstance(tex);

		// TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();
		final Class<?> clTeXIconBuilder = clTeXFormula.getClasses()[0];
		final Object builder = clTeXIconBuilder.getConstructors()[0].newInstance(formula);
		clTeXIconBuilder.getMethod("setStyle", int.class).invoke(builder, 0);
		clTeXIconBuilder.getMethod("setSize", float.class).invoke(builder, (float) 20);
		icon = (Icon) clTeXIconBuilder.getMethod("build").invoke(builder);

		final int margin = 1;
		final Insets insets = new Insets(margin, margin, margin, margin);
		icon.getClass().getMethod("setInsets", insets.getClass()).invoke(icon, insets);
		icon.getClass().getMethod("setForeground", foregroundColor.getClass()).invoke(icon, foregroundColor);
	}

	public Icon getIcon() {
		return icon;
	}

}
