/*
AbstractSpan1D.java
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

package org.nongnu.alph.impl;
import org.nongnu.alph.*;
import org.nongnu.alph.util.*;
import org.nongnu.storm.*;

/** An abstract base class for 1D spans.
 */
abstract class AbstractSpan1D extends AbstractSpan implements Span1D {
    public static boolean dbg = false;
    final static void pa(String s) { System.out.println(s); }

    protected final int offs0, offs1;

    public int hashCode() {
	return (offs0*17) ^ (offs1*13) ^ scrollBlock.hashCode();
    }

    /** Create a new 1-D span, which starts at offs0 and
     * ends just before offs1 - analogous to String.substring.
     */
    protected AbstractSpan1D(ScrollBlock scrollBlock, int offs0, int offs1) {
	super(scrollBlock);
	this.offs0 = offs0;
	this.offs1 = offs1;
    }

    public int offset() { return offs0; }

    public int length() { return offs1-offs0; }

    public Span1D subSpan(int o1, int o2) {
	if (dbg) pa("o1:"+o1+", o2:"+o2+", length"+length() +" - "+toString());
	if(o1 < 0 ||
	   o2 > length() ||
	   o1 > o2)
	    throw new IndexOutOfBoundsException();
	return createNew(offs0+o1, offs0+o2);
    }

    protected abstract AbstractSpan1D createNew(int offs0, int offs1);

    public Span1D subSpan(int o1) {
	return subSpan(o1, length());
    }

    public Span1D append(Span s) {
	if(!(s instanceof AbstractSpan1D)) return null;
	AbstractSpan1D sts = (AbstractSpan1D)s;
	if(sts.getScrollBlock() != this.getScrollBlock()) return null;
	if(offs1 == sts.offs0)
	    return createNew(offs0, sts.offs1);
	return null;
    }

    private AbstractSpan1D isSame(Span s) {
	if(!(s instanceof AbstractSpan1D)) return null;
	AbstractSpan1D t = (AbstractSpan1D)s;
	if(!this.getScrollBlock().equals(t.getScrollBlock())) return null;
	return t;
    }

    public boolean intersects(Span s) {
	AbstractSpan1D t = isSame(s);
	if(t == null) return false;
	return (offs0 < t.offs1) && (offs1 > t.offs0);
    }

    public boolean equals(Object o) {
	if(!(o instanceof AbstractSpan1D)) return false;
	AbstractSpan1D s = (AbstractSpan1D)o;
	return (s.getScrollBlock().equals(this.getScrollBlock())
		    && s.offs0 == offs0 && s.offs1 == offs1);
    }

    public String toString() {
	return "SPAN1D("+scrollBlock+" "+offs0+" "+offs1+")";
    }

}


