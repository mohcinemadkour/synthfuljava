/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import org.synthful.io.AcceptDirectory;
import org.synthful.io.AcceptFile;
import org.synthful.io.FileNode;
import org.synthful.io.RemoteFileNode;
import org.synthful.util.ArgHash;
import java.io.*;

/**
 * FileSync Class.
 * TBD: Incomplete. Do not use.
 */
public class FileSync
{
	
	/**
	 * Instantiates a new FileSync.
	 */
	public FileSync()
	{

	}

	static private boolean getArgs(String[] args)
	{
		ArgHash = new ArgHash(args);
		String[][] RequiredArgs =
			{
			{
			"source", "local|remote"}
			,
			{
			"local.path"}
			,
			{
			"remote.host"}
			,
			{
			"remote.user"}
			,
			{
			"remote.password"}
			,
			{
			"remote.path"}
		};

		VectorNode absentargs =
			ArgHash.verifyRequiredArgs(RequiredArgs);
		if (absentargs.size() > 1)
		{
			System.out.println(
				"Arguments required for:\n" +
				absentargs.toString(",\n ", "\n") +
				"Usage:\n" +
				ArgHash.Help()
				);

			return false;
		}

		return true;
	}

	/**
	 * Gets the FTPClient.
	 * 
	 * @param ArgHash
	 * 
	 * @return the FTPClient as FTPClient
	 */
	static public FTPClient getFTPClient(ArgHash ArgHash)
	{
		FTPClient ftpclient = new FTPClient();
		try
		{
			ftpclient.connect("" + ArgHash.get("remote.host"), 80);
			ftpclient.login(
				"" + ArgHash.get("remote.user"),
				"" + ArgHash.get("remote.password"));
			return ftpclient;
		}
		catch (IOException ex)
		{
			return null;
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		if (!getArgs(args))
			System.exit(2);

		FileNode rootnode = new FileNode("" + ArgHash.get("local.path"));
		rootnode.ListDirectoryNodes(isDirectory, isFile);

		FTPClient ftpclient = getFTPClient(ArgHash);
		if (ftpclient!=null)
		{
			RemoteFileNode ftproot =
				new RemoteFileNode(
				ftpclient, "" + ArgHash.get("remote.path"));
		}

		System.out.println(rootnode.toString(",\n ", "\n"));

//		FileSync fileSync1 = new FileSync();
	}

	/** Variable ArgHash. */
	static ArgHash ArgHash;
	
	/** Variable isDirectory. */
	static AcceptDirectory isDirectory = new AcceptDirectory();
	
	/** Variable isFile. */
	static AcceptFile isFile = new AcceptFile();
}