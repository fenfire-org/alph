// (c) Tuomas J. Lukka and Benja Fallenstein

package org.nongnu.alph;

/** (INCOMPLETE) A Transcludable String - the most important user-visible
 * class in Alph Lite.
 * A transcludable replacement for the java.lang.String object.
 * <p>
 * Time guarantees for a string of length L (in spans): 
 * creating a substring of length k 
 * of a string of length L is O(k + log(L)). charAt is O(log(L)).
 */
public final class TString {

    static private TString empty = new TString("", new String[] {},
					       new int[] {}, new int[] {});

    /** The URIs of the spans that this TString consists of.
     * All elements of this array are interned.
     * <code>null</code> indicates a fake text span.
     * INVARIANT: no adjacent spans can be append()ed to each other.
     */
    private final String[] spanURIs;

    /** The start indices of the spans.
     */
    private final int[] spanStarts;
   
    /** The end indices of the spans.
     */
    private final int[] spanEnds;
   
    /** The offsets of the spans inside the string.
     * Length = spanURIs.length + 1.
     * It holds that offsets[i+1] - offsets[i] = spanEnds[i] - spanStarts[i].
     */
    private final int[] offsets;

    /** The textual content of this TString.
     */
    private final String content;

    /** Private constructor for TString.
     * The passed arrays are stored in this object, 
     * and must not be modified.
     * Used like this because this is a private constructor.
     */
    private TString(String content, String[] spanURIs,
		    int[] spanStarts, int[] spanEnds) {
	if(spanURIs.length != spanStarts.length ||
	   spanURIs.length != spanEnds.length)
	    throw new IllegalArgumentException("Arrays aren't of equal length: " + spanURIs.length + " " +spanStarts.length + " " + spanEnds.length);

	this.content = content;
	this.spanURIs = spanURIs;
	this.spanStarts = spanStarts;
	this.spanEnds = spanEnds;

	this.offsets = new int[spanURIs.length + 1];

	for(int i=0; i<spanURIs.length; i++) {
	    this.offsets[i+1] = this.offsets[i] + (spanEnds[i]-spanStarts[i]);
	    if(spanEnds[i] <= spanStarts[i])
		throw new IllegalArgumentException("Span end isn't larger than span start: "+spanEnds[i] + " <= " + spanStarts[i] + " (tstring=" + repr() + ")");
	}

	if(this.offsets[spanURIs.length] != content.length())
	    throw new IllegalArgumentException("Inconsistent string size: " + this.offsets[spanURIs.length] + " != " + content.length());
    }

    /** Create a TString containing a single span.
     *  @param content The content of the span.
     *  @param uri     The URI the span is associated with. 
     *                 <code>null</code> if this is a fake span.
     *  @param start   The index of the first character in the span.
     *                 Ignored if <code>uri</code> is <code>null</code>.
     */
    public TString(String content, String uri, int start) {
	this.content = content;

	if(content.equals("")) {
	    this.spanURIs = new String[] {};
	    this.spanStarts = this.spanEnds = new int[] {};
	    this.offsets = new int[] { 0 };
	    return;
	}

	if(uri != null) uri   = uri.intern();
	else            start = 0;

	this.spanURIs = new String[] { uri };
	this.spanStarts = new int[] { start };
	this.spanEnds = new int[] { start + content.length() };
	this.offsets = new int[] { 0, content.length() };
    }

    /** Return the index of the span containing
     * the given offset.
     * Offsets between the spans return the index of
     * the span that follows them. 
     * If the offset given is the length of this string,
     * this method will return spans.length.
     * If the offset given is larger than the length of this string
     * or negative, an StringIndexOutOfBoundsException is thrown.
     * For instance, if 
     * we have a TString containing two spans,
     * of lengths 3 and 4, respectively, getSpanIndex
     * shall return 0 0 0 1 1 1 1 2.
     */
    private int getSpanIndex(int offset) {
	if(offset < 0 || offset > offsets[offsets.length-1]) 
	    throw new StringIndexOutOfBoundsException(offset);

	if(offsets.length == 1) return 0;
	
	int lower = 0;
	int upper = offsets.length - 1;
	boolean rienNeVaPlus = false;
	while(true) {
	    int i = lower + (upper-lower)/2;
	    if(offset < offsets[i]) 
		upper = i;
	    else if(offset >= offsets[i+1])
		lower = i;
	    else
		return i; 

	    if(upper == lower+1) {
		if(!rienNeVaPlus) rienNeVaPlus = true;
		else return upper;
	    }
	}	
    }

