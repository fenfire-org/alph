# 
# Copyright (c) 2003, Tuomas J. Lukka
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
import jarray
import alph
from copy import deepcopy

dbg = 0

def p(*s):
    print 'dscutil.py::', s

def reliablePS2DSC(infile, outfile):
    """Convert a postscript file to a .dsc file reliably.
    Because several files have broken dsc comments, we
    need to really *run* the file through gs to do our stuff.

    Returns true if successful.
    """

    cmdline = "gs -q -sOutputFile=- -sDEVICE=pswrite -dNOPAUSE -dBATCH %(infile)s "
    cmdline += " | grep '^%%' >%(outfile)s"

    cmdline = cmdline % locals()

    if dbg: 
	print 'cmdline -- ', cmdline
    result = os.system(cmdline)
    if dbg: 
	print 'result -- ', result
    if result: return 0

    return 1

def reliablePDF2DSC(infile, outfile):
    """Convert a pdf file to a .dsc file.
    """
    cmdline = "pdf2dsc %(infile)s %(outfile)s" % locals()

    if dbg: 
	print "CMD:",cmdline
    result = os.system(cmdline)
    if dbg: 
	print "PDFDSC:",result
    if result: return 0

    return 1

def countparens(string):
    return string.count("(") - string.count(")")

def _dsctokens(string):
    string = string.strip()
    last = 0
    tokens = []
    lastspace = 0
    for i in range(0,len(string)):
	if string[i:i+1].isspace():
	    if lastspace:
		last = i+1
		continue
	    if countparens(string[last:i]):
		continue
	    tokens.append(string[last:i])
	    last = i+1
	    lastspace = 1
	else:
	    lastspace = 0
    if not lastspace:
	tokens.append(string[last:])
    if dbg: 
	print "Tok: '"+string+"' ",tokens
    return tokens

class SinglePageInfo:
    def parseLine(self, str):
        if str.startswith("%%PageMedia:"):
            self.media = str[12:].strip()
        elif str.startswith("%%PageOrientation:"):
            self.orient = str[19:].strip()

def dsc2pageinfo(infile):
    f = open(infile)
    lines = f.readlines()
    i = len(lines) - 1
    # Collapse continued lines
    while i >= 0:
	if lines[i].startswith("%%+"):
	    lines[i-1] += lines[i][3:]
	    lines[i:i] = []
	i-=1
    # Look for DocumentMedia
    documentmedia = [l for l in lines if l.startswith("%%DocumentMedia:")]
    if len(documentmedia) == 0:
        documentmedia=['%%DocumentMedia: A4 595 842 0 () ()']
        if dbg: p('No DocumentMedia found: using defaults.')
    assert len(documentmedia) == 1
    media = _dsctokens(documentmedia[0][16:])
    assert len(media) % 6 == 0
    mtypes = {}
    for i in range(0, len(media)/6):
	(name, w, h, weight, color, type) = media[i*6:(i+1)*6]
	if dbg: 
	    p('Docmedia "%(name)s" %(w)s %(h)s %(weight)s %(color)s %(type)s' \
		    % locals())
	mtypes[name] = (float(w),float(h))

    # find the dsc defaults
    start = [i for i in range(0, len(lines)) if lines[i].startswith("%%BeginDefaults")]
    end = [i  for i in range(0, len(lines)) if lines[i].startswith("%%EndDefaults")]
    if len(start):
	assert len(end)
	assert 1 == 0
	# Don't want to deal with these quite yet
    
    # Start parsing the pages
    curpage = 0
    pages = []

    defaultSPI = SinglePageInfo()
    spi = None

    for l in lines:
	if l.startswith("%%Page:"):
	    toks = _dsctokens(l[7:])
	    assert len(toks) == 2
	    newpage = int(toks[1])
	    assert newpage == curpage + 1
	    curpage = newpage
	    if spi:
		pages.append(spi)
	    spi = deepcopy(defaultSPI)
	else:
	    if spi:
		spi.parseLine(l)
    pages.append(spi)

    for page in pages:
        if not hasattr(p, 'media'):
            if dbg: p('No PageMedia found: using "%s".' % (mtypes.keys()[0]))
            page.media = mtypes.keys()[0]
        if not hasattr(p, 'orientation'):
            if dbg: p('No PageOrientation found: using "portrait".')
            page.orientation = 'portrait'

    pagemedias = [ mtypes[page.media] for page in pages ]
    
    w = jarray.array([ m[0] for m in pagemedias ], 'f')
    h = jarray.array([ m[1] for m in pagemedias ], 'f')

    return alph.util.PageInfo(w, h)

