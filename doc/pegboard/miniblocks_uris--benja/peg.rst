===============================================================
``miniblocks_uris--benja``: Changes to the miniblock URI syntax
===============================================================

:Author:        Benja Fallenstein
:Date: 		2003-01-01
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision:      $Revision: 1.1 $
:Affects-PEGs:  miniblocks--benja
:Status:        Accepted


``miniblocks--benja`` states that the default MIME type
of a miniblock is ``text/plain``. Since we are promoting Unicode,
this should be ``text/plain;charset=UTF-8``.


Issues
======

- Another issue with ``miniblocks--benja`` is that it is based
  on the ``data:`` URI scheme, but doesn't follow it too closely.
  Should we change this at the same time?

   RESOLVED: Yes, because we're about to implement ``miniblocks--benja``
   and it is still time to change this, while later we'd have
   compatibility issues. Being consistent with other specifications
   is always a Good Thing :-)


Changes
=======

In addition, to be closer to `RFC 2397`_ (The "data" URL scheme),
the syntax is modified to the following::

    storm:data:<random>:[<counter>]:[<mimetype>][;base64],<data>

where ``<random>`` is a base64-encoded string containing
at least 160 bits of randomness (as in urn-5), ``<counter>`` is
a counter of miniblocks with the same ``<random>`` part
(only ``[0-9]`` digits are allowed),
``<mimetype>`` is the content type of the block,
with optional parameters, and ``<data>`` is the actual data.

If ``;base64`` is given, ``<data>`` is encoded in base64
as per `RFC 2045`_, section 6.8. (Base64 Content-Transfer-Encoding).
Note that this differs from ``<random>``, which is encoded
according to the `urn-5 spec`_.

We will not support base64 encoding in Gzz initially
(just as we don't support anything but ``text/plain;charset=UTF-8``).

The case of '``data``' and '``;base64``' does not matter
(as well as the case of '``storm``' doesn't, per the URI spec).



.. _RFC 2397: http://www.ietf.org/rfc/rfc2397.txt
.. _RFC 2045: http://www.ietf.org/rfc/rfc2045.txt
.. _urn-5 spec: http://www.iana.org/assignments/urn-informal/urn-5
