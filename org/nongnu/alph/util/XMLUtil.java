/*
XMLUtil.java
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

package org.nongnu.alph.util;

public class XMLUtil {
    public static String escapeXML(String s) {
	StringBuffer buf = new StringBuffer();
	for(int i=0; i<s.length(); i++) {
	    switch(s.charAt(i)) {
		case '&': buf.append("&amp;"); break;
	        case '<': buf.append("&lt;"); break;
		case '>': buf.append("&gt;"); break;
	        case '\'': buf.append("&apos;"); break;
		case '\"': buf.append("&quot;"); break;
	        case '\n': buf.append("&#10;"); break;
		default:
			  buf.append(s.charAt(i)); break;
	    }
	}
	return buf.toString();
    }
}
