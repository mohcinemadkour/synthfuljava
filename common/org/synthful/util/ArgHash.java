/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import org.synthful.util.HashTreeNode;
import org.synthful.util.VectorNode;

/**
 * ArgHash Class.
 */
public class ArgHash
	extends HashTreeNode
{
	
	/**
	 * Instantiates a new ArgHash.
	 * 
	 * @param args
	 *            of the format key=value pair
	 */
	public ArgHash(String[] args)
	{
		for (int i = 0; i < args.length; i++)
		{
			String[] arg = args[i].split("=");
			if (arg == null || arg.length == 0)
				continue;
			String argkey = arg[0];

			String argval
				= (arg.length <= 1)
				? null
				: arg[1];

			put(arg[0], arg[1]);
		}
	}

	/**
	 * Sets the required args.
	 * 
	 * @param argkeys
	 * 
	 * @return this after registering the required args
	 */
	public ArgHash setRequiredArgs(String[][] argkeys)
	{
		RequiredArgs = new HashTreeNode(argkeys);

		return this;
	}

	/**
	 * Sets the optional args.
	 * 
	 * @param argkeys
	 * 
	 * @return this after registering the optional args
	 */
	public ArgHash setOptionalArgs(String[][] argkeys)
	{
		OptionalArgs = new HashTreeNode(argkeys);
		return this;
	}

	/**
	 * Verify required args.
	 * 
	 * @param argkeys
	 * 
	 * @return missing required arg keys as VectorNode
	 */
	public VectorNode verifyRequiredArgs(String[][] argkeys)
	{
		RequiredArgs = new HashTreeNode(argkeys);
		AbsentArgs = new VectorNode();

		for (int i = 0; i < RequiredArgs.size(); i++)
		{
			Object k = RequiredArgs.getKey(i);
			if (k != null && !containsKey(k))
				AbsentArgs.add(k);
		}

		return AbsentArgs;
	}

	/**
	 * Help.
	 * 
	 * @return Help as String
	 */
	public String Help()
	{
		return "" + ListArgs(RequiredArgs) + ListArgs(OptionalArgs);
	}

	static private StringBuffer ListArgs(HashTreeNode h)
	{
		StringBuffer sb = new StringBuffer();
		if (h != null)
			for (int i = 0; i < h.size(); i++)
			{
				String argkey = "" + h.getKey(i);
				Object argx = h.get(i);
				sb.append(argkey);
				sb.append("=<");
				sb.append(argx == null ? argkey : argx);
				sb.append("> ");
			}

		return sb;
	}

	protected HashTreeNode RequiredArgs;
	
	protected HashTreeNode OptionalArgs;
	
	protected VectorNode AbsentArgs;
}