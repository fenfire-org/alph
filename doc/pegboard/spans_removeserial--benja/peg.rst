=============================================================
PEG ``spans_removeserial--benja``: Remove span serialization
=============================================================

:Authors:  Benja Fallenstein
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision: $Revision: 1.1 $
:Date-Created: 2002-09-05
:Status:   Accepted

While developing the saving/loading functionality as present in
0.8alpha1, the first thing we tried was to use Java serialization. It
was always apparent that using Java serialization has two big
disadvantages: that it is a) a Java-specific format, tailored to this
specific platform, with no implementations on other platforms; and b)
not human-readable. On the other hand, Tuomas hopes that serialization
is very efficient compared to other formats.

When the implementation was done, I realized a third gotcha: for
serialization to work, classes must remain structurally unchanged. This
means that all serialized classes must be FROZEN, because any change to
an existing class would break loading of serialized data. This means: NO
refactoring of the ``gzz.media.impl`` package. You can add new classes, but
not change/rename/move existing ones.

However, ``gzz.media.impl`` is **not** in a good shape currently. For example,
there is far too much stuff inside ``ScrollBlockManager``, degrading
readability. And many classes are badly named.

The benefit of clearer code wasn't realized when using serialization,
either: all the serialization special-casing in ``gzz.media.impl`` makes the
code much worse, and to me (having written it) it is actually much
harder to understand than the YAML writer/reader, which does all the
encoding/decoding to/from YAML in one central monolithic place.

Therefore, I very much think that using Java serialization, which
depends a lot on the particular implementation used, is the wrong way to
do things. Currently it is there as an alternative to the default,
YAML-based file format. I suggest getting rid of serialization, possibly
inventing a Java-independent binary format in the future if the
human-readable YAML-based approach turns out to be too slow.

This means removal of Media*Stream and the serializing functionality in
``ScrollBlockManager``'s inner classes.


