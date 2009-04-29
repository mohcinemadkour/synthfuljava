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
 * The Class AcceptDirectory.
 * To be applied as a file filter to accept a file only if it is a folder.
 */
public class AcceptDirectory
	extends ListFilesFilter
{
	
	/**
	 * Instantiates a new AcceptDirectory.
	 */
	public AcceptDirectory()
	{
	}

	/**
	 * Instantiates a new AcceptDirectory.
	 * 
	 * @param namepattern
	 *            the namepattern
	 */
	public AcceptDirectory(String namepattern)
	{
		super(namepattern);
	}

	/* (non-Javadoc)
	 * @see org.synthful.io.ListFilesFilter#accept(java.io.File)
	 */
	public boolean accept(File file)
	{
		if (file == null || !file.isDirectory())
			return false;
		return super.accept(file);
	}

	/* (non-Javadoc)
	 * @see org.synthful.io.ListFilesFilter#accept(org.apache.commons.net.ftp.FTPFile)
	 */
	public boolean accept(FTPFile file)
	{
		if (file == null || !file.isDirectory())
			return false;
		return super.accept(file);
	}

}