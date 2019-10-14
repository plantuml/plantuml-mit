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
package net.sourceforge.plantuml.core;

// Remove CmapData and Dimension2D
// Merge CucaDiagramFileMakerResult
/**
 * Information about a generated image for a diagram.
 * For some diagrams, there are some position information about elements
 * from the diagram. In that case, the method <code>containsCMapData()</code> returns
 * <code>true</code> and you can retrieve those information using
 * <code>getCMapData()</code> method.
 * 
 * @author Arnaud Roques
 * 
 */
public interface ImageData {

	/**
	 * Width in pixel of the image.
	 * @return
	 */
	public int getWidth();

	/**
	 * Height in pixel of the image.
	 * @return
	 */
	public int getHeight();

	/**
	 * Indicates if the image has some position information.
	 * @return <code>true</code> if the image has position information.
	 */
	public boolean containsCMapData();

	/**
	 * Return position information as a CMap formated string.
	 * For example, if you call this method with <code>nameId</code>
	 * set to "foo_map", you will get something like:
	 * 
	 * <code><pre>
	 * &lt;map id="foo_map" name="foo_map"&gt;
	 * &lt;area shape="rect" id="..." href="..." title="..." alt="" coords="64,68,93,148"/&gt;
	 * &lt;/map&gt;
	 * </pre></code>
	 * 
	 * @param nameId thie id to be used in the cmap data string.
	 * @return
	 */
	public String getCMapData(String nameId);
	
	public String getWarningOrError();
	
	public int getStatus();


}
