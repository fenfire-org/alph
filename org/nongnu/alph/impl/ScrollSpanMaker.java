/*
ScrollSpanMaker.java
 *
 *    Copyright (c) 2002, Ted Nelson and Tuomas Lukka
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

/** A SpanMaker that makes real spans in TransientTextScrolls.
 *  Hm. This could be generalized by creating a factory for text scoll
 *  blocks... but this is a factory that uses another factory already,
 *  so let's not do a third kind of factory in here for now.
 */
public class ScrollSpanMaker implements SpanMaker {
    TextScrollBlock scrollBlock;

    /*
    public ScrollSpanMaker(TextScrollBlock scrollBlock)
	    throws ImmutableException {
	if(scrollBlock.isFinalized())
	    throw new ImmutableException(
		"Can't make spans into immutable scrollblock");
	this.scrollBlock = scrollBlock;
    }
    */

    public ScrollSpanMaker() {
        scrollBlock = new TransientTextScroll();
    }

    public TextSpan makeTextSpan(char c) {
        try {
	    return scrollBlock.append(c);
	} catch(ImmutableException e) {
            try {
                scrollBlock = new TransientTextScroll();
	        return scrollBlock.append(c);
	    } catch(ImmutableException f) {
	        throw new Error("The scrollblock went immutable on us!");
	    }
	}
    }

    public TextSpan makeTextSpan(String s) {
        try {
	    return scrollBlock.append(s);
	} catch(ImmutableException e) {
            try {
                scrollBlock = new TransientTextScroll();
	        return scrollBlock.append(s);
	    } catch(ImmutableException f) {
	        throw new Error("The scrollblock went immutable on us!");
	    }
	}
    }
}

