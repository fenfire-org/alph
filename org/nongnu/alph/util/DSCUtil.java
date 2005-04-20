/*
# 
# Copyright (c) 2003, Tuomas J. Lukka
# Copyright (c) 2005, Benja Fallenstein
#
# This file is part of Alph.
# 
# Alph is free software; you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# Alph is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
# Public License for more details.
# 
# You should have received a copy of the GNU General
# Public License along with Alph; if not, write to the Free
# Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
# MA  02111-1307  USA
#
*/ 

package org.nongnu.alph.util;
import java.io.*;
import java.util.*;

public class DSCUtil {
    public static boolean dbg = false;
    private static void p(String s) { System.out.println("DSCUtil:: "+s); }

    /** Convert a postscript file to a .dsc file reliably.
     *  Because several files have broken dsc comments, we
     *  need to really *run* the file through gs to do our stuff.
     * 
     *  Returns true if successful.
     */
    public static boolean reliablePS2DSC(String infile, String outfile) throws IOException {

	String cmdline = "gs -q -sOutputFile=- -sDEVICE=pswrite -dNOPAUSE -dBATCH "+infile+" | grep '^%' >"+outfile;

	if(dbg) p("cmdline -- "+cmdline);

	Process p = Runtime.getRuntime().exec(cmdline);
	try {
	    return p.waitFor() == 0;
	} catch(InterruptedException e) {
	    throw new Error(e);
	}
    }

    /** Convert a pdf file to a .dsc file. */
    public static boolean reliablePDF2DSC(String infile, String outfile) throws IOException {
	String cmdline = "pdf2dsc "+infile+" "+outfile;

	if(dbg) p("CMD: "+cmdline);

	Process p = Runtime.getRuntime().exec(cmdline);
	try {
	    return p.waitFor() == 0;
	} catch(InterruptedException e) {
	    throw new Error(e);
	}
    }

    private static int count(String s, String find) {
	int i=0, n=0;
	while(true) {
	    i = s.indexOf(find, i);
	    if(i < 0) return n;
	    n++;
	}
    }

    private static int countparens(String s) {
	return count(s, "(") - count(s, ")");
    }

    public static List _dsctokens(String string) {
	string = string.trim();

	int last = 0;
	List tokens = new ArrayList();
	int lastspace = 0;
        for(int i=0; i<string.length(); i++) {
	    char c = string.charAt(i);

	    if(c == ' ') {
	        if(lastspace > 0) {
		    last = i+1;
		    continue;
		}
	        if(countparens(string.substring(last, i)) != 0) {
		    continue;
		}
	        tokens.add(string.substring(last, i));
	        last = i+1;
	        lastspace = 1;
	    } else {
	        lastspace = 0;
	    }
        }

	if(lastspace == 0) {
	    tokens.add(string.substring(last));
	}

        if(dbg) p("Tok: '"+string+"' "+tokens);
	return tokens;
    }

    static class SinglePageInfo {
	String media, orient;

	void parseLine(String str) {
	    if(str.startsWith("%%PageMedia:")) {
		media = str.substring(12).trim();
	    } else if(str.startsWith("%%PageOrientation:")) {
		orient = str.substring(19).trim();
	    }
	}

	SinglePageInfo copy() {
	    SinglePageInfo spi = new SinglePageInfo();
	    spi.media = media;
	    spi.orient = orient;
	    return spi;
	}
    }

    public static PageInfo dsc2pageinfo(String infile) throws IOException {
	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "US-ASCII"));
	List lines = new ArrayList();
	String lastLine = null;

	// Collapse continued lines
	while(true) {
	    String line = in.readLine();

	    if(line == null) {
		if(lastLine != null) lines.add(lastLine);
		break;
	    }

	    if(line.startsWith("%%+")) {
		if(lastLine == null)
		    throw new Error();

		lastLine += line.substring(3);
	    } else {
		if(lastLine != null) lines.add(lastLine);
		lastLine = line;
	    }
	}

        // Look for DocumentMedia
        String documentmedia = null;
	for(int i=0; i<lines.size(); i++) {
	    String line = (String)lines.get(i);
	    if(line.startsWith("%%DocumentMedia:")) {
		if(documentmedia != null)
		    throw new Error("parse error");

		documentmedia = line;
	    }
	}

        if(documentmedia == null) {
            documentmedia = "%%DocumentMedia: A4 595 842 0 () ()";
            if(dbg) p("No DocumentMedia found: using defaults.");
	}

        List media = _dsctokens(documentmedia.substring(16));
	if(media.size() % 6 != 0)
	    throw new Error("parse error");

	String name, w, h, weight, color, type;

        Map mtypes = new HashMap();
        for(int i=0; i<media.size(); i+=6) {
	    name = (String)media.get(i);
	    w = (String)media.get(i+1);
	    h = (String)media.get(i+2);
	    weight = (String)media.get(i+3);
	    color = (String)media.get(i+4);
	    type = (String)media.get(i+5);

	    float[] wh = new float[2];
	    wh[0] = Float.parseFloat(w);
	    wh[1] = Float.parseFloat(h);

	    mtypes.put(name, wh);
	}

	/*
        // find the dsc defaults
        start = [i for i in range(0, len(lines)) if lines[i].startswith("%%BeginDefaults")];
        end = [i  for i in range(0, len(lines)) if lines[i].startswith("%%EndDefaults")];

        if len(start) {
	    assert len(end);
	    assert 1 == 0;
	    // Don't want to deal with these quite yet
	}
	*/
    
        // Start parsing the pages
        int curpage = 0;
        List pages = new ArrayList();

        SinglePageInfo defaultSPI = new SinglePageInfo();
        SinglePageInfo spi = null;

        for(int i=0; i<lines.size(); i++) {
	    String l = (String)lines.get(i);

	    if(l.startsWith("%%Page:")) {
	        List toks = _dsctokens(l.substring(7));
	        if(toks.size() != 2) throw new Error("parse error");
	        int newpage = Integer.parseInt((String)toks.get(1));
	        if(newpage != curpage + 1) throw new Error("parse error");
	        curpage = newpage;
	        if(spi != null) {
		    pages.add(spi);
		}
	        spi = defaultSPI.copy();
	    } else {
	        if(spi != null) {
		    spi.parseLine(l);
		}
	    }
	}

        pages.add(spi);

        for(int i=0; i<pages.size(); i++) {
	    SinglePageInfo page = (SinglePageInfo)pages.get(i);
            if(page.media == null) {
                //if (dbg) p('No PageMedia found: using "%s".' % (mtypes.keys()[0]));
                page.media = (String)mtypes.keySet().iterator().next();
	    }
	    if(page.orient == null) {
                if(dbg) p("No PageOrientation found: using 'portrait'.");
                page.orient = "portrait";
	    }
	}

	float[] _w = new float[pages.size()], _h = new float[pages.size()];

	for(int i=0; i<pages.size(); i++) {
	    SinglePageInfo page = (SinglePageInfo)pages.get(i);
	    float[] wh = (float[])mtypes.get(page.media);
	    _w[i] = wh[0];
	    _h[i] = wh[1];
	}

        return new PageInfo(_w, _h);
    }
}