    /** As String.substring(int).
     */
    public TString substring(int offset) {
	return substring(offset, length());
    }

    /** As String.substring(int, int).
     */
    public TString substring(int offset1, int offset2) {
	if(offset2 < offset1) 
	    throw new StringIndexOutOfBoundsException(""+offset1+" "+offset2);

	int spi1 = getSpanIndex(offset1);
	int spi2 = getSpanIndex(offset2);
	// Exceptions have been thrown
	
	if(offset1 == offset2) return empty;
	if(offset1 == 0 && offset2 == length())
	    return this;

	String newContent = content.substring(offset1, offset2);

	if(spi1 == spi2) {
	    return new TString(newContent, spanURIs[spi1],
			       spanStarts[spi1] + (offset1 - offsets[spi1]));
	}

	int n;

	if(spi2 == spanURIs.length || offsets[spi2] == offset2)
	    // the range ends before the beginning of span spi2;
	    // spi2 will not be in the new string.
	    n = spi2 - spi1;
	else
	    // (part of) spi2 will be in the new string.
	    n = spi2 - spi1 + 1;

	String[] newURIs = new String[n];
	int[] newStarts  = new int[n];
	int[] newEnds = new int[n];

	System.arraycopy(spanURIs, spi1, newURIs, 0, n);

	newStarts[0] = spanStarts[spi1] + offset1 - offsets[spi1];
	System.arraycopy(spanStarts, spi1+1, newStarts, 1, n-1);

	System.arraycopy(spanEnds, spi1, newEnds, 0, n-1);
	
	if(spi2 == spanURIs.length || offsets[spi2] == offset2)
	    newEnds[n-1] = spanEnds[spi2-1];
	else
	    newEnds[n-1] = spanStarts[spi2] + offset2 - offsets[spi2];

	return new TString(newContent, newURIs, newStarts, newEnds);
    }

    /** As String.charAt(int).
     */
    public char charAt(int offset) {
	return content.charAt(offset);
    }

    /** As String.length().
     */
    public int length() {
	return offsets[offsets.length-1];
    }

    /** Return this TString as a non-transcludable string.
     */
    public String toString() {
	return content;
    }

    public boolean equals(Object o) {
	if(!(o instanceof TString)) return false;
	return equals((TString)o);
    }

    public boolean equals(TString o) {
	if(spanURIs.length != o.spanURIs.length) return false;
	for(int i=0; i<spanURIs.length; i++)
	    if(spanURIs[i] != o.spanURIs[i] ||
	       spanStarts[i] != o.spanStarts[i] ||
	       spanEnds[i] != o.spanEnds[i])
		return false;

	return content.equals(o.content);
    }

    protected int hashCode = 0;

    public int hashCode() {
	if(hashCode==0) {
	    int result = content.hashCode();
	    for(int i=0; i<spanURIs.length; i++) {
		result += offsets[i];
		if(spanURIs[i] != null) {
		    result ^= spanURIs[i].hashCode();
		    result *= spanStarts[i];
		}
	    }

	    if(result==0) result = 1;
	    hashCode = result;
	    return result;
	} else {
	    return hashCode;
	}
    }

// ------------- Access the identities of characters in the string

    /** Get the URI associated with the ith character in the string. */
    public String getCharURI(int i) {
	int sp = getSpanIndex(i);
	return spanURIs[sp];
    }

    /** Get the TIML index of the ith character in the string. */
    public int getCharIndex(int i) {
	int sp = getSpanIndex(i);
	return spanStarts[sp] + (i-offsets[sp]);
    }

// ------------- Access the spans inside the string

    /** Return the number of spans in this TString.
     */
    public int spanCount() {
	return spanURIs.length;
    }

    /** Return the content of the <code>i</code>th span in this TString.
     *  @param i must be >= 0 and < spanCount().
     */
    public String getSpanContent(int i) {
	return content.substring(offsets[i], offsets[i+1]);
    }

    /** Return the URI of the <code>i</code>th span in this TString.
     *  @param i must be >= 0 and < spanCount().
     */
    public String getSpanURI(int i) {
	return spanURIs[i];
    }

