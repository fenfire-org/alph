=============================================================
``miniblocks--benja``: A relatively sane way to do fake spans
=============================================================

:Author:        Benja Fallenstein
:Date: 		2002-10-31
:Last-Modified: $Date: 2003/03/31 09:12:43 $
:Revision:      $Revision: 1.1 $
:Status:        Accepted


We need fake spans to store "computer data" like numbers
representing cursor positions or window coordinates.
This PEG presents a way to do fake spans in a way that
is efficient, yet allows them to be xanalogically
linked and transcluded.


Desiderata
----------

Fake spans should:

- only be saved if they are still in the space when saving
  (i.e., if the computer changes the window coordinates
  3'000 times in a session, only the last set should be
  saved; we don't want to save the 2'999 spans representing
  window coordinates we have already thrown away);
- not be appended to the scroll containing the user's
  key strokes.


Proposal
--------

Create "miniblocks" whose whole content is contained in their ID.
In the file format, a fake span currently could look like this::

    [FakeTextSpan, "214"]

A real span looks like this::

    [TextSpan, "storm:block:01E88CEE7CF19F016EEF00B315C0B930C953DB7EF2", 0, 3]

Under this proposal, the fake span could look like this:

    [TextSpan, "storm:inline:uzPa+vB7kMBWMZWw-Og58D2NXGeZ:1::214", 0, 3]

That is, in the block's ID, we give:

- a random id (very similar to a urn-5)
- the MIME type, or blank if ``text/plain``
- the whole content of the block

The syntax of the URI would be like this:

    storm:inline:<RANDOM-BYTES>:<COUNTER>:<MIME-TYPE>:<CONTENT>

where ``<MIME-TYPE>`` may be blank in the case of ``text/plain``, and
``<COUNTER>`` has the same role as in a urn-5 and may be omitted.
``<RANDOM-BYTES>`` must be encoded in the same way as in a urn-5.

Since all fake spans then have xanalogical ids (the URI of a Storm block
plus the offsets), links and transclusion resolving works perfectly fine.


Security considerations
-----------------------

Whereever the id of an inline block (colloquial: "miniblock") goes,
the whole content of the block goes. This means that when publishing
a block that has a reference to a miniblock, the miniblock also
gets published. Also, when transcluding a single character
from a miniblock, the whole miniblock is always carried along.

To alleviate these problems, it should be respected that miniblocks
must:

- be atomic-- that is, one miniblock should only contain one
  piece of information (for example, a single number) which will
  normally be transcluded together (in the sense that if you're
  willingly to reveal one character, you can be reasonably expected
  to be willingly to reveal all);
- not contain information which is likely to be sensitive, or
  for which you don't know how likely it is to be sensitive
  (i.e., an internal number is likely to be ok, but for text
  entered by the user or a URL taken from a HTML page visited,
  we have no idea).

\- Benja
