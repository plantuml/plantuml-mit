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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.StringBounder;

public class Bibliotekon {

	private final List<Cluster> allCluster = new ArrayList<Cluster>();

	private final Map<ILeaf, Node> nodeMap = new LinkedHashMap<ILeaf, Node>();;

	private final List<Line> lines0 = new ArrayList<Line>();
	private final List<Line> lines1 = new ArrayList<Line>();
	private final List<Line> allLines = new ArrayList<Line>();

	public Node createNode(ILeaf ent, IEntityImage image, ColorSequence colorSequence, StringBounder stringBounder) {
		final Node node = new Node(ent, image, colorSequence, stringBounder);
		nodeMap.put(ent, node);
		return node;
	}

	public Cluster getCluster(IGroup ent) {
		for (Cluster cl : allCluster) {
			if (cl.getGroups().contains(ent)) {
				return cl;
			}
		}
		return null;
	}

	public void addLine(Line line) {
		allLines.add(line);
		if (first(line)) {
			if (line.hasNoteLabelText()) {
				// lines0.add(0, line);
				for (int i = 0; i < lines0.size(); i++) {
					final Line other = lines0.get(i);
					if (other.hasNoteLabelText() == false && line.sameConnections(other)) {
						lines0.add(i, line);
						return;
					}
				}
				lines0.add(line);
			} else {
				lines0.add(line);
			}
		} else {
			lines1.add(line);
		}
	}

	private static boolean first(Line line) {
		final int length = line.getLength();
		if (length == 1) {
			return true;
		}
		return false;
	}

	public void addCluster(Cluster current) {
		allCluster.add(current);
	}

	public Node getNode(IEntity ent) {
		return nodeMap.get(ent);
	}

	public String getNodeUid(ILeaf ent) {
		final Node result = getNode(ent);
		if (result != null) {
			String uid = result.getUid();
			if (result.isShielded()) {
				uid = uid + ":h";
			}
			return uid;
		}
		assert result == null;
		if (ent.isGroup()) {
			for (IEntity i : nodeMap.keySet()) {
				if (ent.getCodeGetName().equals(i.getCodeGetName())) {
					return getNode(i).getUid();
				}
			}
			return Cluster.getSpecialPointId(ent);
		}
		throw new IllegalStateException();
	}

	public String getWarningOrError(int warningOrError) {
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<ILeaf, Node> ent : nodeMap.entrySet()) {
			final Node sh = ent.getValue();
			final double maxX = sh.getMinX() + sh.getWidth();
			if (maxX > warningOrError) {
				final IEntity entity = ent.getKey();
				sb.append(entity.getCodeGetName() + " is overpassing the width limit.");
				sb.append("\n");
			}

		}
		return sb.length() == 0 ? "" : sb.toString();
	}

	public Map<String, Double> getMaxX() {
		final Map<String, Double> result = new HashMap<String, Double>();
		for (Map.Entry<ILeaf, Node> ent : nodeMap.entrySet()) {
			final Node sh = ent.getValue();
			final double maxX = sh.getMinX() + sh.getWidth();
			final IEntity entity = ent.getKey();
			result.put(entity.getCodeGetName(), maxX);
		}
		return Collections.unmodifiableMap(result);
	}

	public List<Line> allLines() {
		return Collections.unmodifiableList(allLines);
	}

	public List<Line> lines0() {
		return Collections.unmodifiableList(lines0);
	}

	public List<Line> lines1() {
		return Collections.unmodifiableList(lines1);
	}

	public List<Cluster> allCluster() {
		return Collections.unmodifiableList(allCluster);
	}

	public Collection<Node> allNodes() {
		return Collections.unmodifiableCollection(nodeMap.values());
	}

	public List<Line> getAllLineConnectedTo(IEntity leaf) {
		final List<Line> result = new ArrayList<Line>();
		for (Line line : allLines) {
			if (line.isLinkFromOrTo(leaf)) {
				result.add(line);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public Line getLine(Link link) {
		for (Line line : allLines) {
			if (line.isLink(link)) {
				return line;
			}
		}
		throw new IllegalArgumentException();
	}

	public IEntity getOnlyOther(IEntity entity) {
		for (Line line : allLines) {
			final IEntity other = line.getOther(entity);
			if (other != null) {
				return other;
			}
		}
		return null;
	}

	public ILeaf getLeaf(Node node) {
		for (Map.Entry<ILeaf, Node> ent : nodeMap.entrySet()) {
			if (ent.getValue() == node) {
				return ent.getKey();
			}
		}
		throw new IllegalArgumentException();
	}
}
