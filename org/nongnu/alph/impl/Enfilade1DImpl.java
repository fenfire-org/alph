/*
Enfilade1DImpl.java
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

package org.nongnu.alph.impl;
import org.nongnu.alph.*;
import java.util.*;

/** A simple tree implementation of enfilades.
 */
public class Enfilade1DImpl implements Enfilade1D {

    /** The char used to represent non-textual spans.
     */
    char NON_TEXT_SPAN_CHAR = '?';

    /** An empty enfilade.
     */
    private static Enfilade1DImpl empty = new Enfilade1DImpl(null, null, null);

    static public class Enfilade1DImplMaker implements Enfilade1D.Maker {
	public Enfilade1D makeEnfilade(Span span) {
	    return new Enfilade1DImpl(span);
	}
	/**
	 * @param spans A list of spans or Enfilade1D objects
	 * 	of the same type as this.
	 */
	public Enfilade1D makeEnfilade(List spans) {
	    return new Enfilade1DImpl(spans, 0, spans.size());
	}
	public Enfilade1D makeEnfilade() {
	    return empty;
	}
    }

    public static Maker theMaker = new Enfilade1DImplMaker();
    public Maker getMaker() { return theMaker; }

    private final int l;
    private final Enfilade1DImpl left;
    private final Span middle;
    private final Enfilade1DImpl right;

    /** Return the length of the span, as calculated for Enfilade1D.
     */
    private int spanLength(Object s) {
	if(s == null)
	    return 0;
	if(s instanceof Span1D) {
	    return ((Span1D)s).length();
	}
	if(s instanceof Enfilade1D) {
	    return ((Enfilade1D)s).length();
	}
	return 1;
    }

    private Span spanSub(Span s, int start) {
	return spanSub(s, start, spanLength(s));
    }
    private Span spanSub(Span s, int start, int end) {
	if(s instanceof Span1D)
	    return ((Span1D)s).subSpan(start, end);
	if(end==1 && start == 0)
	    return s;
	if(end-start > 0)
	    throw new Error("ARGH ARGH ARGH INCONSISTENT");
	return null;
    }

    /** For returning left or right: test whether the value
     * is null, and if it is, return an empty enfilade.
     */
    private Enfilade1DImpl retEnf(Enfilade1DImpl r) {
	if(r == null) return empty;
	return r;
    }


    public Enfilade1DImpl(Enfilade1DImpl left, Span middle,
			Enfilade1DImpl right) {
	this.left = left;
	this.middle = middle;
	this.right = right;
	int leftl = spanLength(left);
	int midl = spanLength(middle);
	int rightl = spanLength(right);
	this.l = leftl + midl + rightl;
    }

    public Enfilade1DImpl(Span span) {
	this(null, span, null);
    }

    public Enfilade1DImpl(List spans, int from, int n) {
	int hasSpan = n % 2;
	int subWidth = n / 2;

	// Handle the case where we got a ready-made
	// Enfilade.
        /* THIS DOES NOT WORK CORRECTLY!
	if(spans.get(from + subWidth) instanceof Enfilade1DImpl) {
	    if(n == 1) {
		this.left = (Enfilade1DImpl)spans.get(from+subWidth);
		this.middle = null; this.right = null;
		this.l = this.left.length();
		return;
	    } else
		hasSpan = 0;
	}
        */

	Enfilade1DImpl lt = null, rt = null;
	Span ml = null;

	if(subWidth > 0) {
	    lt = new Enfilade1DImpl(spans, from, subWidth);
	    rt = new Enfilade1DImpl(spans, from + subWidth + hasSpan,
	    				   subWidth);
	}
	if(hasSpan > 0)
	    ml = (Span)spans.get(from + subWidth);

	left = lt; middle = ml; right = rt;
	int leftl = spanLength(left);
	int midl = spanLength(middle);
	int rightl = spanLength(right);
	this.l = leftl + midl + rightl;
    }

    public int length() {
	return l;
    }

    public Enfilade1D sub(int o1) {
	int leftl = spanLength(left);
	if(o1 < leftl)
	    return new Enfilade1DImpl((Enfilade1DImpl)left.sub(o1), middle,
				      right);
	if(o1 == leftl)
	    return new Enfilade1DImpl(null, middle, right);
	int midl = spanLength(middle);
	if(o1 < leftl + midl)
	    return new Enfilade1DImpl(null, spanSub(middle, o1-leftl), right);
	if(o1 == leftl + midl)
	    return retEnf(right);
	int rightl = spanLength(right);
	if(o1 < leftl + midl + rightl)
	    return right.sub(o1 - leftl - midl);
	if(o1 == leftl + midl + rightl)
	    return new Enfilade1DImpl(null, null, null);
	throw new IndexOutOfBoundsException("Len "+leftl+" "+midl+" "+rightl+" : "+o1);
    }

