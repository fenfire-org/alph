/*
Media.java
 *    
 *    Copyright (c) 2003, : Tuomas J. Lukka
 *    
 *    This file is part of Fenfire.
 *    
 *    Fenfire is free software; you can redistribute it and/or modify it under
 *    the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *    
 *    Fenfire is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 *    Public License for more details.
 *    
 *    You should have received a copy of the GNU General
 *    Public License along with Fenfire; if not, write to the Free
 *    Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *    MA  02111-1307  USA
 *    
 *    
 */
/*
 * Written by : Tuomas J. Lukka
 */

package org.nongnu.alph.util;
import java.util.*;
import org.nongnu.alph.*;

public class EnfUtil {
    /** Get the set of ScrollBlock objects in the given enfilade.
     */
    static public Set getScrollBlocks(Enfilade1D enf) {
	List l = enf.getList();
	Set set = new HashSet();
	for(Iterator i = l.iterator(); i.hasNext(); ) {
	    Span s = (Span)i.next();
	    ScrollBlock sb = s.getScrollBlock();
	    set.add(sb);
	}
	return set;
    }
    /** Get a set of "representative" spans from the enfilade.
     * Most useful for pagespans.
     */
    static public Set getScrollBlockRepresentatives(Enfilade1D enf) {
	Set res = new HashSet();
	Set done = new HashSet();
	List l = enf.getList();
	for(Iterator i = l.iterator(); i.hasNext(); ) {
	    Span s = (Span)i.next();
	    ScrollBlock sb = s.getScrollBlock();
	    if(done.contains(sb)) continue;
	    res.add(s);
	}
	return res;
    }
}
