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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterStartsub;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;

public class Sub2 {

	private final String name;
	private final List<StringLocated> lines = new ArrayList<StringLocated>();

	public Sub2(String name) {
		this.name = name;
	}

	public void add(StringLocated s) {
		this.lines.add(s);
	}

	public final List<StringLocated> lines() {
		return Collections.unmodifiableList(lines);
	}

	public static Sub2 fromFile(ReadLine reader, String blocname, TContext context, TMemory memory) throws IOException,
			EaterException {
		Sub2 result = null;
		StringLocated s = null;
		while ((s = reader.readLine()) != null) {
			final TLineType type = TLineType.getFromLine(s.getTrimmed().getString());
			if (type == TLineType.STARTSUB) {
				final EaterStartsub eater = new EaterStartsub(s.getTrimmed().getString());
				eater.execute(context, memory);
				if (eater.getSubname().equals(blocname)) {
					result = new Sub2(blocname);
				}
				continue;
			}
			if (type == TLineType.ENDSUB && result != null) {
				reader.close();
				return result;
			}
			if (result != null) {
				result.add(s);
			}
		}
		reader.close();
		return null;
	}

}
