=============================================================
Design of Alph: the default implementations of the interfaces
=============================================================

Text spans
==========

.. no content

"Normal text spans"
===================

..  UML:: alph_textspanimpl_std

    jlinkpackage org.nongnu.alph

    class TextSpan "interface"
	jlink

    class TextScrollBlock
	jlink

    jlinkpackage org.nongnu.alph.impl

    class StdTextSpan
	jlink
	inherit TextSpan

    class PermanentTextScroll
	jlink

    class TransientTextScroll
	jlink
    ---
    horizontally(50, hor_c, TextSpan, TextScrollBlock);
    horizontally(50, hor_d, StdTextSpan, PermanentTextScroll, TransientTextScroll);
    vertically(50, ver_c, TextSpan, StdTextSpan);

URN5 text spans
===============

URN 5 text spans are a relatively new innovation by Benja Fallenstein:
instead of really having a permanent block of text, we virtualize
it by always providing the text with the span, and use URN-5 ids.

This has both advantages and disadvantages: on the other hand,
some things may be spoofed (unless care is taken),
but on the other hand, it makes several operations easier,
without having to worry about the text blocks.


..  UML:: alph_textspanimpl_urn

    jlinkpackage org.nongnu.alph

    class TextSpan "interface"
	jlink

    jlinkpackage org.nongnu.alph.impl

    class (URNSTextSpan) URN5TextSpan
	jlink
	inherit TextSpan
    ---
    vertically(50, ver_c, TextSpan, URNSTextSpan);

Fake text spans
===============

Fake text spans are objects that implement the text span
interface but are implemented as strings, not permanent media
references.

..  UML:: alph_textspanimpl_fake

    jlinkpackage org.nongnu.alph

    class TextSpan "interface"
	jlink

    jlinkpackage org.nongnu.alph.impl

    class FakeTextSpan
	jlink
	inherit TextSpan
    ---
    vertically(50, ver_c, TextSpan, FakeTextSpan);

Fake spans are similar to URN5 spans, except that they have *no* 
IDs. They are therefore somewhat cheaper but lose the ability
to link by transcluding.
