/*   
ImageSpan.java
 *    
 *    Copyright (c) 2001, Ted Nelson and Tuomas Lukka
 *
 *    This file is part of Gzz.
 *    
 *    Gzz is free software; you can redistribute it and/or modify it under
 *    the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *    
 *    Gzz is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 *    Public License for more details.
 *    
 *    You should have received a copy of the GNU General
 *    Public License along with Gzz; if not, write to the Free
 *    Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *    MA  02111-1307  USA
 *    
 *
 */
/*
 * Written by Tuomas Lukka
 */


package org.nongnu.alph;
import java.awt.*;

/** An image span - contiguous piece of a permascroll -. 
 * Spans in images are rectangular regions.
 */
public interface ImageSpan extends Span {

    /** Get the location of this span in the mediaserver block.
     */
    Point getLocation();

    /** Get the size of the image in pixels, at the default resolution.
     */
    Dimension getSize();

    /** Get a subspan of the current span.
     * The coordinates are <b>relative to this span</b>.
     */
    ImageSpan subArea(int x0, int y0, int w, int h);

    /** Get the whole contiguous 2D image that this span is a part of 
     * (opposite of subImage).
     * For PageImageSpans, this would be the page the pageimagespan
     * is taken from (but only the one page), for parts
     * of png images it would be 
     * the whole image.
     * @see PageImageSpan
     */
    ImageSpan getSuperImageSpan();

}
