/*
AbstractImageSpan.java
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

import java.awt.Dimension;
import java.awt.Point;

/** A base class for image spans.
 */
abstract public class AbstractImageSpan extends AbstractSpan
    implements ImageSpan {
    protected final int x, y, w, h;
    AbstractImageSpan(ScrollBlock sb, int x, int y, int w, int h) {
	super(sb);
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
    }
    protected abstract AbstractImageSpan
	createNew(int x, int y, int w, int h);

    public int hashCode() {
	return (x*13) ^ (y*15) ^ (w*17) ^ (h*19) ^ scrollBlock.hashCode();
    }

    public Dimension getSize() { return new Dimension(w, h); }
    public Point getLocation() { return new Point(x, y); }
    public ImageSpan subArea(int x0, int y0, int w0, int h0) {
	if(x0 < 0 || y0 < 0)
	    throw new Error("Negative coordinates");
	if(x0 + w0 > w || y0 + h0 > h)
	    throw new Error("Too large imagearea("+x0+","+w0+","+w+" : "+
			    y0+","+h0+","+h+")");
	return createNew(x+x0, y+y0, w0, h0);
    }

    private AbstractImageSpan isSame(Object s) {
	if(!(s instanceof AbstractImageSpan)) return null;
	AbstractImageSpan t = (AbstractImageSpan)s;
	if(!this.getScrollBlock().equals(t.getScrollBlock())) return null;
	return t;
    }

    public boolean intersects(Span s) {
	AbstractImageSpan t = isSame(s);
	if(t==null) return false;
	return x < t.x + t.w && x + w > t.x &&
	       y < t.y + t.h && y + h > t.y;
    }

    public boolean equals(Object o) {
	AbstractImageSpan s = isSame(o);
	if(s == null) return false;
	return s.scrollBlock == scrollBlock &&
	       s.x == x &&
	       s.y == y &&
	       s.w == w &&
	       s.h == h;
    }

    public String toString() {
	return "["+super.toString()+": "+scrollBlock.getID()+" @ "+
		x+", "+y+", "+w+", "+h+"]";
    }
}

