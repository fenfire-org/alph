=======================================================
``xu_text--benja``: Use random ids for Xanalogical text
=======================================================

:Author:	Benja Fallenstein
:Date:		2003-02-17
:Revision:	$Revision: 1.2 $
:Last-Modified: $Date: 2003/04/06 08:15:20 $
:Type:		Architecture
:Scope:		Major
:Status:	Implemented


This PEG proposes to use random ids 
instead of Storm blocks for xu text.

Storm is good for saving space when transcluding "media"
in multiple places by refering to a single block
(this was what it was originally conceived for, after all).
For text, it doesn't work as well-- it doesn't gain much
(text spans may often contain less characters than the
block's id), it costs much (loading lots of text blocks
to assemble all the text), and it makes things complex
(bowdlerization when publishing, having to save the text
block before saving a space to get the text block's id).

Let's drop text blocks.

Issues
======

- Do we want to change to have *only* this type of xu text?

    RESOLVED: Not yet. The current format (explicit blocks)
    and the new format (urn-5) can coexist easily because
    they share the TextSpan API. The users of Alph can
    decide which one to use.

Changes
=======

A new kind of text span is introduced: (uri, offset, string) triples.

We will keep the old kind at least for now. (We may reconsider this
when we have more experience.)

For example, ("urn:urn-5:...", 17, "foo") would be a span
with offset 17 and length 3 in the 'block' ``urn:urn-5:...``,
containing the string "foo". A xanalogical plaintext would simply
be a list of such triples. (Benefit: Looking at the serialization
of one, you could actually see the text it contains.)

The 'string' component is part of the identity. That is,
("urn:urn-5:...", 17, "bar") is a *different* span from the above.
Finding transclusions thus becomes a three-step process:

1. Find spans with the same URI (character-for-character).
2. Among these, find spans whose range overlaps with the searched range.
3. Check that the strings match in the overlapping range.
   (Discard spans that don't match.)

Step 3 is new. Checking strings for equality should be reasonably
fast, much faster than, say, rendering the transclusions found
through this mechanism.

Finding links is similar, since it is defined in terms of
finding transclusions.

API changes
-----------

In ``Span``, ``getScrollBlock()`` will be made optional (may return
``null``). We still want to use a hashtable when searching for
overlaps; for that, we'll add ``getScrollId()`` (used as the hashtable
key -> no two spans with different scroll ids may intersect).
To determine whether two spans overlap, their ``intersects()`` method
must be used. 

Two text spans as described above overlap iff their ranges overlap
and the substrings in the overlapping range are equal.


\- Benja
