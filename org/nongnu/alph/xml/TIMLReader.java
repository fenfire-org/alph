/*
TIMLReader.java
 *    
 *    Copyright (c) 2004, Benja Fallenstein
 *    This file is part of Alph.
 *    
 *    Alph is free software; you can redistribute it and/or modify it under
 *    the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *    
 *    Alph is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 *    Public License for more details.
 *    
 *    You should have received a copy of the GNU General
 *    Public License along with Alph; if not, write to the Free
 *    Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *    MA  02111-1307  USA
 *    
 */
/*
 * Written by Benja Fallenstein
 */
package org.nongnu.alph.xml;
import org.nongnu.alph.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TIMLReader extends DefaultHandler  {
    protected static String timl = "http://fenfire.org/xmlns/2003/09/tstring#";

    public static final TIMLReader instance = new TIMLReader();

    protected TString read = TString.newFake("");

    protected boolean insideDocument = false, insideSpan = false;
    
    protected String spanURI = null;
    protected int spanOffset = 0;

    protected SAXParser parser = null;

    protected void createParser() throws SAXException {
	if(parser == null) {
	    try {
		SAXParserFactory f = SAXParserFactory.newInstance();
		f.setNamespaceAware(true);
		parser = f.newSAXParser();
	    } catch(ParserConfigurationException e) {
		e.printStackTrace();
		throw new SAXException(""+e);
	    }
	}
    }
    
    public TString read(InputStream in) throws SAXException, IOException {
	createParser();
	parser.parse(in, this);
	return read;
    }

    public TString read(String s) throws SAXException {
	try {
	    InputStream in = new ByteArrayInputStream(s.getBytes("UTF-8"));
	    return read(in);
	} catch(IOException e) {
	    // unexpected
	    e.printStackTrace();
	    throw new Error(""+e);
	}	
    }

    /** Return the TString read so far.
     */
    public TString getTString() { 
	return read; 
    }

    public void startDocument() {
	insideDocument = false;
	read = TString.newFake("");
    }

    public void startElement(String namespaceURI, String localName, 
			     String qname, Attributes atts) 
	throws SAXException {

	if(insideSpan)
	    throw new SAXException("Nested element inside span");

	if(!insideDocument && namespaceURI.equals(timl) &&
	   localName.equals("tstring")) {

	    insideDocument = true;

	} else if(insideDocument && namespaceURI.equals(timl) &&
		  localName.equals("tspan")) {

	    spanURI = atts.getValue(timl, "uri");
	    spanOffset = Integer.parseInt(atts.getValue(timl, "offs"));
	    insideSpan = true;
	} else {
	    throw new SAXException("Unexpected element: "+qname);
	}
    }

    public void endElement(String namespaceURI, String localName, 
			   String qname) {

	if(insideSpan) insideSpan = false;
	else insideDocument = false;
    }

    public void characters(char[] arr, int start, int length) {
	if(length == 0) return;

	String s = new String(arr, start, length);
	if(insideSpan) {
	    read = read.plus(new TString(s, spanURI, spanOffset));
	    spanOffset += length;
	} else {
	    read = read.plus(TString.newFake(s));
	}
    }
}
