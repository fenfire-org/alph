=============================
Alph design -- the interfaces
=============================

Alph is an implementation of xanalogical hypertext (see e.g. Nelson's
writings). The fundamental point in xanalogical hypertext is that
each smallest unit of media has a unique identity.

Spans and Media types
=====================

The most user-visible objects in Alph are spans. A span
is a contiguous block of permanent media, e.g. "the 5 characters
'xyahb' typed on 5 May 2003 by Janne V. Kujala" or 
"the 23x12 block of pixels in a particular photo snapshot of a custom
controller built from lego bricks by Asko Soukka".
Spans are operated on much as any immutable media objects,
like Java's own ``String`` 's.

The media types handled by Alph are

- text

- image

- pageimage (e.g. PDF files): images of several consecutive pages

There additionally exist interfaces for

- audio

- video

but these have not yet been implemented and will not be discussed
further in this document.

The inheritance hierarchy between the core media type interfaces
is as follows:

..  UML:: alph_mediatypes

    jlinkpackage org.nongnu.alph

    class Span "interface"
	jlink	
	methods
	    intersects(Span s)
	    String getScrollId()
	    ScrollBlock getScrollBlock()

    class (SpanID) Span1D "interface"
	jlink
	inherit Span
	methods
	    int offset()
	    int length()
	    SpanID subSpan(..)
	    SpanID append(SpanID s)

    class TextSpan "interface"
	jlink
	inherit SpanID
	methods
	    String getText()

    class PageSpan "interface"
	jlink
	inherit SpanID
	methods
	    PageImageSpan getPage(int ind)

    class ImageSpan "interface"
	jlink
	inherit Span
	methods
	    Point getLocation()
	    Dimension getSize()
	    ImageSpan subArea(..)

    class PageImageSpan "interface"
	jlink
	inherit ImageSpan
	methods
	    int getPageIndex()

    dep "create" PageSpan PageImageSpan 

    ---

    Span.c = (0,0);

    horizontally(50, vs, 
	TextSpan, PageSpan, PageImageSpan, ImageSpan);

    vs.c = Span.c + (0, -250);
    SpanID.c = .5[Span.c, TextSpan.c];

The interfaces in the lowest row are those that are actually
implemented by some classes.

Note: Spans are immutable objects - the ``append()`` method returns
a new span, if the the object and the parameter were consecutive
in the right order.

Manipulating text: Enfilades, Span makers
=========================================

.. The 

Scrollblocks
============

Alph does not use the tumbler model in previous implementations,
but is rather based on *scrollblocks* - fixed units
of fluid media. This allows Alph to exist on top of Storm.

Each span can contain media only inside one scrollblock:

..  UML:: alph_scrollblock

    jlinkpackage org.nongnu.alph

    class Span "interface"
	jlink

    class ScrollBlock
	jlink
	assoc multi(1) - multi(*) Span
	methods
	    String getContentType()
	    Span getCurrent()
	    boolean isMutable()
	    String getID()
	    Object getBlockId()
	    
    ---
    ScrollBlock.c = Span.c + (-200,0);


The scrollblock model assumes that given a span it is possible
to access all the fluid media units inside the same block.
However, this is not always true:
there are some span types that do not have a real scroll block - 
``FakeTextSpan`` and ``URN5TextSpan``.


Access to the original media: block files
=========================================

Alph does not currently provide access methods
for data in images and pagespans. This is because the 
programs may use their own methods for accessing these -
e.g. Fenfire uses either Java AWT *or* the Libvob OpenGL 
system implemented in C++. Forcing access using AWT images
would make things inefficient for the OpenGL system.

Because of this, Alph provides direct access to the underlying
data block by allowing the access through the  ``BlockFile`` class.

..  UML:: alph_blockfile
    
    jlinkpackage org.nongnu.alph

    class ScrollBlock
	jlink
	assoc multi(1) - multi(0..1) BlockFile
	methods
	    BlockFile getBlockFile()

    class BlockFile
	jlink
	methods
	    java.io.File getFile()
	    String getFilename()
	    void close()

    ---
    ScrollBlock.c = BlockFile.c + (-200,0);

The blockfile is created by calling ``ScrollBlock.getBlockFile()``,
and can then be used to obtain access to the file.
The file may be temporary, which is why it is important to
explicitly ``close()`` the ``BlockFile`` after use.

XXX getBlockInputStream, delegation to Alph?

The central media repository class: ``Alph``
============================================

Finally, there is a central class ``Alph`` which
takes care of finding ScrollBlocks based on URIs,
adding files etc.


..  UML:: alph_alph

    jlinkpackage org.nongnu.alph

    class Alph
	jlink
	assoc compos - multi(*) ScrollBlock
	methods
	    ScrollBlock getScrollBlock(String uri)
	    ScrollBlock addFile(File f, String contentType)

    class ScrollBlock
	jlink
    ---
    horizontally(50, hor_c, Alph, ScrollBlock);
