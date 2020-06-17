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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Englober;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Englobers {

	private final List<Englober> englobers = new ArrayList<Englober>();

	public Englobers(TileArguments tileArguments) {
		Englober pending = null;
		for (Participant p : tileArguments.getLivingSpaces().participants()) {
			final ParticipantEnglober englober = tileArguments.getLivingSpaces().get(p).getEnglober();
			if (englober == null) {
				pending = null;
				continue;
			}
			assert englober != null;
			if (pending != null && englober == pending.getParticipantEnglober()) {
				pending.add(p);
				continue;
			}
			pending = Englober.createTeoz(englober, p, tileArguments, tileArguments.getSkinParam()
					.getCurrentStyleBuilder());
			englobers.add(pending);
		}
	}

	public int size() {
		return englobers.size();
	}

	public double getOffsetForEnglobers(StringBounder stringBounder) {
		double result = 0;
		for (Englober englober : englobers) {
			final double height = englober.getPreferredHeight();
			if (height > result) {
				result = height;
			}
		}
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		Englober last = null;
		for (Englober current : englobers) {
			current.addInternalConstraints();
			if (last != null) {
				last.addConstraintAfter(current);
			}
			last = current;
		}
	}

	public void drawEnglobers(UGraphic ug, double height, Context2D context) {
		for (Englober englober : englobers) {
			englober.drawEnglober(ug, height, context);
		}
	}

	public Real getMinX(StringBounder stringBounder) {
		if (size() == 0) {
			throw new IllegalStateException();
		}
		final List<Real> all = new ArrayList<Real>();
		for (Englober englober : englobers) {
			all.add(englober.getMinX(stringBounder));
		}
		return RealUtils.min(all);
	}

	public Real getMaxX(StringBounder stringBounder) {
		if (size() == 0) {
			throw new IllegalStateException();
		}
		final List<Real> all = new ArrayList<Real>();
		for (Englober englober : englobers) {
			all.add(englober.getMaxX(stringBounder));
		}
		return RealUtils.max(all);
	}

}
