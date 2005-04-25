/*
PageImageSpan.java
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
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;


/** An scrollblock containing paged media (PS/PDF).
 * <p>
 * For performance on startup, dsc info is cached into the directory
 * specified by the alph.cache.dsc property
 */

public class PageImageScroll extends AbstractScrollBlock implements PageScrollBlock{
    public static boolean dbg = false;
    final static void pa(String s) { System.out.println(s); }

    static File dscCacheDir;

    static {
	String dir = System.getProperty("alph.cache.dsc", "./tmpimg");
	dscCacheDir = new File(dir);
	if(!dscCacheDir.exists()) {
	    pa("DSC CACHE DIR NOT FOUND -- NOT CACHING");
	    dscCacheDir = null;
	} else {
	    pa("USING DSC CACHE "+dscCacheDir);
	}
    }

    PageInfo pageInfo;

    /** Single-page spans.
     */
    PageImageSpanImpl[] pages;


    public boolean equals(Object o) {
        if(!(o instanceof ScrollBlock)) return false;
	ScrollBlock sb = (ScrollBlock)o;
	return sb.getID().equals(getID());
    }
    //    Mediaserver.Block block;

    /** Generate the page info for this scrollblock.
     */
    private void generatePageInfo() {
	pa("Generating page info: "+getID());

	try {
	    File dsc;
	    BlockFile blockfile;

	    dsc = File.createTempFile("dsc",".dsc");
	    blockfile = alph.getBlockFile(this);

	    boolean res;
	    
	    String infile = blockfile.getFilename(), outfile = dsc.getPath();
	    
	    if(contentType.equals("application/postscript")) {
		res = DSCUtil.reliablePS2DSC(infile, outfile);
	    } else {
		res = DSCUtil.reliablePDF2DSC(infile, outfile);
	    }
	    
	    blockfile.close();
	    
	    if(!res)
		throw new Error("Couldn't generate .dsc");
	    
	    pageInfo = DSCUtil.dsc2pageinfo(dsc.getPath());
	    
	    if(dbg) pa("Result from dsc: "+pageInfo);
	} catch(IOException e) {
	    throw new Error(""+e);
	}
    }

    private File getDscCacheFile() {
	if(dscCacheDir == null) 
	    return null;
	// Munge the string to not use bad chars
	String s = getID();
	s = s.replace('/','-');
	s = s.replace(':','_');
	return new File(dscCacheDir, "dsccache-"+s);
    }

    /** Get the page info for this scrollblock from the cache or generate it.
     */
    private void getPageInfo() {
	File cache = getDscCacheFile();
	if(cache == null) {
	    generatePageInfo();
	    return;
	}
	if(cache.exists()) {
	    // try to read
	    ObjectInputStream i = null ; 
	    try {
		i = new ObjectInputStream(
			new FileInputStream(cache));
		this.pageInfo = (PageInfo)i.readObject();
		i.close();
	    } catch(Exception e) {
		pa("Exception reading cache: "+e);
		e.printStackTrace();
		try {
		    if(i != null)
			i.close();
		} catch(IOException g) {
		}
	    }
	}
	if(this.pageInfo == null) {
	    // Couldn't read, 
	    generatePageInfo();
	    try {
		ObjectOutputStream o = new ObjectOutputStream(
			new FileOutputStream(cache));
		o.writeObject(this.pageInfo);
		o.close();
	    } catch(IOException e) {
		pa("Exception writing cache: "+e);
		e.printStackTrace();
	    }
	}
    }

    public PageImageScroll(Alph alph, Object blockId, String contentType) {
	super(alph, blockId, contentType);
	if(!( contentType.equals("application/postscript") ||
	      contentType.equals("application/pdf")))
	    throw new Error("Can't do pageimagescroll except for ps/pdf. ("
				+contentType+")");
	getPageInfo();

	pages = new PageImageSpanImpl[pageInfo.getNPages()];
    }

    /** The resolution of the coordinates on the original paper.
     */
    public float coordinateResolution() {
	return 72;
    }


    public Span getCurrent() {
	return new StdPageSpan(this, 0, pageInfo.getNPages());
    }
    public PageSpan getSpan(int p0, int p1) {
	return new StdPageSpan(this, p0, p1);
    }

    private static class PageImageSpanImpl 
	extends AbstractImageSpan implements PageImageSpan {
	int page;

	protected AbstractImageSpan
		createNew(int x, int y, int w, int h) {
	    return new PageImageSpanImpl(scrollBlock, page, x, y, w, h);
	}
	public PageImageSpanImpl(ScrollBlock sb, int page, int x, int y, int w, int h) {
	    super(sb, x, y, w, h);
	    this.page = page;
	}
	public int getPageIndex() { return page; }

	public boolean equals(Object o) {
	    return super.equals(o) &&
		    ((PageImageSpanImpl)o).page == page;
	}

	public boolean intersects(Span s) {
	    return super.intersects(s) &&
		    ((PageImageSpanImpl)s).page == page;
	}

	public int hashCode() {
	    return super.hashCode() ^ (page * 237);
	}

	public String toString() {
	    // Can't call Object.toString() since superclass overrides it
	    // (there was a way but can't recall)
	    String oString = getClass().getName() + "@" + Integer.toHexString(hashCode());
	    return "["+oString+": "+scrollBlock.getID()+" @ ("+
		    page +") "+
		    x+", "+y+", "+w+", "+h+"]";
	}

	public ImageSpan getSuperImageSpan() {
	    return ((PageImageScroll)scrollBlock).getPage(this.page);
	}
    }

    public PageImageSpan getPage(int p) {
	if(pages[p] == null) {
	    pages[p] = new PageImageSpanImpl(this, p, 0, 0, 
		    (int)pageInfo.getWidth(p),
		    (int)pageInfo.getHeight(p)
		    );
	}
	return pages[p];
    }


}


