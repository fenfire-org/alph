=============================================================================================
``obsless_get_enfilade--benja``: A variant of ``getEnfilade`` not taking an ``Obs`` parameter
=============================================================================================

:Author:	Benja Fallenstein
:Date:		2002-11-26
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision:	$Revision: 1.1 $
:Type:		Interface
:Scope:		Minor
:Status:	Accepted


Currently, the only method to get the enfilade in a cell is::

    Enfilade1D getEnfilade(Cell c, Obs obs);

in ``VStreamCellTexter``. Other than for example ``getText()``, this
is not a method delegated from elsewhere (``Cell``); i.e., this
is the method the front-end code actually frequently uses.

It's pretty unusual that this kind of method does not have a
non-Obs-taking variant; let's add that::

    Enfilade1D getEnfilade(Cell c);

\- Benja
