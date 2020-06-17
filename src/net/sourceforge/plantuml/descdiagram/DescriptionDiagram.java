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
package net.sourceforge.plantuml.descdiagram;

import net.sourceforge.plantuml.ComponentStyle;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;

public class DescriptionDiagram extends AbstractEntityDiagram {

	public DescriptionDiagram(ISkinSimple skinParam) {
		super(skinParam);
	}

	@Override
	public Ident cleanIdent(Ident ident) {
		String codeString = ident.getName();
		if (codeString.startsWith("[") && codeString.endsWith("]")) {
			return ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
		}
		if (codeString.startsWith(":") && codeString.endsWith(":")) {
			return ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
		}
		if (codeString.startsWith("()")) {
			codeString = StringUtils.trin(codeString.substring(2));
			codeString = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString);
			return ident.parent().add(Ident.empty().add(codeString, null));
		}
		return ident;
	}

	@Override
	public ILeaf getOrCreateLeaf(Ident ident, Code code, LeafType type, USymbol symbol) {
		checkNotNull(ident);
		if (type == null) {
			String codeString = code.getName();
			if (codeString.startsWith("[") && codeString.endsWith("]")) {
				final USymbol sym = getSkinParam().componentStyle().toUSymbol() ;
				final Ident idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
				return getOrCreateLeafDefault(idNewLong, idNewLong.toCode(this), LeafType.DESCRIPTION, sym);
			}
			if (codeString.startsWith(":") && codeString.endsWith(":")) {
				final Ident idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
				return getOrCreateLeafDefault(idNewLong, idNewLong.toCode(this), LeafType.DESCRIPTION,
						getSkinParam().actorStyle().toUSymbol());
			}
			if (codeString.startsWith("()")) {
				codeString = StringUtils.trin(codeString.substring(2));
				codeString = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeString);
				final Ident idNewLong = buildLeafIdent(codeString);
				final Code code99 = this.V1972() ? idNewLong : buildCode(codeString);
				return getOrCreateLeafDefault(idNewLong, code99, LeafType.DESCRIPTION, USymbol.INTERFACE);
			}
			final String tmp4 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code.getName(), "\"([:");
			final Ident idNewLong = ident.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
			code = this.V1972() ? idNewLong : buildCode(tmp4);
			return getOrCreateLeafDefault(idNewLong, code, LeafType.STILL_UNKNOWN, symbol);
		}
		return getOrCreateLeafDefault(ident, code, type, symbol);
	}

	private boolean isUsecase() {
		for (ILeaf leaf : getLeafsvalues()) {
			final LeafType type = leaf.getLeafType();
			final USymbol usymbol = leaf.getUSymbol();
			if (type == LeafType.USECASE || usymbol == getSkinParam().actorStyle().toUSymbol()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		final LeafType defaultType = isUsecase() ? LeafType.DESCRIPTION : LeafType.DESCRIPTION;
		final USymbol defaultSymbol = isUsecase() ? getSkinParam().actorStyle().toUSymbol() : USymbol.INTERFACE;
		for (ILeaf leaf : getLeafsvalues()) {
			if (leaf.getLeafType() == LeafType.STILL_UNKNOWN) {
				leaf.muteToType(defaultType, defaultSymbol);
			}
		}
	}

	@Override
	public String checkFinalError() {
		this.applySingleStrategy();
		return super.checkFinalError();
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.DESCRIPTION;
	}

}
