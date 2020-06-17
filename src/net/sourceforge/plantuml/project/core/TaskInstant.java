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
package net.sourceforge.plantuml.project.core;

import net.sourceforge.plantuml.project.lang.Complement;
import net.sourceforge.plantuml.project.time.Wink;

public class TaskInstant implements Complement {

	private final Moment task;
	private final TaskAttribute attribute;
	private final int delta;

	public TaskInstant(Moment task, TaskAttribute attribute) {
		this(task, attribute, 0);
	}

	private TaskInstant(Moment task, TaskAttribute attribute, int delta) {
		this.task = task;
		this.attribute = attribute;
		this.delta = delta;
		if (attribute != TaskAttribute.START && attribute != TaskAttribute.END) {
			throw new IllegalArgumentException();
		}
	}

	public TaskInstant withDelta(int newDelta) {
		return new TaskInstant(task, attribute, newDelta);
	}

	private Wink manageDelta(Wink value) {
		if (delta > 0) {
			for (int i = 0; i < delta; i++) {
				value = value.increment();
			}
		}
		if (delta < 0) {
			for (int i = 0; i < -delta; i++) {
				value = value.decrement();
			}
		}
		return value;
	}

	public Wink getInstantPrecise() {
		if (attribute == TaskAttribute.START) {
			return manageDelta(task.getStart());
		}
		if (attribute == TaskAttribute.END) {
			return manageDelta(task.getEnd().increment());
		}
		throw new IllegalStateException();
	}

	public Wink getInstantTheorical() {
		if (attribute == TaskAttribute.START) {
			return manageDelta(task.getStart());
		}
		if (attribute == TaskAttribute.END) {
			return manageDelta(task.getEnd());
		}
		throw new IllegalStateException();
	}

	@Override
	public String toString() {
		return attribute.toString() + " of " + task;
	}

	public final Moment getMoment() {
		return task;
	}

	public final boolean isTask() {
		return task instanceof Task;
	}

	public final TaskAttribute getAttribute() {
		return attribute;
	}

}
