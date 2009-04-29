/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 */
package org.synthful.io;

import java.util.Map;
import java.io.File;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import org.synthful.util.HashTreeNode;
import org.synthful.util.VectorNode;
import java.io.*;
import org.apache.commons.net.ftp.*;

// TODO: Auto-generated Javadoc
/**
 * RemoteFileNode Class.
 */
public class RemoteFileNode
	extends HashTreeNode
{
	
	/**
	 * Instantiates a new RemoteFileNode.
	 */
	public RemoteFileNode()
	{
	}

	/**
	 * Instantiates a new RemoteFileNode.
	 * 
	 * @param ftpclient
	 *            the ftpclient
	 * @param path
	 *            the path
	 */
	public RemoteFileNode(
		FTPClient ftpclient, String path)
	{
		FtpClientHandle = ftpclient;
    changeWorkingDirectory(path);
	}

	/**
	 * Instantiates a new RemoteFileNode.
	 * 
	 * @param map
	 *            the map
	 */
	public RemoteFileNode(RemoteFileNode map)
	{
		super(map);
	}

	/**
	 * Instantiates a new RemoteFileNode.
	 * 
	 * @param initSz
	 *            the init sz
	 */
	public RemoteFileNode(int initSz)
	{
		super(initSz);
	}

	/**
	 * Instantiates a new RemoteFileNode.
	 * 
	 * @param initSz
	 *            the init sz
	 * @param factor
	 *            the factor
	 */
	public RemoteFileNode(int initSz, float factor)
	{
		super(initSz, factor);
	}

	/* (non-Javadoc)
	 * @see org.synthful.util.HashTreeNode#getKeyDelimiter()
	 */
	public char getKeyDelimiter()
	{
		return KeyDelimiter;
	}

	/**
	 * Instantiates a new RemoteFileNode.
	 * 
	 * @param file
	 *            the file
	 */
	public RemoteFileNode(FTPFile file)
	{
		NodeFile = file;
	}

	/**
	 * Instantiates a new RemoteFileNode.
	 * 
	 * @param name
	 *            the name
	 */
	public RemoteFileNode(String name)
	{
		NodeFile = new FTPFile();
		NodeFile.setName(name);
	}

	/**
	 * Gets the node file.
	 * 
	 * @return the node file
	 */
	public FTPFile getNodeFile()
	{
		return NodeFile;
	}

	/**
	 * Sets the ftp client.
	 * 
	 * @param ftpclient
	 *            the ftpclient
	 * 
	 * @return the remote file node
	 */
	public RemoteFileNode setFTPClient(FTPClient ftpclient)
	{
		FtpClientHandle = ftpclient;
		return this;
	}

	/**
	 * Sets the node file.
	 * 
	 * @param file
	 *            the NodeFile
	 */
	public void setNodeFile(FTPFile file)
	{
		NodeFile = file;
	}

	/**
	 * Sets the node file.
	 * 
	 * @param pathname
	 *            the NodeFile
	 */
	public void setNodeFile(String pathname)
	{
		NodeFile = new FTPFile();
		NodeFile.setName(pathname);
	}

	/**
	 * List file nodes.
	 * 
	 * @param filenamefilter
	 *            the filenamefilter
	 * 
	 * @return the remote file node
	 */
	public RemoteFileNode ListFileNodes(AcceptFile filenamefilter)
	{
		FTPFile[] nodes = null;
		try
		{
			nodes =
				FtpClientHandle.listFiles();
		}
		catch (IOException ex)
		{
		}

		for (int i = 0; i < nodes.length; i++)
			if (filenamefilter.accept(nodes[i]))
				put(nodes[i].getName(), nodes[i]);

		return this;
	}

	/**
	 * List directory nodes.
	 * 
	 * @param dirnamefilter
	 *            the dirnamefilter
	 * @param filenamefilter
	 *            the filenamefilter
	 * 
	 * @return the remote file node
	 */
	public RemoteFileNode ListDirectoryNodes(
		AcceptDirectory dirnamefilter, AcceptFile filenamefilter)
	{
		FTPFile[] nodes = null;
		try
		{
			nodes =
				FtpClientHandle.listFiles();
		}
		catch (IOException ex)
		{
		}

		for (int i = 0; i < nodes.length; i++)
		{
			RemoteFileNode filenode = new RemoteFileNode(nodes[i]);
			if (!dirnamefilter.accept(nodes[i]))
				continue;

			put(nodes[i].getName(), filenode);
			try
			{
				String pwd = FtpClientHandle.printWorkingDirectory();
				File cwd = new File(pwd, nodes[i].getName());
				FtpClientHandle.changeWorkingDirectory(cwd.getAbsolutePath());
			}
			catch (IOException ex1)
			{
			}
			filenode.ListDirectoryNodes(dirnamefilter, filenamefilter);
			filenode.ListFileNodes(filenamefilter);
		}

		return this;
	}

	/**
	 * Change working directory.
	 * 
	 * @param path
	 *            the path
	 * 
	 * @return true, if successful
	 */
	public boolean changeWorkingDirectory(String path)
	{
		return changeWorkingDirectory(FtpClientHandle, path);
	}

	/**
	 * Change working directory.
	 * 
	 * @param ftpclient
	 *            the ftpclient
	 * @param path
	 *            the path
	 * 
	 * @return true, if successful
	 */
	static public boolean changeWorkingDirectory(
		FTPClient ftpclient, String path)
	{
		try
		{
			return ftpclient.changeWorkingDirectory(path);
		}
		catch (IOException ex)
		{
			return false;
		}
	}

	/** The Key delimiter. */
	protected char KeyDelimiter = '/';
	
	/** The Node file. */
	protected FTPFile NodeFile;
	
	/** The Ftp client handle. */
	protected FTPClient FtpClientHandle;
	
	/** The Path. */
	protected String Path;
}
