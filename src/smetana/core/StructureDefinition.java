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

package smetana.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StructureDefinition {

	private static final Map<Class, StructureDefinition> all = new HashMap<Class, StructureDefinition>();

	public static StructureDefinition from(Class cl) {
		if (cl == null) {
			throw new IllegalArgumentException();
		}
		StructureDefinition result = all.get(cl);
		if (result == null) {
			result = new StructureDefinition(cl);
			all.put(cl, result);
		}
		return result;
	}

	private final Class cl;
	private Map<String, Bucket> buckets; // = new LinkedHashMap<String, StructureDefinition.Bucket>();

	private StructureDefinition(Class cl) {
		// JUtils.LOG("BUIDLING StructureDefinition " + cl);
		this.cl = cl;
	}

	public String toString() {
		return (cl == null ? "NO_CLASS" : cl.getName()) + " " + buckets;
	}

	private Map<String, Bucket> buckets() {
		if (buckets == null) {
			final List<String> definition = CType.getDefinition(cl);
			JUtils.LOG("StructureDefinition::run for " + cl);
			JUtils.LOG("def=" + definition);
			JUtils.LOG("first=" + definition.get(0));

			buckets = new LinkedHashMap<String, Bucket>();

			if (definition.get(0).equals("typedef enum")) {
				final String last = definition.get(definition.size() - 1);
				if (last.matches("\\w+") == false) {
					throw new UnsupportedOperationException();
				}
				buckets.put(last, Bucket.buildEnum(last, definition));
				return buckets;
			}

			if (definition.get(0).equals("typedef struct gvplugin_active_textlayout_s") == false
					&& definition.get(0).equals("typedef struct color_s") == false
					&& definition.get(0).equals("typedef struct") == false
					&& definition.get(0).equals("typedef struct pointf_s") == false
					&& definition.get(0).equals("typedef struct gvplugin_active_layout_s") == false
					&& definition.get(0).equals("typedef struct GVCOMMON_s") == false
					&& definition.get(0).equals("struct " + cl.getSimpleName()) == false
					&& definition.get(0).equals("typedef struct " + cl.getSimpleName()) == false
					&& definition.get(0).equals("typedef struct " + cl.getSimpleName().replaceFirst("_t", "_s")) == false
					&& definition.get(0).equals("typedef union " + cl.getSimpleName()) == false) {
				throw new IllegalStateException("<struct " + cl.getSimpleName() + "> VERSUS <" + definition.get(0)
						+ ">");
			}
			if (definition.get(1).equals("{") == false) {
				throw new IllegalStateException();
			}

			int last = definition.size() - 1;
			if (definition.get(definition.size() - 2).equals("}")
					&& definition.get(definition.size() - 1).equals(cl.getSimpleName())) {
				last--;
			}

			if (definition.get(last).equals("}") == false) {
				throw new IllegalStateException();
			}

			for (Iterator<String> it = definition.subList(2, last).iterator(); it.hasNext();) {
				buckets.putAll(Bucket.buildSome(it));
			}
		}
		return buckets;
	}

	public Set<String> getFields() {
		return buckets().keySet();
	}

	public Collection<Bucket> getBuckets() {
		return buckets().values();
	}

	public Map<String, Bucket> getBucketsMap() {
		return buckets();
	}

	public Bucket getBucket(String field) {
		final Bucket result = buckets().get(field);
		if (result == null) {
			throw new IllegalArgumentException(field);
		}
		return result;
	}

	public Class getTheClass() {
		return cl;
	}

	public boolean containsFieldName(String fieldName) {
		return buckets().keySet().contains(fieldName);
	}

}
