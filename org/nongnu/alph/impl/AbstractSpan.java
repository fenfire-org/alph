/*
AbstractSpan.java
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

/** An abstract implementation of Span.
 * Simply stores the ScrollBlock.
 */
abstract public class AbstractSpan implements Span {

    protected ScrollBlock scrollBlock;
    public AbstractSpan(ScrollBlock scrollBlock) { 
	this.scrollBlock = scrollBlock; 
    }
    public ScrollBlock getScrollBlock() { return scrollBlock; }
    public String getScrollId() { return scrollBlock.getID(); }
    
}
