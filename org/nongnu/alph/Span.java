/*   
Span.java
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

/** An immutable fluid media span; referential or not, depending on 
 * subclasses.
 * This is abstract since we have different kinds of spans for
 * text, images and video, with different operations.
 * <p>
 * Spans are immutable, just like Strings: all the verb-like methods
 * such as join and split return new Span objects.
 * <p>
 * Spans are always of some media type, like TextSpan or ImageSpan.
 * <p>
 * Alph supports true referential span using scrollblocks,
 * and transcludable, but not referential,
 * Random-Id, Content-Carrying (RICC) spans,
 * and non-transcludable, Fake spans.
 * For true referential spans, the getScrollBlock() method
 * returns the scrollblock and getScrollId() returns
 * its id. For RICC spans, getScrollBlock() returns null and
 * getScrollId() returns the id of the RICC span. For Fake spans,
 * getScrollBlock() returns null and getScrollId() return 
 * an empty string.
 * <p>
 * Alph Lite only supports RICC and Fake text spans,
 * so the getScrollBlock() method is irrelevant.
 *
 *
 *
 * @see Span
 * @see Span1D
 * @see TextSpan
 * @see ImageSpan
 * @see ScrollBlock
 */

public interface Span {

    /** Get a string representation of this span.
     * For text spans, this representation is <em>not</em>
     * the text inside but also gives the type and, in fact,
     * complete information to reconstruct the span object.
     */
    String toString();

    /** Whether this span and the given span intersect.
     */
    boolean intersects(Span s);

    /** (Irrelevant for Alph Lite) 
     * Get the ScrollBlock that this span points to.
     * May return null for URN5TextSpans - if getting
     * the scrollblock for the identifier, use getScrollId().
     */
    ScrollBlock getScrollBlock();

    /** Get the scrollblock Id.
     * Never null; for fake spans, an empty string.
     */
    String getScrollId();

}

