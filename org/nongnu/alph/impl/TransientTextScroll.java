/*
TransientTextScroll.java
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
import org.nongnu.storm.*;

/** A text scroll block.
 */

public class TransientTextScroll extends AbstractScrollBlock
	implements TextScrollBlock, ScrollBlockManager.StormSaveableScrollBlock {

    public TransientTextScroll() {
	super(null, null, AbstractScrollBlock.plainText);
    }

    boolean finalized = false;
    BlockId mediaserverId = null;

    final StringBuffer current = new StringBuffer();
    char[] curchars;

    /***** Do compare by identity for now!
    public boolean equals(Object o) {
        if(!(o instanceof ScrollBlock)) return false;
        if(this == o) return true;
	if(mediaserverId == null) return false;
	ScrollBlock sb = (ScrollBlock)o;
	return sb.getID().equals(getID());
    }
    public int hashCode() {
        if(mediaserverId == null)
	    throw new Error("Not finalized-- cannot hash yet");
	return getID().hashCode();
    }
    public String toString() {
        return "TransientTextScroll<"+getID()+">";
    }
    *****/

    public TextSpan append(char ch) throws ImmutableException {
	if(finalized)
	    throw new ImmutableException("Already saved; can't append");
	current.append(ch);
	return new StdTextSpan(this, current.length()-1, current.length());
    }

    public TextSpan append(String s) throws ImmutableException {
	if(finalized)
	    throw new ImmutableException("Already saved; can't append");
	int l = s.length();
	current.append(s);
	return new StdTextSpan(this, current.length()-l, current.length());
    }

    public Span getCurrent() {
	return new StdTextSpan(this, 0, current.length());
    }

    public Span getSpan(int offs1, int offs2) {
	return new StdTextSpan(this, offs1, offs2);
    }


    public BlockId saveOrGetId(StormPool saveTo)
    				throws java.io.IOException {
        if(finalized)
	    return this.mediaserverId; // saved already

	if(current.length() == 0)
	    return null; // don't save empty block

	byte[] bytes = current.toString().getBytes("UTF8");
	String content_type = "text/plain; charset=UTF-8";

	BlockOutputStream stream = saveTo.getBlockOutputStream(content_type);
	stream.write(bytes);
	stream.close();

	finalized = true;
	mediaserverId = stream.getBlockId();

	ScrollBlockManager.msCache.put(mediaserverId, this);
	return mediaserverId;
    }

    public char[] getCharArray() {
	if(curchars == null || curchars.length != current.length())
	    curchars = current.toString().toCharArray();
	return curchars;
    }

}


