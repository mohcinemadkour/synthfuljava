/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 * @author Blessed Geek
 * 
 * SynFile.java
 *
 * Created on September 8, 2003, 12:34 PM
 */

package org.synthful.io;
import java.io.File;
import java.net.URI;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;

public class SynFile
extends File
{
    
    public SynFile(String filename)
    {
        super(filename);
    }
    public SynFile(String parent,String filename)
    {
        super(parent,filename);
    }
    public SynFile(File parent,String filename)
    {
        super(parent,filename);
    }
    
    /**
	 * Instantiates a new SynFile.
	 * 
	 * @param uri
	 *            the uri
	 */
    public SynFile(URI uri)
    {
        super(uri);
    }
    
    /**
	 * Gets the PrintWriter.
	 * 
	 * @return the PrintWriter
	 * 
	 * @throws FileNotFoundException
	 */
    public PrintWriter getPrintWriter()
    throws java.io.FileNotFoundException
    {
        FileOutputStream cfout = new FileOutputStream(this);
        BufferedOutputStream cfbuf = new BufferedOutputStream(cfout);
        PrintWriter cfprn = new PrintWriter(cfbuf);
        return cfprn;
    }
    
}
