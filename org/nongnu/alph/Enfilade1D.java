/*
Enfilade1D.java
 *
 *    Copyright (c) 2002 Ted Nelson and Tuomas Lukka
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
import java.util.List;

/** A sequence of 1D spans (assumed to be the same type, but not enforced) that
 * can be operated on as a string for cuttings.
 * <p>
 * 2D (image) or 3D (video/page) spans can be included in the streams:
 * those which do not extend Span1D
 * they are interpreted as single characters by this class, but those
 * that do are interpreted as text of their length. In the text
 * string, they are represented by the character (XXX? Not sure yet).
 * The Iterator returned by the iterator() method will iterate over
 * those spans normally.
 * <p>
 * NOTE: THIS IS AN <b>IMMUTABLE</b> CLASS: NEW OBJECTS ARE CREATED BY THE
 OPERATIONS.
 * A tree implementation should be able to share nodes and thus perform
 operations
 * with log(L) efficiency.
 */
public interface Enfilade1D extends java.io.Serializable {

    static public interface Maker {
	Enfilade1D makeEnfilade(Span span);
	Enfilade1D makeEnfilade(List spans);
	Enfilade1D makeEnfilade();
    }

    Maker getMaker();

    /** Get the sum of the lengths. This <b>MUST</b> be the same
     * as the length of the string returned by makeString().
     */
    int length();

    Enfilade1D sub(int o1);
    Enfilade1D sub(int o1, int o2) ;

    /** Return an Enfilade1D object whose vstream is a concatenation
     * of this enfilade and the other one given.
     * Unless the enfilades were created by the same maker, this method
     * may throw a classcastexception.
     */
    Enfilade1D plus(Enfilade1D other);

    /** Return an Enfilade1D object whose vstream is a concatenation
     * of this enfilade and the span given.
     * Unless the enfilades were created by the same maker, this method
     * may throw a classcastexception.
     */
    Enfilade1D plus(Span1D other) ;

    /** Return an Enfilade1D object whose vstream is a concatenation of
     * the given span and this enfilade.
     * Unless the enfilades were created by the same maker, this method
     * may throw a classcastexception.
     */
    Enfilade1D prepended(Span1D other);

    /** Return a String object where offsets are the same as in this enfilade.
     * Note that having an audio span will put in a lot of dummy chars so don't
     * do that ;)
     * <p>
     * This is not toString() because that routine will give a string
     * which explicitly indicates that this is an enfilade, and also gives
     * the permanent ids of the spans therein.
     */
    String makeString() ;

    /** Get a List of the spans in this enfilade.
     */
    List getList() ;

    /** Return a possibly optimized version of this enfilade.
     * This could e.g. balance a tree. This function may be relatively
     * expensive in terms of time, and also may reduce the number of nodes
     * that can be shared between enfilades.
     * <p>
     * It is at its most useful for enfilades that have been created
     * by adding little by little to the end, and which are not shared.
     * Of course, most text entered in cells *is* just like that...
     * <p>
     * This method must at least join spans which are next to each other
     * and which are contiguous (the latter can be appended to the former).
     * This is necessary because coniguous spans MUST be appended before
     * saving, otherwise we'd generate lots of JUNK!
     * <p>
     * Invariants which implementations MUST obey: ( XXX too harsh? )
     *	<ul>
     *	<li> For any two enfilades with the same spans in the same
     *	     order (split or joined in any permissible way), this
     *	     method must return the same structure. This is so that
     *	     optimized() enfilades will have the same hash codes.
     *	<li> The method must be idempotent (which naturally follows
     *	     from the previous point).
     *	</ul>
     */
    Enfilade1D optimized() ;

}
