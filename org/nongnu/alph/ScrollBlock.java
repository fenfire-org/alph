/*
ScrollBlock.java
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

/** A block from which typed spans may be taken.
 * A larger unit of fluid media.
 * @see Span
 * @see Span1D
 * @see TextSpan
 * @see ImageSpan
 */

public interface ScrollBlock {

    /** Get the content type of this scrollblock.
     */
    String getContentType();

    /** Get the data of the scrollblock in a tmp file.
     * May return null, if data not yet finalized.
     */
    BlockFile getBlockFile() throws java.io.IOException;

    /** Get the current contents of this block as a single span
     * of the appropriate type.
     */
    Span getCurrent();

    /** Whether this block has been finalized or whether more
     * can be appended.
     */
    boolean isMutable();

    /** Get the globally unique identifier of this block.
     * XXX Exact semantics, tmp ids
     */
    String getID();

    /** Get the globally unique identifier of this block.
     * XXX Exact semantics, tmp ids
     */
    Object getBlockId();

    /** Compare this scrollblock with the other one and return
     * -1, 0 or 1 accordingly.
     * All scrollblocks are well-ordered.
     * <p>
     * We currently define this to be
     * <pre>getID().compareTo(sb.getID()) </pre>
     * but this may change in the future.
     */
    /*public final int compareTo(ScrollBlock sb) {
	return getID().compareTo(sb.getID()) ;
    }*/

}