    public Enfilade1D sub(int o1, int o2) {
	if(o1 == 0) {
	    int leftl = spanLength(left);
	    if(o2 < leftl)
		return left.sub(0, o2);
	    if(o2 == leftl)
		return retEnf(left);
	    int midl = spanLength(middle);
	    if(o2 < leftl+midl)
		return new Enfilade1DImpl(left, spanSub(middle, 0,o2-leftl), null);
	    if(o2 == leftl+midl)
		return new Enfilade1DImpl(left, middle, null);
	    int rightl = spanLength(right);
	    if(o2 < leftl+midl+rightl)
		return new Enfilade1DImpl(left,
				middle,
				(Enfilade1DImpl)right.sub(0,o2-leftl-midl));
	    if(o2 == leftl+midl+rightl)
		return this;
	    throw new IndexOutOfBoundsException("fromLen "+leftl+" "+midl+" "+rightl+" : "+o2);

	} else {
	    return sub(o1).sub(0, o2-o1);
	}
    }

    public Enfilade1D plus(Enfilade1D other) {
	return new Enfilade1DImpl(this, null, (Enfilade1DImpl)other);
    }

    public Enfilade1D plus(Span1D other) {
	return new Enfilade1DImpl(this, other, null);
    }

    public Enfilade1D prepended(Span1D other) {
	return new Enfilade1DImpl(null, other, this);
    }

    public String makeString() {
	char[] chars = new char[l];

	this.putIntoArray(chars, 0);
	return new String(chars);
    }

    public String toString() {
	return "Enf1DImpl["+left+","+middle+","+right+"]";
    }

    private void putIntoArray(char[] chars, int offset) {

	int leftl = spanLength(left);
	int midl = spanLength(middle);
	int rightl = spanLength(right);

	if(left != null) left.putIntoArray(chars, offset);
	if(middle != null) {
	    if(middle instanceof TextSpan) {
		String s = ((TextSpan)middle).getText();
		for(int i=0; i<midl; i++)
		    chars[offset + leftl + i] = s.charAt(i);
	    } else {
		for(int i=0; i<midl; i++)
		    chars[offset + leftl + i] = NON_TEXT_SPAN_CHAR;
	    }
	}
	if(right != null) {
	    right.putIntoArray(chars, offset + leftl + midl);
	}
    }

    public List getList() {
        // XXX iterator-based implementation
	//     (subclassing AbstractSequentialList)
	List l = new ArrayList();
	addToList(l);
	return l;
    }

    public void addToList(List l) {
        if(left != null)
	    ((Enfilade1DImpl)left).addToList(l);
	if(middle != null)
	    l.add(middle);
	if(right != null)
	    ((Enfilade1DImpl)right).addToList(l);
    }

    public boolean equals(Object o) {
        if(!(o instanceof Enfilade1D)) return false;
	Enfilade1D e = (Enfilade1D)o;
	return e.getList().equals(getList());
    }

    public int hashCode() {
        return getList().hashCode();
    }

    /** Return true if this enfilade intersects
     * (span-wise) the other one.
     * XXX Implemented using a really slow, bad algorithm.
     */
    public boolean intersects(Enfilade1D other0) {
	Enfilade1DImpl other = (Enfilade1DImpl)other0;
	if(middle != null && other.intersects(middle))
		return true;
	if(left != null && left.intersects(other))
		return true;
	if(right != null && right.intersects(other))
		return true;
	return false;
    }

    /** Return true if this enfilade intersects
     * (span-wise) the given span.
     * XXX Implemented using a really slow, bad algorithm.
     */
    public boolean intersects(Span s) {
	if(middle != null && middle.intersects(s))
		return true;
	if(left != null && left.intersects(s))
		return true;
	if(right != null && right.intersects(s))
		return true;
	return false;
    }

    public Enfilade1D optimized() {
        // brute force-- regenerate all...
	List l = getList();
        if(l.isEmpty()) return empty;

        ArrayList n = new ArrayList(l.size());
        Iterator i = l.iterator();

        Object last = i.next();
        while(i.hasNext()) {
            Object next = i.next();
	    if(next instanceof Span1D && ((Span1D)next).length() < 1)
		continue;

            if(next instanceof Span1D && last instanceof Span1D) {
                Span1D app = ((Span1D)last).append((Span1D)next);
                if(app != null) {
                    last = app; continue;
                }
            }
            n.add(last);
            last = next;
        }
        n.add(last);

	return new Enfilade1DImpl(n, 0, n.size());
    }
}

