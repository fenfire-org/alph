/*   
AudioSpan.java
 *    
 *    Copyright (c) 2001, Ted Nelson and Tuomas Lukka
 *
 *    This file is part of Gzz.
 *    
 *    Gzz is free software; you can redistribute it and/or modify it under
 *    the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *    
 *    Gzz is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 *    Public License for more details.
 *    
 *    You should have received a copy of the GNU General
 *    Public License along with Gzz; if not, write to the Free
 *    Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *    MA  02111-1307  USA
 *    
 *
 */
/*
 * Written by Tuomas Lukka
 */

package org.nongnu.alph;

/** A span - contiguous piece of a permascroll - of sound.
 * The natural units for sound are not defined and may depend on
 * the sound format.
 * However, they should be mostly linear.
 * XXX Mono/Stereo - what to do?
 * XXX Other measures?
 * XXX Get sound data / play?
 * XXX Get graphical representation - some type of graph of volume etc.
 */

public interface AudioSpan extends Span1D {


}
