=====================================================
``styled_text--benja``: Formatted text as a primitive
=====================================================

:Author:       Benja Fallenstein
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision:     $Revision: 1.1 $
:Status:       Accepted
:Date-Created: 2002-10-30


In a modern computer system, *formatted* text should be
the primitive we normally deal with. Unfortunately,
all-to-often, users are only allowed to format *some*
text, not all (for example, text in a text document
may be formatted, but the title as entered in a dialog
box can't be). Many spreadsheet programs only seem to
allow formatting a whole cell, not individual characters
in it.

This is stupid: It should be possible to apply e.g.
emphasis in all text you enter.

This probably stems from the fact that the primitive in
the underlying programming language is the "string:"
a *non-formatted* list of characters. For the same reason,
xanalogical text is practically unthinkable.

In Gzz, let's make the primitive *formatted*, xanalogical
text. Like it should be possible to link to and transclude
all text, it should be possible to emphasize and color
all text, for example.

This PEG doesn't specify a full implementation of formatted
text; it proposes that we do make formatted text one of
our goals and sets a direction, but leaves the details
to later PEGs.



Discussion
==========

Tuomas has said he'd like to try out the way the Xanadu project
planned to do formatted text: through links. For example,
to emphasize some text, you'd make a link to it saying,
"Emphasize this!"

This is indeed an interesting idea. The two main runners for
how to implement styled text seem to be:

- the Xu one: using external links;
- the boring one: storing the markup in the enfilade.

The Xu way of doing this would, for example, allow us to say
"This should be emphasized!" in one transclusion, and have
the emphasis appear in another transclusion as well.
If I have transcluded a sentence in three places, and I decide
that it flows better when one word is emphasised, it would
be cool if emphasizing it in one copy would automatically
emphasize it in the other copies.

However, this is not how Xu handles this, for a simple reason:
Formatting properties must be versioned. 
It must be easy,  for example, to emphasize
a piece of a document, then go back to the old, non-emphasized
version, then view the two side-by-side; it would be absolutely
unexpected to have the emphasis appear in the older version,
too. It is possible that we have different versions of the
same text (different in the formatting applied to them, too)
in two different cells on the screen, for example in Ted's
real cut-and-paste system.

The intent in Xanadu was to handle this by only interpreting
the links in the same document: In Xanadu, a document would
contain some text and a set of links, and only the formatting
links in the same document would be applied to the text.
This solves the problem above, but it also makes the excitement
go away for me: different transclusions of a sentence would
be in different documents, so formatting one of them would not
automatically format the others.

Now, this is a revealing similarity to the *order* of characters-- 
the vstream. If I have the same sentence in different contexts,
sometimes I would like changes (inserting a character, swapping
two words) to appear in all instances, but in other cases I don't.
This is exactly the same as with the
formatting properties: the computer can't decide the problem,
so even though it might be convenient if it could do it right,
we don't make an attempt that could only do it wrongly.
(In zzstructure, we can use clones if we want all transclusions
to change when one changes.)

Additionally, it is possible that we want to have the 
same characters twice in the same document (in Gzz: cell), with
different formatting properties. For example, we might give
a quote from another document verbatim, then quote it again
several times while analyzing it in-depth,
emphasizing different parts of it each time.
In the Xu model, this simply doesn't work, since we cannot
specify in the link which transclusion of the quote should
be formatted how-- if we emphasize a word in one of the
transclusions, it'll be formatted in all others as well.

I think this is a good argument for choosing the "boring" way,
above: Implementing formatting in the enfilade. 


Proposal
========

Conceptually,
an enfilade object is simply a list of spans (or, if you prefer,
characters); a "formatted enfilade object" will conceptually
be a list of spans each of which has zero or more "styles"
applied to it (or, if you prefer, a list of characters each
of which has zero or more "styles"). A style will simply be
defined by a URI (which means: a cell). It is up to outside
mechanisms to determine what the style means. (The point is
that styles should when possible be semantic, for example
"emphasis" or "author's address" etc., so that they can be
interpreted in different ways for presentation.)

In a true enfilade representation, this will be really efficient
to implement as a "DSPative property" in enfilade speak.
Basically this means that at the highest possible node
in the tree, you store the information, "All text inside this
node has style X applied to it." In this scheme, it is equally
cheap to apply a style to a single word, to a section or to
the whole document (for the same reason that both small- and
large-scale rearrangements are efficient; the order of spans
in the enfilade is also a "DSPative property;" in fact, it's the
name-giving "DSP" (disposition) property itself.)

\- Benja
