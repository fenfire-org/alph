=============================================================
PEG deprecations1--tjl: Deprecating old stuff, round 1
=============================================================

:Author:   Tuomas J. Lukka
:Last-Modified: $Date: 2003/10/04 11:58:51 $
:Revision: $Revision: 1.1 $
:Status:   Current

There is some crud in Alph left over from long times ago.
Let's get rid of it.

Issues
======

Changes
=======

Deprecate (and remove ASAP) ::

    Span1D.getRelativeStart(Span1D)
    Span1D.getRelativeEnd(Span1D)

Nothing uses those methods and they're easy to reproduce
outside the spans if ever needed.
