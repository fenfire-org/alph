=================================================================
PEG alph_lite--tjl: A dependencyless text-oriented subset of Alph
=================================================================

:Author:   Tuomas J. Lukka, Benja Fallenstein
:Last-Modified: $Date: 2002/11/14 15:40:07 $
:Revision: $Revision: 1.5 $
:Status:   Accepted

For the Alph article, and in general, it would be nice to be able
to give a small, self-contained subset of Alph that doesn't require
any dependencies (aside from XML parser) and just works
and is easily integrated anywhere. The subset should probably
be text-oriented as images require Storm. Also, basically every 
user-oriented program has a good reason for using a text-oriented
fluid media API, whereas most programs don't need image or
pagespan functionality; a text-oriented subset is better suited 
to the needs of most developers.

This PEG proposes such a subset.

Issues
======

- Should the subset be compilable by itself or just distributable
  as a .jar? This affects e.g. the inclusion of ``ScrollBlock``,
  which is returned by ``Span.getScrollBlock()``

  RESOLUTION: Yes, it should compile by itself. It could then be
  a separate free software package but remain completely compatible.

- Should we rename SpanMaker to TextSpanFactory or something else?

  RESOLUTION: Yes, but it will not be included in alph lite.

- Should we rename URN5SpanMaker and URN5TextSpan? Something like
  RICCTextSpanFactory and RICCTextSpan?

  PROPOSED RESOLUTION: Yes, those are good names.

- How should the XML parsing stuff be done for maximum modularity?
  SAX? DOM?

  RESOLVED: For now, just two routines that do
  TString <-> Complete Input/OutputStream, with no user-visible modularity.


Changes
=======

This PEG defines the initial starting point for the Alph lite 
subset. The subset is frozen and 
will later be changed through new PEGs.

Alph lite shall contain equivalent (maybe to be renamed) classes::

    Enfilade1D (restricted to strings)
    Span
    Span1D
    TextSpan

    impl.FakeSpan
    impl.URN5TextSpan

    xml.SpanReader
    xml.SpanSerializer

    org.nongnu.storm.util.URN5NameSpace

The following classes are explicitly excluded::

    Alph

The following (or equivalent with different name) will
be brought in from Fenfire::

    EnfiladeOverlapIndex

To summarize the name changes below, at the end we shall have

    most important user-visible classes/interfaces

	TString
	TStringXML

    interfaces

	Span
	Span1D
	TextSpan

    two textspan implementations

	FakeTextSpan
	RICCTextSpan

    indices and comparison tools (to be determined in detail later)
	
	TStringOverlapIndex ??
	TStringComparisonIter ??


    util

	org.nongnu.storm.util.URN5NameSpace



Renamings and API twids - effective in Alph mainline as well
------------------------------------------------------------

The renamings are a part of this PEG since they 
affect the comprehensibility of the resulting
system.

TString
"""""""

First, the main Enfilade-like class: it shall be
called ``TString`` for Transcludable String.
It will only accept ``TextSpan`` objects.

TString shall have no public constructors,
but rather the methods::

    public static TString newRICC(String s);
    public static TString newFake(String s);

which create new TStrings of a given type.

TString shall implement all the methods 
of the String class. The equality and comparison
methods shall compare **with** ids, and
there shall be ::

    boolean equalsString(String s)
    boolean equalsString(TString s)
    int compareString(String s)
    int compareString(TString s)

For non-referential comparisons, there is also a class ::

    public static class StringComparator implements Comparator {
	...
    }
    public static StringComparator stringComparator = new StringComparator();

For concatenation, there is::

    public TString plus(TString other);

TStringXML
""""""""""

A class with static methods for converting TStrings to/from 
input/outputstreams. 

    public static TString read(TString OutputStream outputStream);
    public static void write(TString tString, OutputStream outputStream);







