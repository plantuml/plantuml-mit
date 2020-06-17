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
package net.sourceforge.plantuml;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.SkinParameter;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.Padder;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class SkinParamDelegator implements ISkinParam {

	final private ISkinParam skinParam;

	public SkinParamDelegator(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public HColor getHyperlinkColor() {
		return skinParam.getHyperlinkColor();
	}

	public HColor getBackgroundColor(boolean replaceTransparentByWhite) {
		return skinParam.getBackgroundColor(replaceTransparentByWhite);
	}

	public int getCircledCharacterRadius() {
		return skinParam.getCircledCharacterRadius();
	}

	public UFont getFont(Stereotype stereotype, boolean inPackageTitle, FontParam... fontParam) {
		return skinParam.getFont(stereotype, false, fontParam);
	}

	public HColor getFontHtmlColor(Stereotype stereotype, FontParam... param) {
		return skinParam.getFontHtmlColor(stereotype, param);
	}

	public HColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		return skinParam.getHtmlColor(param, stereotype, clickable);
	}

	public String getValue(String key) {
		return skinParam.getValue(key);
	}

	public int classAttributeIconSize() {
		return skinParam.classAttributeIconSize();
	}

	public int getDpi() {
		return skinParam.getDpi();
	}

	public DotSplines getDotSplines() {
		return skinParam.getDotSplines();
	}

	public HorizontalAlignment getHorizontalAlignment(AlignmentParam param, ArrowDirection arrowDirection,
			boolean isReverseDefine) {
		return skinParam.getHorizontalAlignment(param, arrowDirection, isReverseDefine);
	}

	public ColorMapper getColorMapper() {
		return skinParam.getColorMapper();
	}

	public boolean shadowing(Stereotype stereotype) {
		return skinParam.shadowing(stereotype);
	}

	public boolean shadowing2(Stereotype stereotype, SkinParameter skinParameter) {
		return skinParam.shadowing2(stereotype, skinParameter);
	}

	public PackageStyle packageStyle() {
		return skinParam.packageStyle();
	}

	public Sprite getSprite(String name) {
		return skinParam.getSprite(name);
	}

	public ComponentStyle componentStyle() {
		return skinParam.componentStyle();
	}

	public boolean stereotypePositionTop() {
		return skinParam.stereotypePositionTop();
	}

	public boolean useSwimlanes(UmlDiagramType type) {
		return skinParam.useSwimlanes(type);
	}

	public double getNodesep() {
		return skinParam.getNodesep();
	}

	public double getRanksep() {
		return skinParam.getRanksep();
	}

	public double getRoundCorner(CornerParam param, Stereotype stereotype) {
		return skinParam.getRoundCorner(param, stereotype);
	}

	public double getDiagonalCorner(CornerParam param, Stereotype stereotype) {
		return skinParam.getDiagonalCorner(param, stereotype);
	}

	public UStroke getThickness(LineParam param, Stereotype stereotype) {
		return skinParam.getThickness(param, stereotype);
	}

	public LineBreakStrategy maxMessageSize() {
		return skinParam.maxMessageSize();
	}

	public LineBreakStrategy wrapWidth() {
		return skinParam.wrapWidth();
	}

	public boolean strictUmlStyle() {
		return skinParam.strictUmlStyle();
	}

	public boolean forceSequenceParticipantUnderlined() {
		return skinParam.forceSequenceParticipantUnderlined();
	}

	public ConditionStyle getConditionStyle() {
		return skinParam.getConditionStyle();
	}

	public ConditionEndStyle getConditionEndStyle() {
		return skinParam.getConditionEndStyle();
	}

	public double minClassWidth() {
		return skinParam.minClassWidth();
	}

	public boolean sameClassWidth() {
		return skinParam.sameClassWidth();
	}

	public Rankdir getRankdir() {
		return skinParam.getRankdir();
	}

	public boolean useOctagonForActivity(Stereotype stereotype) {
		return skinParam.useOctagonForActivity(stereotype);
	}

	public HColorSet getIHtmlColorSet() {
		return skinParam.getIHtmlColorSet();
	}

	public boolean useUnderlineForHyperlink() {
		return skinParam.useUnderlineForHyperlink();
	}

	public HorizontalAlignment getDefaultTextAlignment(HorizontalAlignment defaultValue) {
		return skinParam.getDefaultTextAlignment(defaultValue);
	}

	public double getPadding() {
		return skinParam.getPadding();
	}

	public int groupInheritance() {
		return skinParam.groupInheritance();
	}

	public Guillemet guillemet() {
		return skinParam.guillemet();
	}

	public boolean handwritten() {
		return skinParam.handwritten();
	}

	public String getSvgLinkTarget() {
		return skinParam.getSvgLinkTarget();
	}

	public String getPreserveAspectRatio() {
		return skinParam.getPreserveAspectRatio();
	}

	public String getMonospacedFamily() {
		return skinParam.getMonospacedFamily();
	}

	public Colors getColors(ColorParam param, Stereotype stereotype) {
		return skinParam.getColors(param, stereotype);
	}

	public int getTabSize() {
		return skinParam.getTabSize();
	}

	public boolean shadowingForNote(Stereotype stereotype) {
		return shadowingForNote(stereotype);
	}

	public int maxAsciiMessageLength() {
		return skinParam.maxAsciiMessageLength();
	}

	public int colorArrowSeparationSpace() {
		return skinParam.colorArrowSeparationSpace();
	}

	public SplitParam getSplitParam() {
		return skinParam.getSplitParam();
	}

	public int swimlaneWidth() {
		return skinParam.swimlaneWidth();
	}

	public UmlDiagramType getUmlDiagramType() {
		return skinParam.getUmlDiagramType();
	}

	public HColor hoverPathColor() {
		return skinParam.hoverPathColor();
	}

	public double getPadding(PaddingParam param) {
		return skinParam.getPadding(param);
	}

	public boolean useRankSame() {
		return skinParam.useRankSame();
	}

	public boolean displayGenericWithOldFashion() {
		return skinParam.displayGenericWithOldFashion();
	}

	public TikzFontDistortion getTikzFontDistortion() {
		return skinParam.getTikzFontDistortion();
	}

	public boolean responseMessageBelowArrow() {
		return skinParam.responseMessageBelowArrow();
	}

	public boolean svgDimensionStyle() {
		return skinParam.svgDimensionStyle();
	}

	public char getCircledCharacter(Stereotype stereotype) {
		return skinParam.getCircledCharacter(stereotype);
	}

	public LineBreakStrategy swimlaneWrapTitleWidth() {
		return skinParam.swimlaneWrapTitleWidth();
	}

	public boolean fixCircleLabelOverlapping() {
		return skinParam.fixCircleLabelOverlapping();
	}

	public void setUseVizJs(boolean useVizJs) {
		skinParam.setUseVizJs(useVizJs);
	}

	public boolean isUseVizJs() {
		return skinParam.isUseVizJs();
	}

	public void copyAllFrom(ISkinSimple other) {
		skinParam.copyAllFrom(other);
	}

	public Map<String, String> values() {
		return skinParam.values();
	}

	public HorizontalAlignment getStereotypeAlignment() {
		return skinParam.getStereotypeAlignment();
	}

	public Padder sequenceDiagramPadder() {
		return skinParam.sequenceDiagramPadder();
	}

	public StyleBuilder getCurrentStyleBuilder() {
		return skinParam.getCurrentStyleBuilder();
	}

	public void muteStyle(Style modifiedStyle) {
		skinParam.muteStyle(modifiedStyle);
	}

	public Collection<String> getAllSpriteNames() {
		return skinParam.getAllSpriteNames();
	}

	public String getDefaultSkin() {
		return skinParam.getDefaultSkin();
	}

	public void setDefaultSkin(String newFileName) {
		skinParam.setDefaultSkin(newFileName);
	}

	public ActorStyle actorStyle() {
		return skinParam.actorStyle();
	}

}
