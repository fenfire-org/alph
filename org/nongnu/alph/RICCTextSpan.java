/*
RICCTextSpan.java
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
import org.nongnu.alph.*;

/** A Random-Id Content-Carrying (RICC)
 * text span.
 * A RICC span is a unit of transcludable, but not referential, media.
 * A single RICC span simply a triple (random-Id, offset, string)
 * that is compared for intersection by comparing the ids, offsets
 * and corresponding characters etc.
 */
public class RICCTextSpan implements TextSpan {
    /** The interned random id name string.
     */
    private String randomId;

    /** The fake offset within the randomId
     */ 
    private int offset;

    /** The contents.
     */
    private String str;

    /** Create a new RICC text span.
     * @param randomId The random Id of the virtual scrollblock
     * @param offset The offset within the virtual scrollblock
     * @param str The text string
     */
    public RICCTextSpan(String randomId, int offset, String str) {
	this.randomId = randomId.intern();
	this.offset = offset;
	this.str = str;
    }

    public String toString() {
	return "RICCSpan["+randomId+","+offset+",'"+str+"']";
    }

    public boolean intersects(Span s) {
	if(!(s instanceof RICCTextSpan)) return false;
	RICCTextSpan other = (RICCTextSpan)s;
	// Because randomId is interned, can do this
	if(other.randomId != randomId) return false;

	if(offset <= other.offset) {
	    int loc = other.offset - offset;
	    int tlen = str.length() - loc;
	    int olen = other.str.length();
	    int len;
	    if(tlen < olen) len = tlen; else len = olen;
	    if(len <= 0) return false;
	    for(int i=0; i<len; i++)
		if(str.charAt(i + loc) != other.str.charAt(i))
		    return false;
	    return true;
	} else {
	    return other.intersects(this);
	}

    }
    public ScrollBlock getScrollBlock() { return null; }

    public String getScrollId() { return randomId; }

    public int offset() {
	return offset;
    }

    public int length() {
	return str.length();
    }

    public Span1D subSpan(int o1, int o2) {
	return new RICCTextSpan(randomId, offset + o1, str.substring(o1,o2));

    }

    public Span1D subSpan(int o1) {
	return new RICCTextSpan(randomId, offset + o1, str.substring(o1));
    }

    public Span1D append(Span s) {
	if(!(s instanceof RICCTextSpan)) return null;
	RICCTextSpan other = (RICCTextSpan)s;
	if(other.randomId != randomId) return null;
	if(offset + str.length() != other.offset) return null;
	return new RICCTextSpan(randomId, offset, str + other.str);
    }

    public String getText() {
	return str;
    }

    public boolean equals(Object o) {
	if(!(o instanceof RICCTextSpan)) return false;
	RICCTextSpan other = (RICCTextSpan) o;
	return  randomId    == other.randomId &&
		offset  == other.offset &&
		str.equals(other.str);
    }

}
