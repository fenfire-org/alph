
===========================================================================
PEG alph_lite_naming--tjl: The concepts for things implemented in Alph Lite
===========================================================================

:Author:   Tuomas J. Lukka, Benja Fallenstein
:Status:   Incomplete

The TString name was not good for the schema after all, since it implies
it can handle all transcludable stuff whereas it actually can handle only
identity of text given inside the spans.

We need to clarify these concepts and names.

Issues
======

Concepts
========

Transcludable text / string
    A piece of text that contains deeper identity information
    than just the sequence of characters. Enables transclusions
    by copying into a new context.

Text Identity Markup Language (TIML)
    A markup language, defined by the 

TIML Schema

TString

TSpan
