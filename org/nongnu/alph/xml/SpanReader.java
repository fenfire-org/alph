/*
SpanReader.java
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

package org.nongnu.alph.xml;
import org.nongnu.alph.*;

import java.util.ArrayList;
import java.util.List;

/** A simple span serializer / deserializer.
 * Just takes a sequence of spans and adds them to a list.
 * To clear the list between readings, call clear().
 */

public class SpanReader extends org.xml.sax.helpers.DefaultHandler  {
    public static boolean dbg = false;
    private static void pa(String s) { System.out.println("SpanReader:: "+s); }

    public Alph alph;

    public SpanReader(Alph alph) {
	this.alph = alph;
    }

    private ArrayList spans = new ArrayList();

    /** Empty this SpanReader for another read operation.
     */
    public void clear() { spans.clear(); }

    public List getSpans() { return spans; }

    public  void startElement(String uri, String localName, String qName, 
			    org.xml.sax.Attributes attributes)  {
	if(dbg) pa("Se: '"+uri+"' '"+localName+"' '"+qName+"'");
	// Wrapper element
	if(qName.equals("alph")) return;
	if(qName.equals("ts")) {
	    String b = attributes.getValue("b");
	    int s = Integer.parseInt(attributes.getValue("s"));
	    int e = Integer.parseInt(attributes.getValue("e"));
	    spans.add(((TextScrollBlock)(alph.getScrollBlock(b)))
		. getSpan(s, e));
	} else if(qName.equals("uts")) {
	    String b = attributes.getValue("b");
	    int o = Integer.parseInt(attributes.getValue("o"));
	    String t = attributes.getValue("t");
	    spans.add(new org.nongnu.alph.RICCTextSpan(b, o, t));
	} else if(qName.equals("fts")) {
            String t = attributes.getValue("t");
            spans.add(new org.nongnu.alph.FakeTextSpan(t));
	} else if(qName.equals("ps")) {
	    String b = attributes.getValue("b");
	    int s = Integer.parseInt(attributes.getValue("s"));
	    int e = Integer.parseInt(attributes.getValue("e"));
	    spans.add(((PageScrollBlock)(alph.getScrollBlock(b)))
		.getSpan(s, e));
	} else if(qName.equals("is")) {
	    String b = attributes.getValue("b");
	    spans.add(((ImageScrollBlock)(alph.getScrollBlock(b)))
		.getCurrent());
	} else if(qName.equals("pis")) {
	    try {
		String b = attributes.getValue("b");
		int p = Integer.parseInt(attributes.getValue("p"));
		int x = Integer.parseInt(attributes.getValue("x"));
		int y = Integer.parseInt(attributes.getValue("y"));
		int w = Integer.parseInt(attributes.getValue("w"));
		int h = Integer.parseInt(attributes.getValue("h"));
		spans.add(((PageScrollBlock)(alph.getScrollBlock(b)))
			  .getPage(p).subArea(x,y,w,h));
	    } catch(Throwable t) {
		System.out.println("------------ PageImageSpan -------------");
		t.printStackTrace();
	    }
	} else {
	    throw new Error("Unknown element '"+localName+"'");
	}
    }
    public  void endElement(String uri, String localName, String qName) {
    }


}
