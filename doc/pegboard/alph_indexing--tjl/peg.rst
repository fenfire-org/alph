=============================================================
PEG ``alph_indexing--tjl``: Generic indexing API
=============================================================

:Authors:  Tuomas Lukka, Benja Fallenstein
:Last-Modified: $Date: 2003/03/31 09:12:43 $
:Revision: $Revision: 1.1 $
:Status:   Accepted [ partly implemented ]

Replace SearchableText with a more general indexing API where the user
can specify how to match strings. This change would also make the core
uml diagram accurate: no classes in the package gzz would exist that
aren't documented there. 

The classes in the core APIs (the ones visible to the frontend) would
then be

    * IndexManager

    * The classes representing index types

    * The interface of an index, containing a single method call
      Collection getMatches(Object o);


Issues
------

    * How to specify search types? Ignore-case, regex, match only at
      beginning, match only whole words etc.

	  Through the index instance, i.e. asking the IndexManager for
	  different index instances for the different policies. 

    * How to make index management as general as possible? Or should we
      keep to cell content indexing for now?

	  By using Objects as the keys, not Cells, and by not
	  making the classes dependent on the zzstructure APIs. 
	  Spaces should not index things other than cell
	  content through this interface.

    * Can we implement Xanalogical text search at the same time, with
      the same APIs?

	Yes. See IndexManager.getXuIndexer()

Changes
-------

Some of the changes are already done; gzz.index exists and works.
Further changes:

    * Make gzz.index into a peg-frozen core package

    * Remove StringSearcher

    * Add the intersects calls from gzz.media.impl.Enfilade1DImpl into
      gzz.media.Enfilade1D

    * Remove SpanSet and its implementations

    * Delete SearchableCellTexter, as its functionality will be
      supported by the new framework


