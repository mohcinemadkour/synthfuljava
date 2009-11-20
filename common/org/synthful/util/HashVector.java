/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.synthful.lang.Empty.EmptyFactory;
import org.synthful.util.ToStringBuffer.Fillers;
import org.synthful.util.ToStringBuffer.ToStringBufferable;

/**
 * HashTreeNode Class. Is a combination of Hashtable and Vector extended to
 * allow instances to be hooked up to form a network or a tree. Extended to
 * allow objects to be stored or retrieved using hash key progressions. A
 * hashkey progression is of the String form denoting a hierarchy
 * "keya/keyb/keyc" where "keya" is a hash key leading to another HashVector,
 * which in turn has a hash key leading to yet another HashVector, which then
 * has a hash key leading to the desired location of retrieval/storage. Also
 * allows storage and retrieval by position.
 */
public class HashVector
extends Hashtable<String, Object>
implements TreeNode, EmptyFactory, ToStringBufferable
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new HashTreeNode.
	 */
    public HashVector ()
    {
        super ();
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param map
	 */
    public HashVector (Map map)
    {
        add (map);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param t
	 */
    public HashVector (Object[][] t)
    {
        add (t);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 */
    public HashVector (int initSz)
    {
        super (initSz);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 * @param factor
	 */
    public HashVector (int initSz, float factor)
    {
        super (initSz, factor);
    }
    
    /**
	 * Adds the.
	 * 
	 * @param map
	 * 
	 * @return Adds the as HashTreeNode
	 */
    public HashVector add (Map map)
    {
        Set keyset = map.keySet ();
        Object[] keys = keyset.toArray ();
        if (keys != null)
            for (int i = 0; i < keys.length; i++)
            {
                Object key = keys[i];
                Object value = map.get (key);
                if (value==null)
                	value = EmptyFactory.EMPTY;
                this.put ("" + key, value);
            }
        
        return this;
    }
    
    /**
	 * Adds the.
	 * 
	 * @param t
	 * 
	 * @return Adds the as HashTreeNode
	 */
    public HashVector add (Object[][] t)
    {
        if (t != null)
            for (int i = 0; i < t.length; i++)
            {
                if (t[i] == null || t[i].length < 1)
                    continue;
                Object key = t[i][0];
                Object value
	                = t[i].length >= 2
	                ? t[i][1]
	                : EmptyFactory.EMPTY;
                this.put ("" + key, value);
            }
        
        return this;
    }
    
    /**
	 * Gets the String.
	 * 
	 * @param key
	 * 
	 * @return the String as String
	 */
    public String getString (String key, char sepr)
    {
        Object o = this.get (key, sepr);
        if (o == null)
            return "";
        return o.toString ();
    }
    
    /**
	 * get stored object by hashkey progression.
     * 
	 * @param key
	 *            is a hash key progression
     * @param sepr
	 * @return stored tree indexed by key string
     */
    public Object get (String keyStr, char sepr)
    {
        String[] keyArray = keyStr.split ("["+sepr+']');
        
        return get(keyArray, this);
    }
    
    /**
     * 
     * @param myV
     * @return stored tree indexed by keys in vector
     */
    static public Object get (String[] keyArray, Hashtable h)
    {
        Object myH = h;
		for (int i = 0; i < keyArray.length; i++)
        {
            if (! (myH instanceof Hashtable))
                return null;
            
            myH = ( (Hashtable) myH).get (keyArray[i]);
            if (myH == null)
                return null;
        }
        return myH;
    }
    
    /**
	 * Gets the object indicated by the second last level key in the key string.
	 * 
     * @param keyStr
     * @param sepr delimiter of keys in keyStr.
	 * @return the ParentOf as Object
     */
    public HashVector getParentOf (String keyStr, char sepr)
    {
        String[] keyArray = keyStr.split ("["+sepr+']');
        if (keyArray.length>0) keyArray[keyArray.length-1] = null;
        
        Object o = get(keyArray, this);
        if (o instanceof HashVector)
        	return (HashVector)o;
        else
        	return null;
    }
    
    /**
	 * get stored object by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash vector.
	 * 
	 * @return Object stored at that position.
	 */
    public Object get (int position)
    {
        if (position < KeysVector.size () && position >= 0)
            return get (KeysVector.get (position));
        
        return null;
    }
    
    /**
	 * get hash key of stored object by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash vector.
	 * 
	 * @return Hash key of object stored at that position.
	 */
    public String getKey (int position)
    {
        if (position < KeysVector.size () && position >= 0)
            return KeysVector.get (position);
        
        return null;
    }
    
    /**
	 * Gets the KeyPosition.
	 * 
	 * @param key
	 * 
	 * @return the KeyPosition as int
	 */
    public int getKeyPosition (Object key)
    {
        for (int i = 0; i < KeysVector.size (); i++)
            if (key.equals (KeysVector.get (i)))
                return i;
        return -1;
    }
    
    /**
	 * Gets the KeysVector.
	 * 
	 * @return the KeysVector as Vector
	 */
    public Vector<String> getKeysVector ()
    {
        return KeysVector;
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
	 * @return the parent HashTreeNode of the inserted object
	 * 
	 *         Algorithm uses iteration rather than recursion to progress from
	 *         one dot level to the next.
	 */   
    public HashVector put (String key, char sepr, Object value)
    {
        
        //char sepr = getKeyDelimiter ();
        String[] keyArray = key.split ("["+sepr+']');
        String leafKey = keyArray[keyArray.length-1];
        keyArray[keyArray.length-1] = null;
        
        HashVector myH = setPath(this, keyArray, true);
        myH.putValue(leafKey, value);
        
        return myH;
    }
    
    /**
	 * Locate Hash Vector addressed by key and clear it of all members. Not that
	 * this clears the located member of any sub-members but does not remove the
	 * member itself. To remove the whole branch including the branch node, use
	 * cut.
	 * 
	 * @param key
	 *            Hash key of hash branch to be cleared.
	 * 
     * @param sepr delimiter of keys in keyStr.
     * 
	 * @return this Hash Vector
	 */
    public HashVector clear (String keyStr, char sepr)
    {
    	HashVector p = this.getParentOf(keyStr, sepr);
    	if (p!=null)
    		p.clear();
        return this;
    }
    
    /* (non-Javadoc)
     * @see java.util.Hashtable#clear()
     */
    public void clear ()
    {
        super.clear ();
        KeysVector.clear ();
    }
    
    /**
	 * Put value.
	 * 
	 * @param key
	 * @param value
	 * 
	 * @return Put value as HashTreeNode
	 */
    protected HashVector putValue (String key, Object value)
    {
        if(key==null)
            key=EMPTY.toString();
        if(value==null)
            value=EMPTY;
        ( (Hashtable<String, Object>)this).put (key, value);
        KeysVector.add (key);
        if (value instanceof TreeNode)
            ((TreeNode)value).setParentNode (this);
        return this;
    }
    
    /**
	 * Cut.
	 * 
	 * @param key
     * @param sepr delimiter of keys in keyStr.
	 * 
	 * @return Cut as Object
	 */
	public Object cut(
		String key, char sepr)
	{
		// char sepr = getKeyDelimiter ();
		Object myH = getParentOf(key, sepr);
		Object item = get(key, sepr);
		if (myH == null || myH == EMPTY)
			return null;

		if (myH.getClass().isInstance(this))
		{
	        String[] keys = key.split ("["+sepr+']');
	        String leafKey = keys[keys.length-1];

	        //remove item from hashtable of parent
			((Hashtable<String, Object>) myH).remove(leafKey);
	        //remove item from KeysVector of parent
			((HashVector) myH).KeysVector.removeElement(leafKey);
			
			if (item!=null || item.getClass().isInstance(this))
			{
				// reset parent ref of item
				((HashVector)item).setParentNode(DefaultTree);
			}
			
			return item;
		}
		else if (myH instanceof Hashtable)
		{
			((Hashtable) myH).remove(key);
			return item;
		}
		else if (myH instanceof Vector)
		{
			((Vector) myH).removeElement(key);
			return item;
		}
		return null;
	}

    /**
	 * Get or otherwise create HashTreeNode indicated by path in keyStr.
     * 
     * @param hashTreeNode
     * @param keyStr the path
     * @param sepr delimiter of keys in keyStr.
	 * @param overWriteLeaf
	 *            over-write a leaf node with a HashTreeNode if it is not a
	 *            HashTreeNode.
	 * @return the HashTreeNode indicated by key sequence, or null if path does
	 *         not exist and overWriteLeaf is false.
     */
	static public HashVector setPath(
		HashVector hashTreeNode,
		String keyStr,
		char sepr,
		boolean overWriteLeaf)
	{
        VectorNode<String> myV = new VectorNode<String> (5);
        myV.fromString (keyStr, sepr);
        String[] keyArray = (String[])myV.toArray();
 		
        return setPath(hashTreeNode, keyArray, overWriteLeaf);
	}

    
	/**
	 * Get or otherwise create HashTreeNode indicated by hierarchical sequence
	 * of keys.
	 * 
	 * @param keyArray
	 *            Array containing hierarchical sequence of keys.
	 * @param overWriteLeaf
	 *            over-write a leaf node with a HashTreeNode if it is not a
	 *            HashTreeNode.
	 * @return the HashTreeNode indicated by key sequence, or null if path does
	 *         not exist and overWriteLeaf is false.
	 */
	static public HashVector setPath(
		HashVector hashTreeNode,
		String[] keyArray,
		boolean overWriteLeaf)
	{
		// HashTreeNode myH = this;
		for (int i = 0; i < keyArray.length; i++)
		{
			if (keyArray[i] == null || keyArray[i].length() == 0) continue;

			Object otree = hashTreeNode.get(keyArray[i]);

			if (otree == null)
			{
				HashVector h = new HashVector();
				hashTreeNode.put(keyArray[i], h);
				hashTreeNode = h;
			}
			else if (otree instanceof HashVector)
			{
				hashTreeNode = (HashVector) otree;
			}
			else if (overWriteLeaf)
			{
				HashVector h = new HashVector();
				hashTreeNode.put(keyArray[i], h);
				hashTreeNode = h;
			}
			else
			{
				return null;
			}
		}

		return hashTreeNode;
	}
    
    /**
	 * Converts to Array.
	 * 
	 * @return Array as Object[]
	 */
    public Object[][] toArray ()
    {
        Object[][] ar = new Object[size ()][2];
        
        for (int i = 0; i < size (); i++)
        {
            ar[i][0] = this.getKey (i);
            ar[i][1] = this.get (i);
        }
        
        return ar;
    }
    
    /**
	 * Converts to Vector.
	 * 
	 * @param v
	 * 
	 * @return Vector as VectorNode
	 */
    public VectorNode toVector (VectorNode v)
    {
        for (int i = 0; i < size (); i++)
        {
            v.add (get (i));
        }
        
        return v;
    }
    
    /**
	 * Converts to Vector.
	 * 
	 * @return Vector as VectorNode
	 */
    public VectorNode toVector ()
    {
        return toVector (new VectorNode (size ()));
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#toString(java.lang.String, java.lang.String)
     */
    public String toString (
        String itemdelimiter, String nodedelimiter)
    {
        return "" + toStringBuffer (itemdelimiter, nodedelimiter);
    }
    
    /**
	 * Converts to StringBuffer.
	 * 
	 * @param itemdelimiter
	 * @param nodedelimiter
	 * 
	 * @return StringBuffer as StringBuffer
	 */
    public StringBuffer toStringBuffer (
    String itemdelimiter, String nodedelimiter)
    {
    	ToStringBuffer tostrbuf = new ToStringBuffer ();
        tostrbuf.ItemDelimiter = itemdelimiter;
        tostrbuf.NodeDelimiter = nodedelimiter;
        
        return this.toStringBuffer (tostrbuf, 0, 0);
    }
    
    public StringBuffer toStringBuffer (ToStringBuffer tostrbuf, int depth, int iteration)
    {
        StringBuffer indent = tostrbuf.mkIndentation(depth);
        StringBuffer strBuf = tostrbuf.getStringBuffer();
        
        tostrbuf.appendFillers(Fillers.NodeTerminatorLeft, depth);
        
        for (int i = 0; i < this.size (); i++)
        {
	        if (i > 0)
	        	tostrbuf.appendFillers (Fillers.NodeDelimiter, depth);
            
            strBuf.append (this.getKey (i));
            tostrbuf.appendFillers(Fillers.PairSeparator, depth);

            tostrbuf.toStringBuffer (this.get (i), depth+1, i);
        }
        
        tostrbuf.appendFillers(Fillers.NodeTerminatorRight, depth);
        
		return strBuf;

    }
    /**
     * @see org.synthful.util.TreeNode#getParentNode()
     */
    public TreeNode getParentNode ()
    {
        return ParentNode;
    }
    
    /**
     * @see org.synthful.util.TreeNode#getNode(int)
     */
    public TreeNode getNode (int i)
    {
        Object o = get (i);
        if (o instanceof TreeNode)
            return (TreeNode) o;
        return null;
    }
    
    
    /**
     * @see org.synthful.util.TreeNode#setParentNode(org.synthful.util.TreeNode)
     */
    public TreeNode setParentNode (TreeNode node)
    {
        ParentNode = node;
        return this;
    }
    
    
    
    /** Variable KeysVector. */
    protected Vector<String> KeysVector = new Vector<String> ();
    
    /** Variable ParentNode. */
    protected TreeNode ParentNode;
    
    static protected HashVectorTree DefaultTree = new HashVectorTree();
}
