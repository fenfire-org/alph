=============================================================
PEG spancellviews--tjl: A new architecture for showing spans
=============================================================

:Author:   Tuomas J. Lukka
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision: $Revision: 1.1 $
:Status:   Incomplete

For reorganizing xupdf in view of the coming article deadline,
we need to look at showing spans/enfilades in a cell in a flexible
way.

Issues
======

- Two basic approaches: using the vobscene for the info, or
  storing the info in a different (single cellview instance) object.

  Pros and cons:

  Vobscene:
    + simplicity
    - inflexibility

  Cellview instance:
    + the data can be stored efficiently, and needs to be created
      anyway
    - complexity

      RESOLVED: we shall use the VobScene, in a slightly special way,
      as described below.
    


Basics
======

CellView A places the contents of a cell into a vobscene under some
CS in the hierarchy. ConnectingView B wants to know where a certain
subspan of it is. Instances of this problem:

    - anchors of buoys

    - irregularframing of the linked-to area in a buoy.

    - xanalogical comparison of two documents with beams

Version 0.6 allowed this to happen easily by storing the spans as the
keys in the VobScene. This worked fine as long as VobScenes were not
hierarchical, and no image spans were used.

Now the situation is different: when linking to an image span we want
to know exactly where a given subspan is.


The solution
============

The solution is to define a convention that
Image- and PageSpan keys in a VobScene will be used on coordinate
systems exactly when the coordinate system is a mapping whose
unit square is exactly where the Image- or PageSpan (PageSpan's 
first page!) is **linearly** placed.
