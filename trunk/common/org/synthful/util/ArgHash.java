/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import org.synthful.lang.Empty;

/**
 * ArgHash Class.
 */
public class ArgHash
	extends HashVectorTree
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new ArgHash.
	 * 
	 * @param args
	 *            of the format key=value pair
	 */
	public ArgHash(String[] args)
	{
		this.setKeyDelimiter('.');
		this.eatArgs(args, false);
	}
	
	/**
	 * Instantiates a new ArgHash.
	 * 
	 * @param args
	 *            of the format key=value pair
	 * @param keyDelimiter
	 * 		Facilitates storage of hierarchical arguments. e.g.,<br>
	 * 		argA.cfg.path=path1 -> keyDelimiter is '.'<br>
	 * 		argA:cfg:path=path1 -> keyDelimiter is ':'<br>
	 */
	public ArgHash(String[] args, char keyDelimiter)
	{
		this.setKeyDelimiter(keyDelimiter);
		this.eatArgs(args, false);
	}
	
	/**
	 * Instantiates a new ArgHash.
	 * 
	 * @param args
	 *            of the format key=value pair
	 * @param keyDelimiter
	 * 		Facilitates storage of hierarchical arguments. e.g.,<br>
	 * 		argA.cfg.path=path1 -> keyDelimiter is '.'<br>
	 * 		argA:cfg:path=path1 -> keyDelimiter is ':'<br>
	 * 
	 * @param multivalueDelimiter
	 * 		argA.names=ramu,jeya,amad -> multivalueDelimiter is ','<br>
	 */
	public ArgHash(String[] args, char keyDelimiter, char multivalueDelimiter)
	{
		this.MultivalueDelimiter = ""+multivalueDelimiter;
		this.setKeyDelimiter(keyDelimiter);
		this.eatArgs(args, true);
	}
	
	public void eatArgs(String[] args, boolean multivalue)
	{
		for (int i = 0; i < args.length; i++)
		{
			String[] arg = args[i].split(this.KeyValueSeparator);
			if (arg == null || arg.length == 0)
				continue;
			String argkey = arg[0];

			String argval
				= (arg.length <= 1)
				? Empty.Blank
				: arg[1];
			
			if (multivalue && argval!=Empty.Blank)
			{
				String[] argvals = arg[1].split(this.MultivalueDelimiter);
				this.put(argkey, argvals);
			}
			else
				this.put(argkey, argval);
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
		RequiredArgs = new HashVectorTreeNode(argkeys);

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
		OptionalArgs = new HashVectorTreeNode(argkeys);
		return this;
	}

	/**
	 * Verify required args.
	 * 
	 * @param argkeys
	 * 
	 * @return missing required arg keys as VectorNode
	 */
	public VectorNode<String> verifyRequiredArgs(String[][] argkeys)
	{
		RequiredArgs = new HashVectorTreeNode(argkeys);
		AbsentArgs = new VectorNode<String>();

		for (int i = 0; i < RequiredArgs.size(); i++)
		{
			String k = RequiredArgs.getKey(i);
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

	static private StringBuffer ListArgs(HashVectorTreeNode h)
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

	protected HashVectorTreeNode RequiredArgs;
	
	protected HashVectorTreeNode OptionalArgs;
	
	protected VectorNode<String> AbsentArgs;
	
	protected String MultivalueDelimiter = ",";
	
	protected String KeyValueSeparator = "=";
	
	protected String ArgumentSeparator = " ";
}