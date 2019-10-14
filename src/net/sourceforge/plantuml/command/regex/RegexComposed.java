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
package net.sourceforge.plantuml.command.regex;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringLocated;

public abstract class RegexComposed implements IRegex {

	protected static final AtomicInteger nbCreateMatches = new AtomicInteger();
	private final List<IRegex> partials;

	protected final List<IRegex> partials() {
		return partials;
	}

	abstract protected String getFullSlow();

	private final AtomicReference<Pattern2> fullCached = new AtomicReference<Pattern2>();

	private Pattern2 getPattern2() {
		Pattern2 result = fullCached.get();
		if (result == null) {
			final String fullSlow = getFullSlow();
			result = MyPattern.cmpile(fullSlow, Pattern.CASE_INSENSITIVE);
			fullCached.set(result);
		}
		return result;
	}

	final protected boolean isCompiled() {
		return fullCached.get() != null;
	}

	public RegexComposed(IRegex... partial) {
		this.partials = Collections.unmodifiableList(Arrays.asList(partial));
	}

	public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		nbCreateMatches.incrementAndGet();
		final Map<String, RegexPartialMatch> result = new HashMap<String, RegexPartialMatch>();
		for (IRegex r : partials) {
			result.putAll(r.createPartialMatch(it));
		}
		return result;
	}

	final public int count() {
		int cpt = getStartCount();
		for (IRegex r : partials) {
			cpt += r.count();
		}
		return cpt;
	}

	protected int getStartCount() {
		return 0;
	}

	public RegexResult matcher(String s) {
		final Matcher2 matcher = getPattern2().matcher(s);
		if (matcher.find() == false) {
			return null;
		}

		final Iterator<String> it = new MatcherIterator(matcher);
		return new RegexResult(createPartialMatch(it));
	}

	public boolean match(StringLocated s) {
		return getPattern2().matcher(s.getString()).find();
	}

	final public String getPattern() {
		return getPattern2().pattern();
	}

	final protected List<IRegex> getPartials() {
		return partials;
	}

}
