=============================================================
PEG imagespan_getwholepage--tjl: Access entire page
=============================================================

:Author:   Tuomas J. Lukka
:Last-Modified: $Date: 2003/06/23 12:37:11 $
:Revision: $Revision: 1.1 $
:Status:   Accepted

Now that there are two distinct types of image spans: plain
``ImageSpan``s and ``PageImageSpan``s, it is not easy to get the entire
image that the current span is a subset of, as ``PageImageSpan``s require
different treatment. This is not good.

This PEG resolves the problem by adding a method to ``ImageSpan`` to 
get the entire 2D image the current span is a subset of.

Issues
======

- The name for the new method? We need to say it's the opposite of a subimage,
  or somehow express that it's a larger area.

    RESOLVED: getSuperImageSpan. It's not perfect but at least it expresses that.

Changes
=======

Into ``ImageSpan`` add the method ``getSuperImageSpan``::

    /** Get the whole contiguous 2D image that this span is a part of.
     */
    ImageSpan getSuperImageSpan();

