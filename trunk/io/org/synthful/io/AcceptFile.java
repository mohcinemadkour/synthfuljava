/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 */
package org.synthful.io;

import java.io.FileFilter;
import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

// TODO: Auto-generated Javadoc
/**
 * AcceptFile Class.
 */
public class AcceptFile
	extends ListFilesFilter
{
    
    /**
	 * Instantiates a new AcceptFile.
	 */
    public AcceptFile()
    {
    }

    /**
	 * Instantiates a new AcceptFile.
	 * 
	 * @param namepattern
	 *            the namepattern
	 */
    public AcceptFile(String namepattern)
    {
        super(namepattern);
    }

    /* (non-Javadoc)
     * @see org.synthful.io.ListFilesFilter#accept(java.io.File)
     */
    public boolean accept(File file)
    {
        if (file == null || !file.isFile())
            return false;
        return super.accept(file);
    }

    /* (non-Javadoc)
     * @see org.synthful.io.ListFilesFilter#accept(org.apache.commons.net.ftp.FTPFile)
     */
    public boolean accept(FTPFile file)
    {
        if (file == null || !file.isFile())
            return false;
        return super.accept(file);
    }
}