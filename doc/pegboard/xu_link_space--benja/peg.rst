===================================================================
``xu_link_space--benja``: Put xanalogical links into the same space
===================================================================

:Author:   	Benja Fallenstein
:Date:     	2002-11-14
:Revision: 	$Revision: 1.1 $
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Type:		Architecture
:Status:	Irrelevant


Xanalogical links are currently stored in a different space,
because we do not want the cells that make up the link
to be shown as transclusions.

However, links are not the only place where we do not
necessarily want transclusions to be shown. Other examples
include marking spans to make links (we do not want
the mark cells to show), an applitude to render textured
vector graphics (we do not want the cells containing
the textures to show), and so on. Whenever a cell's contents
are not actually 'user contents' but are used in the
lower-level fabric of the system, we don't usually want
them to show up as transclusions.

Additionally, this makes the concept of a space less pure:
it introduces the need to know about
two different ``Space`` objects in many places, or
to have a Storm pointer reference in the main space that allows
loading the link space.
In reality, of course, the contents of the two spaces
must be presented as a unit to the user (as Xanadu had
documents with links in them as well as the document's main text, 
at least conceptually we have spaces with links in them
as well as the cells & connections).

Finally, links should be first-class members of a space,
available for connections. For example, it should be possible
to connect an explanation to a link (like 'this is related
because...'), which could be rendered in the middle between
the link's endpoints on the screen. It should be possible
for such an explanation to be a clone of something else
in the space etc.

Thus, I propose to put the links into the same space
and to have a more generally useful way for avoiding
to show them as transclusions. Since whether a cell
is shown as a transclusion generally depends on the
cell type, let's also introduce a structure
for cell types now:

    If a cell is not a headcell on d.cell-type,
    the headcell on that dimension is its type.
    (If a cell is a headcell on d.cell-type, it
    does not have a specific cell type assigned.)

Now, for each cell type, we just need to decide whether
it should be shown as a transclusion or not.
There are various ways to do that; for example, we could
specify that if ``type.s(d_no_transclusion, 1).equals("true")``,
we do not show the transclusion, or some such.
However, string matching seems like bad practice;
we'd probably better have specific cells (URIs) denoting
'true' and 'false' to connect to them.

We should also probably not have an own dimension
for specifying this attribute of a cell type, since
we may want to introduce others and when there are ten
or so this structure becomes really inconvenient to view.
Therefore, let's have two general purpose dimensions here,
``d.type-attribute`` and ``d..type-attibute-set``.
Attributes will also be specific cells (URIs).
For an attribute A and a cell type T, the value
of the attribute will be the cell at the first
intersection of the ``d..type-attribute-set``
rank starting at T with the ``d.type-attribute`` rank
starting at A. For example::

                           __
            A1       A2   /  \         
             |        |   |   |        +---> d..type-attribute-set
   T1  --- true  --- 103 --- 222       |
             |        |   |            v
   T2  --- false --- 299  |     d.type-attribute
                      \___/


In this case, the value of A1 for T1 would be 'true';
the value of A1 for T2 would be 'false'; the value
of A2 for T1 would be 103; and the value of A2 for T2
would be 299. (The intersection at '222' is ignored,
because the '103' intersection comes earlier on the
``d..type-attribute-set`` rank, which is what counts.)

So, for the "don't show as transclusion" attribute,
if there is an intersection between a cell's cell type
and that attribute cell, and if that intersection
is the 'true' cell or a clone of it, then the original
cell will not usually be shown as a xu transclusion.
(Of course, for diagnostic views, we may want to view
such cells also.)

Notes:

- The respective applitudes creating the cells are responsible
  for putting the cell type in.
- Ted has been suggesting that cells have cell types.
  I don't know whether he intended to have cell types
  in the structure or externally, but since mapping them
  into the structure is trivial, let's just do that.
- Cell types will be well-known cells (i.e., the cells' URIs
  will be known, like a dimension's).

\- Benja