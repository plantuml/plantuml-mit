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
package net.sourceforge.plantuml.donors;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6xKB02mFk3cOSJIBSEBLlhdPHgj_M8aA4IpF4uh3m2AUxWR47-3UwN6_aVuYkjAzRveHRVbhZfzd3DL0"
			+ "qEFnwshOvdzxy6_EaXV1DrE22qW1-V5gD_8XxyXnUgJAa1B4Kj7zbudRqwtZGcMSg4Oe1ufR6zvtnkRP"
			+ "QNpnwj44RGH-3Eoh_xjqbNLvLHLceBiLnc3KQOJglAKOUofmQ_uVTOSwec0dAD3JZ-bKpdKuL549hpV6"
			+ "8irXp-vnTfsMLCNalo8bMHif9Ct13BqvwVsKVkYZzI_j0ZgqLjSGpnbGF6PAZwtRdexkEnX-dM97Y8LO"
			+ "i7dW3gKSLISeSMwwSHCJGKBtBhAnZKVUUHA0eHPMw9i_D9CZlOT4TXbItCJLG47IsnWWZKQzbd1szcr0"
			+ "3JtOObI3K5qPD-w1wx0yPUQNXeV08iw3Ma0BGVu7E-4p-ifB9kVS5B52tgPuTjk6-PBu26rJqRVqQ0g4"
			+ "myv3CrzS5ShMGnf7NiVygwD3tZLavTLlHX_5F8AvlCobu7vfWVeHwA4r4Ic-Ldcpa8Hm7QaNlUlSSQLc"
			+ "4KxfKAbOfrzYoslc7xhIdSsAvkvWKRV-dojTYM0FNRbKGszeDQHPvnCbgQ7-ViYhI-Uxu9e6jNidQ7sc"
			+ "pKNsVzSjpGpb09rTOXPqGo1GhgRLIB6120uBNHbo5w0DA9ycj-9f-H1xv0c2jv0PT8KDIH_4yo3b8HSX"
			+ "p42xxanH0SoVCkh9JS8D7bfi2BeTLGhfvUxw6IOjsvQDkMNI1ikT3cCKntHIyyJzCXTuX663MJIRkxLj"
			+ "x19SPtUQJzrb9g_YsTd2Uz55QF-GWJZ3d16HojCrzptj4lTGHs0MTy5JNMmljuC_3psmu1IBLZ6ZpVUG"
			+ "6T2uq8oQTelIXmR3trd86hRkAEeTfDQ4dH1cHXOyNvhSqf9BtCHiPpbXlW1KMujFxCe8PmdHiDnaZgub"
			+ "J8vOFGaPyZlL9x1KLGjBBtuCTmsUKJqAyd3U8wjkzMrAyNS1jSOqGK5M26k654NOMY03rqfwLle0kv_Q"
			+ "iU1iyvCTQmx5jqsqqxX1mnIORYAyaI0IrOQqH2yv9GhutXzFgQVcElZuxfvMZ8FKGX83LQZTEJyOFKtK"
			+ "RMeQwpNwlwsrbsNdpx_NaKrEVNxlgmcYvo0r_lqKeAESn0pU1hw6V2fvMs4bAuo335UkLteMA2muGE-y"
			+ "H3rJPdJkHB7SiboqYk-7QlZkMoK6piQ65HU55CNiIuoqzb8Fgepj1xvOB79G_9-eCY-JgwTcVHa9cq4M"
			+ "vsxiDbCnjzKQy61rJ2PmzzHdfsiyPSozXgeqN0No_HuDIKOFBMofQSiy4TYXpkkzfMr8PFJVO4I_ZDRc"
			+ "yQiUHWxI--aYqF3j3J8kc0c6YvG31Nfppea5tNnZ8I-gXHfOsZN3BXgyqD_iZuX2mZ7R-42IsrmiTtiX"
			+ "X-uX--Xgb9EDg2OiqC9Ygvr9B5Gv7gX82kRCbeayQWTjm3Mz0-WK1UV_SGV2cOdc5sHMcMTl97DvvtwS"
			+ "GMJ3-HaaTatPx6ZJkzLMv6O97W00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, getMetadata(),
				null, 1.0, HColorUtils.WHITE);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	private UDrawable getGraphicStrings() throws IOException {
		final List<TextBlock> cols = getCols(getDonors(), COLS, FREE_LINES);
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				final TextBlockBackcolored header = GraphicStrings
						.createBlackOnWhite(Arrays.asList("<b>Special thanks to our sponsors and donors !"));
				header.drawU(ug);
				final StringBounder stringBounder = ug.getStringBounder();
				ug = ug.apply(UTranslate.dy(header.calculateDimension(stringBounder).getHeight()));
				double x = 0;
				double lastX = 0;
				double y = 0;
				for (TextBlock tb : cols) {
					final Dimension2D dim = tb.calculateDimension(stringBounder);
					tb.drawU(ug.apply(UTranslate.dx(x)));
					lastX = x;
					x += dim.getWidth() + 10;
					y = Math.max(y, dim.getHeight());
				}
				final UImage logo = new UImage(
						new PixelImage(PSystemVersion.getPlantumlImage(), AffineTransformType.TYPE_BILINEAR));
				ug.apply(new UTranslate(lastX, y - logo.getHeight())).draw(logo);
			}
		};
	}

	public static List<TextBlock> getCols(List<String> lines, final int nbCol, final int reserved) throws IOException {
		final List<TextBlock> result = new ArrayList<TextBlock>();
		final int maxLine = (lines.size() + (nbCol - 1) + reserved) / nbCol;
		for (int i = 0; i < lines.size(); i += maxLine) {
			final List<String> current = lines.subList(i, Math.min(lines.size(), i + maxLine));
			result.add(GraphicStrings.createBlackOnWhite(current));
		}
		return result;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<String>();
		final Transcoder t = TranscoderImpl.utf8(new AsciiEncoder(), new StringCompressorNone(),
				new CompressionBrotli());
		try {
			final String s = t.decode(DONORS).replace('*', '.');
			final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
			while (st.hasMoreTokens()) {
				lines.add(st.nextToken());
			}
		} catch (NoPlantumlCompressionException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Donors)");
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
