/*
AbstractScrollBlock.java
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

/** A scrollblock that is based on a permanent, physical
 * data block.
 */
public abstract class AbstractScrollBlock implements ScrollBlock {

    protected final Alph alph;
    protected Object blockid;
    protected final String contentType;

    public AbstractScrollBlock(Alph alph, Object blockid, String contentType) {
	this.alph = alph;
	this.blockid = blockid;
	this.contentType = (contentType != null ?
			contentType.intern() : null);
    }

    public String getContentType() { return contentType; }

    public BlockFile getBlockFile() throws java.io.IOException {
	return alph.getBlockFile(this);
    }

    public boolean isMutable() {
	return blockid == null;
    }

    public Object getBlockId() {
	return blockid;
    }

    public String getID() {
	if(blockid != null) return blockid.toString();
	return "!!!TMPID!!!XXXFOO";
    }

    public int hashCode() {
	return getID().hashCode();
    }


    static final String gzz1 = "application/x-gzigzag-gzz1";
    static final String plainText = "text/plain;charset=utf-8";

    public static ScrollBlock createBlock(Alph alph, Object id, 
				String ct) {

	ct = ct.toLowerCase();
	int ind = ct.indexOf('/');
	if(ind < 0)
	 throw new IllegalArgumentException("Can't parse mediatype "+ ct);
	String type = ct.substring(0,ind);

	if(type.equals("text") &&
	    !ct.equals(plainText))
	     throw new IllegalArgumentException(
		     "Unknown text mime type '"+ct+"'");

	if(type.equals("text") || ct.equals(gzz1)) {
	    return new PermanentTextScroll(alph, id, ct);

	} else if(type.equals("image")) {
	    return new SimpleImageScroll(alph, id, ct);
	} else if(type.equals("application")) {
	    return new PageImageScroll(alph, id, ct);
	} 

	throw new IllegalArgumentException(
		"Unknown mediatype "+ct);

    }

    
}

