====================================================
``xu_text_2--benja``: Separate xu text from xu media
====================================================

:Author:	Benja Fallenstein
:Date:		2003-02-21
:Revision:	$Revision: 1.1 $
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Type:		Architecture
:Scope:		Major
:Status:	Current


XML-based formats (as well as e.g. YAML or RDF)
know plain-text strings-- sequences of characters.
Images or audio units cannot be embedded in these sequences.
These formats refer to images by URIs, but not to text,
which they include directly. We want to enhance such
formats with Xanalogical text.

Giving Xanalogical identities to the character content in an XML file
seems like a much less intrusive change than using an enfilade format
which can include e.g. audio as well as characters, so that you could
say ``<tag>`` *10000 audio units* ``</tag>``.


Changes
=======

``Enfilade1D`` is renamed to ``AlphText`` and will only contain
text spans. ``ImageSpan`` and ``PageSpan`` are used for images
and page fragments.

\- Benja