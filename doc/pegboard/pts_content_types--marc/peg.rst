==============================================================================
PEG pts_content_types--marc: new accepted Content-Types in PermanentTextScroll 
==============================================================================

:Author:        Marc Schiereck
:Stakeholders:  Benja
:Last-Modified: $Date: 2003/03/31 09:12:44 $
:Revision:      $Revision: 1.1 $
:Status:        Current

In peg email_storage--marc it is defined that the content-type 
of header blocks is message/rfc822 and of body blocks is 
the content-type specified in the email's header.
The problem is that PermanentTextScroll only accepts 
"text/plain; charset=UTF-8" and "application/x-gzigzag-GZZ1".
Since we want to transclude from both header and body, 
PermanentTextScroll should also accept the content-type
"message/rfc822" and "text/plain" in other charsets than UTF-8.

Issues
------

- Which charsets exactly should be accepted? This PEG should be
  very specific as to the requirements. E.g., "this and that",
  "all in the IANA list fooblah", ... would be ok.

Changes
-------

PermanentTextScroll has to be changed accordingly. There it
will be necessary to determine the charset used in the body
to set the appropriate charset for the String defined in
load().

