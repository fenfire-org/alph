/*
FakeTextSpan.java
 *    
 *    Copyright (c) 2002, Ted Nelson and Tuomas Lukka
 *    Copyright (c) 2003, Tuomas Lukka
 *
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
 *
 */
/*
 * Written by Tuomas Lukka
 */

package org.nongnu.alph;
import org.nongnu.storm.util.*;

/** An object that behaves like a text span but is not stored in a real Storm block.
 * Used for things like coordinates or other computer-generated rapidly changing
 * content. Must be atomic: if you give one piece away, you give
 * the whole thing away. Must be short, because the whole thing
 * is repeated whereever one piece goes. See PEG miniblocks--benja.
 */

public class FakeTextSpan implements TextSpan, java.io.Serializable {

    String text;
    transient char[] textarr;

    public FakeTextSpan(String s) {
	this.text = s;
    }

    public String getText() { return text; }
    public int offset() { return 0; }
    public int length() { return text.length(); }
    public Span1D subSpan(int o1, int o2) { return new FakeTextSpan(text.substring(o1,o2)); }
    public Span1D subSpan(int o1) { return new FakeTextSpan(text.substring(o1)); }
    public Span1D append(Span s) {
	return null;
    }

    public boolean equals(Object o) {
        if(!(o instanceof FakeTextSpan)) return false;
	return ((FakeTextSpan)o).text.equals(text);
    }
    public int hashCode() {
        return 78 ^ text.hashCode();
    }

    public String toString() { return "FAKESPAN "+text; }
    public boolean intersects(Span s) { return false; }

    public String getScrollId() { return ""; }

    public ScrollBlock getScrollBlock() {
	return null;
    }
}
