/*
SpanSerializer.java
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
import org.nongnu.alph.util.*;
import java.util.List;
import java.util.Iterator;

/** A simple span serializer / deserializer.
 */

public class SpanSerializer {

    public String namespace;

    public String span2xml(Span s0) {
	if(s0 instanceof org.nongnu.alph.FakeTextSpan) {
	    org.nongnu.alph.FakeTextSpan t = 
		(org.nongnu.alph.FakeTextSpan)s0;
	    return "<fts t=\""+
		    XMLUtil.escapeXML(t.getText())+"\"/>";
	} else if(s0 instanceof org.nongnu.alph.RICCTextSpan) {
	    org.nongnu.alph.RICCTextSpan t = 
		(org.nongnu.alph.RICCTextSpan)s0;
	    return "<uts b=\""+t.getScrollId()+
		    "\" o=\""+t.offset()+
		    "\" t=\""+XMLUtil.escapeXML(t.getText())+"\"/>";
	} else if(s0 instanceof TextSpan) {
	    TextSpan s = (TextSpan)s0;
	    return "<" + (namespace == null ? "" : 
					      namespace+":") +
			"ts b=\""+s.getScrollId()+
			"\" s=\""+s.offset()+
			"\" e=\""+(s.offset()+s.length())+"\"/>";
	} else if(s0 instanceof PageSpan) {
	    PageSpan s = (PageSpan)s0;
	    return "<" + (namespace == null ? "" : 
					      namespace+":") +
			"ps b=\""+s.getScrollId()+
			"\" s=\""+s.offset()+
			"\" e=\""+(s.offset()+s.length())+"\"/>";
	} else if(s0 instanceof PageImageSpan) { 
	    PageImageSpan s = (PageImageSpan)s0;
	    java.awt.Point p = s.getLocation();
	    java.awt.Dimension d = s.getSize();
	    return "<" + (namespace == null ? "" : 
					      namespace+":") +
			"pis b=\""+s.getScrollId()+
			"\" p=\""+s.getPageIndex()+
			"\" x=\""+p.x+"\" y=\""+p.y+
		        "\" w=\""+d.width+
			"\" h=\""+d.height+"\"/>";
	// ImageSpan has to be *after* PageImageSpan as it's 
	// a superclass
	} else if(s0 instanceof ImageSpan) {
	    ImageSpan s = (ImageSpan)s0;
	    return "<" + (namespace == null ? "" : 
					      namespace+":") +
			"is b=\""+s.getScrollId()+"\"/>";
	} else {
	    throw new Error("Don't know how to serialize "+s0+" yet");
	}
    }

    StringBuffer b = new StringBuffer();

    public void addSpan(Span s0) {
	b.append(span2xml(s0));
    }

    public String get() {
	return "<alph>"+b.toString()+"</alph>";
    }

    static public String serialize(Enfilade1D enf) {
	List l = enf.optimized().getList();
	SpanSerializer ser = new SpanSerializer();
	for(Iterator i = l.iterator(); i.hasNext(); ) {
	    ser.addSpan((Span)i.next());
	}
	return ser.get();
    }

}
