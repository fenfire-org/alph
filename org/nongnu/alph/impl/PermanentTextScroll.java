/*
PermanentTextScroll.java
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
import java.io.*;

/** A text scroll block loaded from somewhere.
 */

public class PermanentTextScroll extends AbstractScrollBlock
	implements TextScrollBlock
                  {

    char[] chars;
    boolean loadingFailed;

    public PermanentTextScroll(Alph alph, Object blockId, String contentType) {
	super(alph, blockId, contentType);
    }

    public boolean equals(Object o) {
        if(!(o instanceof PermanentTextScroll)) return false;
	ScrollBlock sb = (ScrollBlock)o;
	return sb.getID().equals(getID());
    }

    protected final void load() {
	if(chars != null || loadingFailed) return;
	loadingFailed = true;

        // Note: for the legacy string content to work, we need to be able
        // to load GZZ1 diffs as text blocks (see GZZ1Handler.LegacyContent
        // javadoc for more info).
	if(!contentType.equals(AbstractScrollBlock.plainText) &&
	   !contentType.equals(AbstractScrollBlock.gzz1) &&
	   !contentType.equals("message/rfc822")) {
	    throw new Error("Unknown text block content type '"+
			contentType+"'");
	}

	String string;
	try {
	    BlockFile f = getBlockFile();
	    string = new String(SlurpStream.slurp(
			new FileInputStream(f.getFile())), "UTF8");
	    f.close();
	} catch(Exception e) {
	    loadingFailed = true;
	    e.printStackTrace();
	    throw new Error("Exception while reading: "+e);
	}

        this.chars = string.toCharArray();
	loadingFailed = false;
    }

    public TextSpan append(char ch) throws ImmutableException {
	throw new ImmutableException("Can't append to permanent scroll block");
    }
    public TextSpan append(String s) throws ImmutableException {
	throw new ImmutableException("Can't append to permanent scroll block");
    }

    public Span getCurrent() {
	load();
	return new StdTextSpan(this, 0, chars.length);
    }

    public Span getSpan(int offs1, int offs2) {
	return new StdTextSpan(this, offs1, offs2);
    }


    public char[] getCharArray() {
	load();
	return chars;
    }

}


