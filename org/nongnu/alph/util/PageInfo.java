/*
PageInfo.java
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

/** A class that represents the sizes of the pages 
 * from a postscript/pdf document.
 */
public class PageInfo implements java.io.Serializable {
    float[] w;
    float[] h;

    public PageInfo(float[] w, float[] h) {
	this.w = w;
	this.h = h;
    }

    public int getNPages() { return w.length; }
    public float getWidth(int page) { return w[page]; }
    public float getHeight(int page) { return h[page]; }

}
