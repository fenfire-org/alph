/*   
Span1D.java
 *    
 *    Copyright (c) 1999-2001, Ted Nelson and Tuomas Lukka
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

/** A 1-dimensional immutable span inside one document.
 * The different routines use "natural units" the definition
 * of which depends on the span type - see the appropriate subinterfaces.
 */

public interface Span1D extends Span {

    /** Get the offset of this span inside the scrollblock,
     *  in natural units starting at zero.
     */
    int offset();

    /** Get the length of this span, in natural units.
     */
    int length();

    /** Return the subSpan starting at o1 in natural units 
     * and ending just before o2,
     * analogous to java.lang.String.substring().
     */
    Span1D subSpan(int o1, int o2);

    /** Return the subSpan starting at o1 in natural units 
     * and ending at the end
     * of this span,
     * analogous to java.lang.String.substring().
     */
    Span1D subSpan(int o1);

    /** Return the span that results from appending the other span
     * to this span, <em>if</em> the resulting span is contiguous.
     * Otherwise, returns null.
     */
    Span1D append(Span s);

}


