/*   
PageImageSpan.java
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

/** An area of a page inside a PageScrollBlock.
 *  This represents part of a page in a ps or pdf file,
 *  as an image. The scroll block of this kind of span
 *  is a <code>PageScrollBlock</code>.
 */
public interface PageImageSpan extends ImageSpan {

    /** Get the index of the page inside the PageScrollBlock.
     */
    int getPageIndex();
}

