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
package net.sourceforge.plantuml.cucadiagram.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.HideOrShow2;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LongCode;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class EntityFactory {

	private final Map<Code, ILeaf> leafs = new LinkedHashMap<Code, ILeaf>();
	private final List<Link> links = new ArrayList<Link>();
	private final Map<Code, IGroup> groups = new LinkedHashMap<Code, IGroup>();
	private int rawLayout;

	private final IGroup rootGroup = new GroupRoot(this);
	private final List<HideOrShow2> hides2;
	private final List<HideOrShow2> removed;

	public EntityFactory(List<HideOrShow2> hides2, List<HideOrShow2> removed) {
		this.hides2 = hides2;
		this.removed = removed;
	}

	public boolean isHidden(ILeaf leaf) {
		boolean hidden = false;
		for (HideOrShow2 hide : hides2) {
			hidden = hide.apply(hidden, leaf);
		}
		return hidden;
	}

	public boolean isRemoved(ILeaf leaf) {
		boolean result = false;
		for (HideOrShow2 hide : removed) {
			result = hide.apply(result, leaf);
		}
		return result;
	}

	public ILeaf createLeaf(Code code, Display display, LeafType entityType, IGroup parentContainer,
			Set<VisibilityModifier> hides, String namespaceSeparator) {
		if (entityType == null) {
			throw new IllegalArgumentException();
		}
		final Bodier bodier = new Bodier(entityType, hides);
		final LongCode longCode = getLongCode(code, namespaceSeparator);
		final EntityImpl result = new EntityImpl(this, code, bodier, parentContainer, entityType, longCode,
				namespaceSeparator, rawLayout);
		bodier.setLeaf(result);
		result.setDisplay(display);
		return result;
	}

	private LongCode getLongCode(Code code, String namespaceSeparator) {
		final LongCode result = LongCode.of(code.getFullName(), namespaceSeparator);
		// if (result.toString().equals(code.toString()) == false) {
		// System.err.println("result=" + result);
		// System.err.println(" code =" + code);
		// throw new UnsupportedOperationException();
		// }
		return result;
	}

	public IGroup createGroup(Code code, Display display, Code namespace2, GroupType groupType, IGroup parentContainer,
			Set<VisibilityModifier> hides, String namespaceSeparator) {
		if (groupType == null) {
			throw new IllegalArgumentException();
		}
		final Bodier bodier = new Bodier(null, hides);
		final LongCode longCode = getLongCode(code, namespaceSeparator);
		final EntityImpl result = new EntityImpl(this, code, bodier, parentContainer, groupType, namespace2, longCode,
				namespaceSeparator, rawLayout);
		if (Display.isNull(display) == false) {
			result.setDisplay(display);
		}
		return result;
	}

	public IGroup getRootGroup() {
		return rootGroup;
	}

	public final ILeaf getLeafsget(Code code) {
		return leafs.get(code);
	}

	public final Collection<ILeaf> getLeafsvalues() {
		return Collections.unmodifiableCollection(leafs.values());
	}

	public void addLeaf(ILeaf entity) {
		leafs.put(entity.getCode(), entity);
	}

	public void incRawLayout() {
		rawLayout++;
	}

	void removeLeaf(Code code) {
		final IEntity removed = leafs.remove(code);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
	}

	public void addGroup(IGroup group) {
		groups.put(group.getCode(), group);
	}

	void removeGroup(Code code) {
		final IEntity removed = groups.remove(code);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
	}

	public final Collection<IGroup> getGroupsvalues() {
		return Collections.unmodifiableCollection(groups.values());
	}

	public final IGroup getGroupsget(Code code) {
		return groups.get(code);
	}

	public final List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public void addLink(Link link) {
		if (link.isSingle() && containsSimilarLink(link)) {
			return;
		}
		links.add(link);
	}

	private boolean containsSimilarLink(Link other) {
		for (Link link : links) {
			if (other.sameConnections(link)) {
				return true;
			}
		}
		return false;
	}

	public void removeLink(Link link) {
		final boolean ok = links.remove(link);
		if (ok == false) {
			throw new IllegalArgumentException();
		}
	}

	public IGroup muteToGroup(Code code, Code namespace2, GroupType type, IGroup parent) {
		final ILeaf leaf = leafs.get(code);
		((EntityImpl) leaf).muteToGroup(namespace2, type, parent);
		final IGroup result = (IGroup) leaf;
		removeLeaf(code);
		return result;
	}

}