    /** Return the start index of the <code>i</code>th span in this TString.
     *  @param i must be >= 0 and < spanCount().
     */
    public int getSpanStart(int i) {
	return spanStarts[i];
    }

// ------------- Structural representation for debug purposes

    public String repr() {
	String r = "<TString ";
	for(int i=0; i<spanURIs.length; i++) {
	    if(i>0) r += ", ";
	    if(spanURIs[i] != null)
		r = r + "('" + content.substring(offsets[i],offsets[i+1]) + 
		    "',<" + spanURIs[i] + ">," + spanStarts[i] + ")";
	    else
		r += "'" + content.substring(offsets[i], offsets[i+1]) + "'";
	}
	r += ">";

	return r;
    }

// ------------- Creating RICC TStrings

    /** The identifier of the namespace to use for RICC spans.
     */
    static private String RICC_id = org.nongnu.storm.util.URN5Namespace.
					  instance.generateId().intern();
    /** The current RICC offset.
     */
    static private int RICC_offset = 0;

    static public TString newRICC(String s) {
	if(s.equals("")) return empty;
	TString res = new TString(s, RICC_id, RICC_offset);
	RICC_offset += s.length();
	return res;
    }

// -------------- Creating Fake TStrings

    static public TString newFake(String s) {
	if(s.equals("")) return empty;
	return new TString(s, null, 0);
    }

// -------------- Appending TStrings

    public TString plus(TString other) {
	return TString.plus(this, other);
    }

    public static TString plus(TString a, TString b) {
	int na = a.spanURIs.length, nb = b.spanURIs.length;

	if(na == 0) return b;
	if(nb == 0) return a;

	String newContent = a.content + b.content;

	boolean isContiguous =
	    a.spanURIs[na-1] == b.spanURIs[0] &&
	    a.spanEnds[na-1] == b.spanStarts[0];

	int n = isContiguous ? na+nb-1 : na+nb;
	
	String[] newURIs = new String[n];
	int[] newStarts = new int[n];
	int[] newEnds = new int[n];
	
	System.arraycopy(a.spanURIs, 0, newURIs, 0, na);
	System.arraycopy(b.spanURIs, 0, newURIs, n-nb, nb);

	System.arraycopy(a.spanStarts, 0, newStarts, 0, na);
	System.arraycopy(b.spanStarts, 0, newStarts, n-nb, nb);

	System.arraycopy(a.spanEnds, 0, newEnds, 0, na);
	System.arraycopy(b.spanEnds, 0, newEnds, n-nb, nb);

	if(isContiguous) {
	    newStarts[na-1] = a.spanStarts[na-1];
	    newEnds[na-1]   = b.spanEnds[0];
	}

	return new TString(newContent, newURIs, newStarts, newEnds);
    }

    public static TString plus(TString a, TString b, TString c) {
	return plus(plus(a, b), c);
    }

// -------------- XML serialization

    public String toXML() {
	return "<timl:tstring xmlns:timl=\"" +
	    "http://fenfire.org/xmlns/2003/09/tstring#\">" +
	    toXMLFragment("timl") +
	    "</timl:tstring>";
    }

    public String toXMLFragment(String timlNamespacePrefix) {
	StringBuffer b = new StringBuffer();
	String n = timlNamespacePrefix;

	for(int i=0; i<spanURIs.length; i++) {
	    if(spanURIs[i] != null)
		b.append("<"+n+":tspan "+n+":offs=\""+spanStarts[i]+"\" "+
			 n+":uri=\""+xmlQuote(spanURIs[i])+"\">");

	    b.append(xmlQuote(getSpanContent(i)));

	    if(spanURIs[i] != null)
		b.append("</"+n+":tspan>");
	}

	return b.toString();
    }

    private static String xmlQuote(String s) {
	StringBuffer buf = new StringBuffer();
	for(int i=0; i<s.length(); i++) {
	    switch(s.charAt(i)) {
		case '&': buf.append("&amp;"); break;
	        case '<': buf.append("&lt;"); break;
		case '>': buf.append("&gt;"); break;
	        case '\'': buf.append("&apos;"); break;
		case '\"': buf.append("&quot;"); break;
		default:
			  buf.append(s.charAt(i)); break;
	    }
	}
	return buf.toString();
    }
}
