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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockGeneric;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.HeaderLayout;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageClassHeader extends AbstractEntityImage {

	final private HeaderLayout headerLayout;

	public EntityImageClassHeader(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);

		final boolean italic = entity.getLeafType() == LeafType.ABSTRACT_CLASS
				|| entity.getLeafType() == LeafType.INTERFACE;

		final Stereotype stereotype = entity.getStereotype();
		final boolean displayGenericWithOldFashion = skinParam.displayGenericWithOldFashion();
		final String generic = displayGenericWithOldFashion ? null : entity.getGeneric();
		FontConfiguration fontConfigurationName;

		if (SkinParam.USE_STYLES()) {
			final Style style = FontParam.CLASS.getStyleDefinition(SName.classDiagram)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			fontConfigurationName = new FontConfiguration(style, skinParam, stereotype, FontParam.CLASS);
		} else {
			fontConfigurationName = new FontConfiguration(getSkinParam(), FontParam.CLASS, stereotype);
		}
		if (italic) {
			fontConfigurationName = fontConfigurationName.italic();
		}
		Display display = entity.getDisplay();
		if (displayGenericWithOldFashion && entity.getGeneric() != null) {
			display = display.addGeneric(entity.getGeneric());
		}
		TextBlock name = display.createWithNiceCreoleMode(fontConfigurationName, HorizontalAlignment.CENTER, skinParam);
		final VisibilityModifier modifier = entity.getVisibilityModifier();
		if (modifier == null) {
			name = TextBlockUtils.withMargin(name, 3, 3, 0, 0);
		} else {
			final Rose rose = new Rose();
			final HColor back = rose.getHtmlColor(skinParam, modifier.getBackground());
			final HColor fore = rose.getHtmlColor(skinParam, modifier.getForeground());

			final TextBlock uBlock = modifier.getUBlock(skinParam.classAttributeIconSize(), fore, back, false);
			name = TextBlockUtils.mergeLR(uBlock, name, VerticalAlignment.CENTER);
			name = TextBlockUtils.withMargin(name, 3, 3, 0, 0);
		}

		final TextBlock stereo;
		if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			stereo = null;
		} else {
			stereo = TextBlockUtils.withMargin(Display.create(stereotype.getLabels(skinParam.guillemet())).create(
					new FontConfiguration(getSkinParam(), FontParam.CLASS_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, skinParam), 1, 0);
		}

		TextBlock genericBlock;
		if (generic == null) {
			genericBlock = null;
		} else {
			genericBlock = Display.getWithNewlines(generic).create(
					new FontConfiguration(getSkinParam(), FontParam.CLASS_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, skinParam);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
			final HColor classBackground = SkinParamUtils.getColor(getSkinParam(), stereotype, ColorParam.background);

			final HColor classBorder = SkinParamUtils.getFontColor(getSkinParam(), FontParam.CLASS_STEREOTYPE,
					stereotype);
			genericBlock = new TextBlockGeneric(genericBlock, classBackground, classBorder);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
		}

		final TextBlock circledCharacter;
		if (portionShower.showPortion(EntityPortion.CIRCLED_CHARACTER, (ILeaf) getEntity())) {
			circledCharacter = TextBlockUtils.withMargin(getCircledCharacter(entity, skinParam), 4, 0, 5, 5);
		} else {
			circledCharacter = null;
		}
		this.headerLayout = new HeaderLayout(circledCharacter, stereo, name, genericBlock);
	}

	private TextBlock getCircledCharacter(ILeaf entity, ISkinParam skinParam) {
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null && stereotype.getSprite(skinParam) != null) {
			return stereotype.getSprite(skinParam);
		}
		final UFont font = SkinParamUtils.getFont(getSkinParam(), FontParam.CIRCLED_CHARACTER, null);
		final HColor classBorder = SkinParamUtils.getColor(getSkinParam(), stereotype, ColorParam.classBorder);
		final HColor fontColor = SkinParamUtils.getFontColor(getSkinParam(), FontParam.CIRCLED_CHARACTER, null);
		if (stereotype != null && stereotype.getCharacter() != 0) {
			return new CircledCharacter(stereotype.getCharacter(), getSkinParam().getCircledCharacterRadius(), font,
					stereotype.getHtmlColor(), classBorder, fontColor);
		}
		final LeafType leafType = entity.getLeafType();
		final HColor spotBackColor = SkinParamUtils.getColor(getSkinParam(), stereotype, spotBackground(leafType));
		HColor spotBorder = SkinParamUtils.getColor(getSkinParam(), stereotype, spotBorder(leafType));
		if (spotBorder == null) {
			spotBorder = classBorder;
		}
		char circledChar = 0;
		if (stereotype != null) {
			circledChar = getSkinParam().getCircledCharacter(stereotype);
		}
		if (circledChar == 0) {
			circledChar = getCircledChar(leafType);
		}
		return new CircledCharacter(circledChar, getSkinParam().getCircledCharacterRadius(), font, spotBackColor,
				spotBorder, fontColor);
	}

	private ColorParam spotBackground(LeafType leafType) {
		switch (leafType) {
		case ANNOTATION:
			return ColorParam.stereotypeNBackground;
		case ABSTRACT_CLASS:
			return ColorParam.stereotypeABackground;
		case CLASS:
			return ColorParam.stereotypeCBackground;
		case INTERFACE:
			return ColorParam.stereotypeIBackground;
		case ENUM:
			return ColorParam.stereotypeEBackground;
		case ENTITY:
			return ColorParam.stereotypeCBackground;
		}
		assert false;
		return null;
	}

	private ColorParam spotBorder(LeafType leafType) {
		switch (leafType) {
		case ANNOTATION:
			return ColorParam.stereotypeNBorder;
		case ABSTRACT_CLASS:
			return ColorParam.stereotypeABorder;
		case CLASS:
			return ColorParam.stereotypeCBorder;
		case INTERFACE:
			return ColorParam.stereotypeIBorder;
		case ENUM:
			return ColorParam.stereotypeEBorder;
		case ENTITY:
			return ColorParam.stereotypeCBorder;
		}
		assert false;
		return null;
	}

	private char getCircledChar(LeafType leafType) {
		switch (leafType) {
		case ANNOTATION:
			return '@';
		case ABSTRACT_CLASS:
			return 'A';
		case CLASS:
			return 'C';
		case INTERFACE:
			return 'I';
		case ENUM:
			return 'E';
		case ENTITY:
			return 'E';
		}
		assert false;
		return '?';
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return headerLayout.getDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double width, double height) {
		headerLayout.drawU(ug, width, height);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

}
