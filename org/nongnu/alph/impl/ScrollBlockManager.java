/*
ScrollBlockManager.java
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

package org.nongnu.alph.impl;
import org.nongnu.alph.*;
import org.nongnu.alph.util.*;
import org.nongnu.storm.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.util.*;
import java.io.*;

/** A central place which takes care of the ordering of scroll blocks.
 * At the moment, the ordering is transient.
 */

public class ScrollBlockManager {
    public static boolean dbg = false;
    final static void pa(String s) { System.out.println(s); }

    /** A scrollblock that is associated with a Mediaserver block.
     */
    public interface StormSaveableScrollBlock extends ScrollBlock {
        /** Get the Mediaserver id of this block; save when not saved yet. */
        BlockId saveOrGetId(StormPool ms) throws java.io.IOException;
    }

    /** Give an ordering of the given scrollblocks.
     * @return 0 if equal, -1 if s1 &lt; s2, 1 if s1 &gt; s2.
     */
    static public int compare(ScrollBlock s1, ScrollBlock s2) {
	return s1.getID().compareTo(s2.getID());
    }

    static int tmpid = 0;

    /** Create a new temporary ID for a scrollblock.
     */
    static public String getTmpID() {
	tmpid++;
	return "tmp(" + tmpid + ")";
    }

    static public class CannotLoadScrollBlockException extends IOException {
	CannotLoadScrollBlockException(String s) { super(s); }
	CannotLoadScrollBlockException() { super(); }
    }

    public static Span getSpan(StormPool ms, BlockId id,
			       int x, int y, int w, int h) {
	SimpleImageScroll block = null;
	try {
	    block = (SimpleImageScroll)getScrollBlock(ms, id);
	} catch (CannotLoadScrollBlockException _) {
	    throw new Error("Loading image failed: "+id);
	}
	if(block==null) {
//	    block = new SimpleImageScroll(ms, id);
	    msCache.put(id, block);
	}
	return block.getSpan(x, y, w, h);
    }

    public static Span getSpan(StormPool ms, BlockId id,
			       int poffs, int plen, int x, int y, int w, int h) {
    
        int p0 = poffs, p1 = poffs+plen;

	PageImageScroll block = null;

	try {
	    block = (PageImageScroll)getScrollBlock(ms, id);
	} catch (CannotLoadScrollBlockException _) {
	    throw new Error("Loading pageimage failed: "+id);
	}
	/*
	if(block==null) {
	    block = new PageImageScroll(ms, id);
	    msCache.put(id, block);
	}
	*/
//	return block.getSpan(p0, p1, x, y, w, h);
	return null;
    }

    /** We can use this if we know a specific scroll block is a text
     *  scroll block. (We do know because we handle text transclusions
     *  in a special way.)
     */
    static public TextScrollBlock getTextScrollBlock(StormPool ms,
						     BlockId id)
	throws CannotLoadScrollBlockException {
	if(id == null)
            throw new NullPointerException("cannot get block with id null");
	TextScrollBlock b = (TextScrollBlock)msCache.get(id);
	/*
	if(b == null) {
	    b = new PermanentTextScroll(ms, id);
	    msCache.put(id, b);
	}
	*/
	return b;
    }


    static HashMap msCache = new HashMap();
    /** Get a scrollblock from a mediaserver block.
     * The content-type of the block determines what to do.
     * XXX This is something of a problem: we need to be able
     * to have spans work even when
     * their targets are not currently accessible.
     * The type should therefore be gotten from somewhere...
     * <p>
     * Maybe the space storage formats should be extended to include
     * enough information on the scrollblocks:
     * type (image/text/page/audio/video), size in standard coordinates.
     * <p>
     * OTOH, we may be able to do this on the mediaserver side, propagating
     * the headers of blocks...
     * @return The scrollblock.
     */
    static public ScrollBlock getScrollBlock(StormPool ms,
					     BlockId id,
					     boolean lazy)
                        throws CannotLoadScrollBlockException {
	if(id == null)
	    throw new NullPointerException("cannot get block with id null");
	ScrollBlock b = (ScrollBlock)msCache.get(id);
	if(b == null) {
	    if(lazy)
		return null;
	    b = loadScrollBlock(ms, id);

	    msCache.put(id, b);
	}
	return b;
    }

    static public ScrollBlock getScrollBlock(StormPool ms,
					     BlockId id)
			throws CannotLoadScrollBlockException {
	return getScrollBlock(ms, id, false);
    }

    static private  ScrollBlock loadScrollBlock(StormPool ms,
						BlockId id)
		throws CannotLoadScrollBlockException {
	if(dbg) pa("Loading scroll block: "+id);

	Block block ;
        String ct;
	try {
	    block = ms.request(id);
	    if(block == null) return null;
            ct = id.getContentType();
	} catch(IOException e) {
	    throw new CannotLoadScrollBlockException("Couldn't load block");
	}
	int ind = ct.indexOf('/');
	if(ind < 0)
	 throw new CannotLoadScrollBlockException("Can't parse mediatype "+ ct);
	String type = ct.substring(0,ind);

	// Note: for the legacy string content to work, we need to be able
	// to load GZZ1 diffs as text blocks (see GZZ1Handler.LegacyContent
	// javadoc for more info).
	if(type.equals("text") || ct.equals("application/x-gzigzag-GZZ1")) {
	    if(!ct.equals("text/plain; charset=UTF-8") &&
	       !ct.equals("application/x-gzigzag-GZZ1"))
	     throw new CannotLoadScrollBlockException("Unknown text block '"+ct
			    +"'");
	    // return new PermanentTextScroll(ms, id);
	} else if(type.equals("image")) {
	    // return new SimpleImageScroll(ms, id);
	} else if(ct.equals("application/postscript") ||
		  ct.equals("application/pdf")) {
	    if(dbg) pa("Loaded page image scroll block.");
	    // return new PageImageScroll(ms, id);
	} else {

	    throw new CannotLoadScrollBlockException("Unknown mediatype "+ct);
	}
	return null;

    }

}


