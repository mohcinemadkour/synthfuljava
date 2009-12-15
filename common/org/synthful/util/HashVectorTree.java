package org.synthful.util;

import java.util.Map;

public class HashVectorTree
extends HashVectorTreeNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new HashTreeNode.
	 */
    public HashVectorTree ()
    {
        super ();
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param map
	 */
    public HashVectorTree (Map map)
    {
        super (map);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param t
	 */
    public HashVectorTree (Object[][] t)
    {
        super (t);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 */
    public HashVectorTree (int initSz)
    {
        super (initSz);
    }
    
    
    /**
	 * get stored object by hashkey progression.
     * 
	 * @param key
	 *            is a hash key progression
	 * @return stored tree indexed by key string
     */
    public Object get (String keyStr)
    {
        return this.get(keyStr, this.getKeyDelimiter());
    }
    
    /**
	 * Cut.
	 * 
	 * @param key
	 * 
	 * @return Cut as Object
	 */
	public Object cut(String key)
	{
		return this.cut(key, this.getKeyDelimiter());
	}
	
    /**
	 * 
	 * 
	 * @param key
	 *            dot delimited String hierarchical progression key
	 *            
	 * @param value
	 *            value to be put into hash for key <BR>
	 *            Dot delimited String key has adjacent hierarchy levels
	 *            segregated by dots. <BR>
	 *            e.g. key of "greatest.hits.1980" = value ["Sheena Easton",
	 *            "Robert Palmer"] <BR>
	 *            would locate for, or create if not existent, a key "greatest"
	 *            where its referenced value is a hashtable in which the key
	 *            "hits" is located/created, where in turn, the value of key
	 *            "hits" is a hashtable in which the key "1980" is
	 *            located/created. Since "1980" is the leaf key, the array <BR>
	 *            ["Sheena Easton", "Robert Palmer"] is put() as value of
	 *            "1980".
	 * 
     * @param sepr delimiter of keys in keyStr.
     * 
	 * @return this
	 * 
	 *         Algorithm uses iteration rather than recursion to progress from
	 *         one dot level to the next.
	 */   
    public HashVectorTreeNode put (String key, Object value)
    {
    	return this.put(key, this.getKeyDelimiter(), value);
    }
    
    /**
	 * Gets the object indicated by the second last level key in the key string.
	 * 
     * @param keyStr
	 * @return the ParentOf as Object
     */
    public Object getParentOf (String keyStr)
    {
        return this.getParentOf(keyStr, this.getKeyDelimiter());
    }
    
    /**
	 * 
	 * 
	 * @return the delimiter used for separating key segments.
	 */
    public char getKeyDelimiter ()
    {
        return this.KeyDelimiter;
    }
    
    /**
     * @see org.synthful.util.TreeNode#setKeyDelimiter(char)
     */
    public TreeNode setKeyDelimiter (char delimiter)
    {
        this.KeyDelimiter = delimiter;
        return this;
    }

    /**
	 * Get or otherwise create HashTreeNode indicated by path in keyStr.
     * 
     * @param hashTreeNode
     * @param keyStr the path
	 * @param overWriteLeaf
	 *            over-write a leaf node with a HashTreeNode if it is not a
	 *            HashTreeNode.
	 * @return the HashTreeNode indicated by key sequence, or null if path does
	 *         not exist and overWriteLeaf is false.
     */
	public HashVectorTreeNode setPath(
		HashVectorTreeNode hashTreeNode,
		String keyStr,
		boolean overWriteLeaf)
	{
        return setPath(
        	hashTreeNode,
        	keyStr,
        	this.getKeyDelimiter(),
        	overWriteLeaf);
	}


    /** Variable KeyDelimiter. */
    protected char KeyDelimiter = '/';
    
}
