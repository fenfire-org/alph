/*
SlurpStream.java
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
import java.io.*;
import java.util.*;

public class SlurpStream {
    /** Read all there is in the given inputstream.
     */
    public static byte[] slurp(InputStream i) throws IOException {
	byte[] arr = new byte[10000];
	int offs = 0;
	int len;
	while((len = i.read(arr, offs, arr.length - offs)) > 0) {
	    offs += len;
	    if(offs == arr.length) {
		byte[] narr = new byte[arr.length * 2];
		System.arraycopy(arr, 0, narr, 0, arr.length);
		arr = narr;
	    }
	}
	byte[] farr = new byte[offs];
	System.arraycopy(arr, 0, farr, 0, offs);
	return farr;
    }
}
