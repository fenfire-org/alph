/*
StormAlph.java
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
import org.nongnu.storm.*;
import org.nongnu.storm.util.BlockTmpFile;

import java.io.IOException;
import org.nongnu.navidoc.util.SoftValueMap;

/** An Alph service implemented using Storm.
 */
public class StormAlph extends Alph {

    StormPool stormPool;

    public StormAlph(StormPool stormPool) {
	this.stormPool = stormPool;
    }

    SoftValueMap scrollBlocks = new SoftValueMap();

    public ScrollBlock getScrollBlock(String uri) {
	ScrollBlock sb;
	sb = (ScrollBlock)scrollBlocks.get(uri);
	if(sb != null) return sb;
	BlockId id = new BlockId(uri);
	sb = (ScrollBlock)scrollBlocks.get(id);
	if(sb != null) return sb;

	String ct = id.getContentType();
	sb = AbstractScrollBlock.createBlock(this, id, ct);

	scrollBlocks.put(uri, sb);
	scrollBlocks.put(id, sb);

	return sb;
    }

    public BlockFile getBlockFile(ScrollBlock block) throws IOException {
	BlockId id = (BlockId)block.getBlockId();

	if(id == null) return null;

	// Queue a request for the block already.
	stormPool.request(id, null);

	return new StdBlockFile(BlockTmpFile.get(stormPool.get(id)));
    }

    public java.io.InputStream getBlockInputStream(ScrollBlock block) 
	throws IOException {

	return stormPool.get((BlockId)block.getBlockId()).getInputStream();
    }

    protected String addFile_id(java.io.File f, String contentType) 
	    throws java.io.IOException {
	BlockOutputStream os = stormPool.getBlockOutputStream(contentType);
	org.nongnu.storm.util.CopyUtil.copy(
		new java.io.FileInputStream(f),
		os);
	os.close();
	Block b = os.getBlock();
	return b.getId().toString();
    }

    public StormPool getStormPool() { return stormPool; }
}
