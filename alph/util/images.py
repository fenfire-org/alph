# 
# Copyright (c) 2003-2004, Tuomas J. Lukka and Matti Katila
# This file is part of Alph.
# 
# Alph is free software; you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# Alph is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
# Public License for more details.
# 
# You should have received a copy of the GNU General
# Public License along with Alph; if not, write to the Free
# Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
# MA  02111-1307  USA
# 


import os
import re

def convert2Image(file, prefix, resolution):
    """Convert a pageimagescroll to .png image files.

    Parameters:

    file: The filename to convert
    prefix: The prefix to use for the resulting files.
	    If prefix is "ab" then the files of 
	    a three-page .pdf will be
	    "ab1", "ab2" and "ab3". Note: 1-based.
    resolution: The resolution to use. A number (dpi) or
            a string of the form <xres>x<yres> is 
	    expected.
    """
    print "Processing ",file
    # Don't check right now-- no os.popen in Jython
    #l = os.popen("file -i -b %(file)s" % locals()).readlines()[0]
    #l = l.rstrip().lstrip()
    #print "'%(l)s'" % locals()

    #if l != "application/pdf" and l != "application/postscript":  
    #	print "Unknown - ignoring"
    #	return 0

    cmdline = "convert -size %(resolution)s -resize %(resolution)s %(file)s %(prefix)s" % locals()
    print "RUNNING ",cmdline
    res = os.system(cmdline)
    if res:
	print "Error running gs",res
	return 0

    return 1


