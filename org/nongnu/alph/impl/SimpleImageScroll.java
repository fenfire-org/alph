/*
SimpleImageScroll.java
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

import java.io.IOException;
import java.awt.*;
import java.awt.image.*;

/** An image scrollblock
 */

public class SimpleImageScroll extends AbstractScrollBlock implements ImageScrollBlock{

    int width=-1, height=-1;

    public SimpleImageScroll(Alph alph,  Object blockid, String contentType) {
	super(alph, blockid, contentType);
    }

    public boolean equals(Object o) {
        if(!(o instanceof ScrollBlock)) return false;
	ScrollBlock sb = (ScrollBlock)o;
	return sb.getID().equals(getID());
    }
    public int hashCode() {
	return getID().hashCode();
    }

    private void loadImageSize() {
	int ind = contentType.indexOf('/');
	int ind2 = contentType.indexOf(';');
	if(ind2 < 0) ind2 = contentType.length();

	String type = contentType.substring(0,ind);
	String subtype = contentType.substring(ind+1, ind2);
	
	if(!type.equals("image"))
	    throw new Error("Image Block isn't an image");

	Block block ;
	Dimension d;

	try {
	    java.io.InputStream is = alph.getBlockInputStream(this);
	    d = org.nongnu.navidoc.util.ImageSize.readSize(is);
	    if(d == null)
		throw new Error("Couldn't get size of image block");
	    is.close();

	} catch(Exception e) {
	    throw new Error("Couldn't load image block");
	}

	this.width = d.width;
	this.height = d.height;
    }

    class SimpleImageSpan extends AbstractImageSpan  {
	SimpleImageSpan(int x, int y, int w, int h) {
	    super(SimpleImageScroll.this, x, y, w, h);
	}

	protected AbstractImageSpan
		createNew(int x, int y, int w, int h) {
	    return new SimpleImageSpan(x, y, w, h);
	}

	public ImageSpan getSuperImageSpan() {
	    return (SimpleImageSpan)SimpleImageScroll.this.getCurrent();
	}

    }

    public Span getCurrent() {
	if(width < 0) loadImageSize();
	return new SimpleImageSpan(0, 0, width, height);
    }

    public Span getSpan(int x, int y, int w, int h) {
	return new SimpleImageSpan(x, y, w, h);
    }


}


