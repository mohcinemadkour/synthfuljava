package org.synthful.io;

import java.util.Map;
import java.io.File;

import org.synthful.util.HashTreeNode;
import org.synthful.util.VectorNode;
import java.io.*;

public class FileNode
	extends HashTreeNode
{
	public FileNode()
	{
	}

	public FileNode(Map map)
	{
		super(map);
	}

	public FileNode(int initSz)
	{
		super(initSz);
	}

	public FileNode(int initSz, float factor)
	{
		super(initSz, factor);
	}

	public FileNode(File file)
	{
		NodeFile = file;
	}

	public FileNode(String pathname)
	{
		NodeFile = new File(pathname);
	}

	public FileNode(File parent, String name)
	{
		NodeFile = new File(parent, name);
	}

  public char getKeyDelimiter()
  {
    return KeyDelimiter;
  }

	public File getNodeFile()
	{
		return NodeFile;
	}

	public void setNodeFile(File file)
	{
		NodeFile = file;
	}

	public void setNodeFile(String pathname)
	{
		NodeFile = new File(pathname);
	}

	public void setNodeFile(File parent, String name)
	{
		NodeFile = new File(parent, name);
	}

	public FileNode ListFileNodes(AcceptFile filenamefilter)
	{
		File[] nodes = NodeFile.listFiles(filenamefilter);

		for (int i = 0; i < nodes.length; i++)
			put(nodes[i].getName(), nodes[i]);

		return this;
	}

	public FileNode ListDirectoryNodes(
     AcceptDirectory dirnamefilter, AcceptFile filenamefilter)
	{
		File[] nodefiles = NodeFile.listFiles(dirnamefilter);
		try
		{
			System.out.println(NodeFile.getCanonicalPath());
		}
		catch (IOException ex)
		{
		}

		for (int i = 0; i < nodefiles.length; i++)
		{
			FileNode filenode = new FileNode(nodefiles[i]);
			put(nodefiles[i].getName(), filenode);
      filenode.ListDirectoryNodes(dirnamefilter, filenamefilter);
			filenode.ListFileNodes(filenamefilter);
		}

		return this;
	}

	protected char KeyDelimiter = '/';
	protected File NodeFile;
}
