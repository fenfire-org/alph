/*
PageScrollBlock.java
 *    
 *    Copyright (c) 2003, Tuomas J. Lukka
 *    This file is part of Alph.
 *    
 *    Alph is free software; you can redistribute it and/or modify it under
 *    the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *    
 *    Alph is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 *    Public License for more details.
 *    
 *    You should have received a copy of the GNU General
 *    Public License along with Alph; if not, write to the Free
 *    Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *    MA  02111-1307  USA
 *    
 */
/*
 * Written by Tuomas J. Lukka
 */

package org.nongnu.alph;

/** A scrollblock storing pages, e.g.PDF or PS.
 * PageScrollblocks are the only type of scrollblock
 * that supports two different types
 * of spans. 
 * There are PageSpans, which are zero or more full pages,
 * and PageImageSpans which are rectangular regions inside a <em>single</em> page.
 * @see PageSpan
 * @see PageImageSpan
 */
public interface PageScrollBlock extends ScrollBlock{

    /** Get a PageSpan that represents the given range inside this scrollblock.
     * Equivalent to getCurrent().subSpan(offs1,offs2).
     *  @param offs1 The offset the span should start at.
     *  @param offs2 The one-past-end offset.
     */
    PageSpan getSpan(int offs1, int offs2);

    /** Get a PageImageSpan for the given page.
     */
    PageImageSpan getPage(int page);
}
