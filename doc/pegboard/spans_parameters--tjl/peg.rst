=====================================================================
PEG ``spans_parameters--tjl``: Span subArea etc. parameter convention
=====================================================================

:Authors:  Tuomas Lukka
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision: $Revision: 1.1 $
:Status:   Implemented

It was recently noticed that the method::

    Span getSpan(int offs, int len);

in TextScrollBlock had a different convention from Span1D (and thus, TextSpan)::

    Span1D subSpan(int o1, int o2);

This caused one bug and will cause more if not stopped.

Issues
------

    - Isn't (offset,length) more natural?
	
	Not really, see e.g. Dijkstra's writings. 

	Also, in Java, String.substring() is what 
	subSpan() needs
	to conform with.

    - Should ImageSpan and PageSpan coordinates be changed the same way?
      Specifically,::

	    PageSpan subArea(int page0, int page1, int x, int y, int w, int h);

      mixes the two conventions. 

Changes
-------

Replace, in TextScrollBlock,::

    Span getSpan(int offs, int len);

with::

    Span getSpan(int offs1, int offs2);

defined to be equivalent to::

    getCurrent().subSpan(offs1, offs2);

