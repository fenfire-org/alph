/*
TextScrollBlock.java
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

/** A text scroll block.
 */

public interface TextScrollBlock extends ScrollBlock {

    TextSpan append(char ch) throws ImmutableException;
    TextSpan append(String s) throws ImmutableException;

    /** Get the internal character array of this scrollblock.
     * This is used for speeding up drawing strings from a TextScrollBlock
     * by being able to give a scrollblock and two offsets to the drawing
     * routines.
     * <p>
     * <b><big>THE RETURNED CHARACTER ARRAY MUST NOT BE MODIFIED UNDER
     *   <font size="+3">ANY</font> CIRCUMSTANCES.
     * </b>
     */
    char[] getCharArray();

    /** Get a span that represents the given range inside this scrollblock.
     * Equivalent to getCurrent().subSpan(offs1,offs2).
     *  @param offs1 The offset the span should start at.
     *  @param offs2 The one-past-end offset.
     */
    Span getSpan(int offs1, int offs2);
}

