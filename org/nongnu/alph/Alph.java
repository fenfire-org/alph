/*
Alph.java
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

package org.nongnu.alph;
import java.io.File;

/** The overall alph instance class.
 * This is where it all starts.
 */
public abstract class Alph {
    public abstract ScrollBlock getScrollBlock(String uri);

    public abstract BlockFile getBlockFile(ScrollBlock block) 
	throws java.io.IOException;
    public abstract java.io.InputStream getBlockInputStream(ScrollBlock block)
	throws java.io.IOException;

    /** Add a file to this Alph.
     * This is an optional method, and may be missing from some implementations.
     * <p>
     * This method is final because it fixes the implementation
     * to call addFile_id and then getScrollBlock. This is done
     * to obtain a certain fuzzy feeling of security about
     * the file being there also the <b>next</b> time the program
     * is started by avoiding certain kinds of optimizations.
     */
    public final ScrollBlock addFile(File f, String contentType) 
	    throws java.io.IOException{
	String id = addFile_id(f, contentType);
	return getScrollBlock(id);
    }

    /** Implement adding a file to this Alph.
     * @return The id; passing the id to getScrollBlock() shall return the file.
     */
    protected String addFile_id(File f, String contentType) 
	    throws java.io.IOException {
	throw new UnsupportedOperationException();
    }

    /** The directory in which SMALL items pertaining to blocks may be cached.
     * Currently intended for postscript/PDF DSC files for page sizes.
     */
    protected File cacheDir;


    /** Get the cache dir for small items.
     * Nothing larger than 1kb/block should be stored there.
     */
    public File getCacheDir() { return cacheDir; }
}



