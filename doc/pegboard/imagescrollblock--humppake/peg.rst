==========================================================================
PEG imagescrollblock--humppake: interface ImageScrollBlock
==========================================================================

:Authors:  Asko Soukka
:Date-Created: 2003-09-03
:Last-Modified: $Date: 2003/09/08 12:28:29 $
:Revision: $Revision: 1.8 $
:Status:   Implemented
:Scope:    Minor
:Type:     Interface

This PEG prososes a new Alph scrollblock type: ImageScrollBlock.
ImageScrollBlock would be the interface for Alph scrollblocks
containing only a single image.

Currently our FenPDF application supports importing only PDF and PS
files. This PEG arose from the need of importing also plain images.

Own scrollblock type for images is needed because handling images
differ from handling any of the current scrollblock types. Therefore
imagescrollblocks must be able to distinguish from other scrollblocks
and they need their own type.

Issues
======

Aren't current interfaces enough?

    RESOLVED: The currently existing scrollblock interfaces are
    PageScrollBlock and TextScrollBlock, both extending the basic
    ScrollBlock interface. TextScrollBlock is meant for text and
    PageScrollBlock for PDF and PS documents.
 
    scrollblocks containing single images should be handled
    separately from both the text scrollblocks and PDF/PS document
    scrollblocks. Therefore, they need their own scrollblock
    interface.

What spesific methods should the new interface provide?

    RESOLVED: None for now. The interface is currently needed only
    to separate ImageScrollBlocks from other ScrollBlocks. New 
    ImageScrollBlock spesific methods could be pegged later, if
    found necessary.

Changes
=======

Create the new interface ``org.nongnu.alph.ImageScrollBlock`` with no
method.

Change SimpleImageScroll to implement the new interface. No changes
into SimpleImageScroll's code is needed, since the interface doesn't
present any new methods.

