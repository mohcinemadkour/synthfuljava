/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.synthful.lang.Lang;

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
public class HashTreeNode
extends Hashtable
implements TreeNode, Lang
{
    
    /**
	 * Instantiates a new HashTreeNode.
	 */
    public HashTreeNode ()
    {
        super ();
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param map
	 */
    public HashTreeNode (Map map)
    {
        add (map);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param t
	 */
    public HashTreeNode (Object[][] t)
    {
        add (t);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 */
    public HashTreeNode (int initSz)
    {
        super (initSz);
    }
    
    /**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 * @param factor
	 */
    public HashTreeNode (int initSz, float factor)
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
    public HashTreeNode add (Map map)
    {
        Set keyset = map.keySet ();
        Object[] keys = keyset.toArray ();
        if (keys != null)
            for (int i = 0; i < keys.length; i++)
            {
                Object key = keys[i];
                Object value = map.get (key);
                put ("" + key, value);
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
    public HashTreeNode add (Object[][] t)
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
                : null;
                put ("" + key, value);
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
    public String getString (String key)
    {
        Object o = get (key);
        if (o == null)
            return "";
        return o.toString ();
    }
    
    /**
	 * get stored object by hashkey progression.
	 * 
	 * @param key
	 *            is a hash key progression
	 * 
	 * @return stored object indexed by key
	 */
    public Object get (String key)
    {
        VectorNode myV = new VectorNode (5);
        char sepr = getKeyDelimiter ();
        myV.fromString (key, sepr);
        
        Object myH = this;
        for (int i = 0; i < myV.size (); i++)
        {
            if (! (myH instanceof Hashtable))
                return null;
            
            myH = ( (Hashtable) myH).get (myV.get (i));
            if (myH == null)
                return null;
        }
        return myH;
    }
    
    /**
	 * Gets the ParentOf.
	 * 
	 * @param key
	 * 
	 * @return the ParentOf as Object
	 */
    public Object getParentOf (String key)
    {
        VectorNode myV = new VectorNode (5);
        myV.fromString (key.toString (), KeyDelimiter);
        
        Object myH = this;
        for (int i = 0; i < myV.size () - 1; i++)
        {
            if (! (myH instanceof Hashtable))
                return null;
            
            myH = ( (Hashtable) myH).get (myV.get (i));
            if (myH == null)
                return null;
        }
        return myH;
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
	 * 
	 * 
	 * @return the delimiter used for separating key segments.
	 */
    public char getKeyDelimiter ()
    {
        return KeyDelimiter;
    }
    
    /**
	 * get hash key of stored object by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash vector.
	 * 
	 * @return Hash key of object stored at that position.
	 */
    public Object getKey (int position)
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
    public Vector getKeysVector ()
    {
        return KeysVector;
    }
    
    /**
	 * 
	 * 
	 * @param key
	 *            dot delimited String hierarchical progression key
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
	 * @return this
	 * 
	 *         Algorithm uses iteration rather than recursion to progress from
	 *         one dot level to the next.
	 */
    
    public HashTreeNode put (String key, Object value)
    {
        
        char sepr = getKeyDelimiter ();
        String[] keysegs = key.split ("["+sepr+']');
        
        HashTreeNode myH = this;
        for (int i = 0; i < keysegs.length; i++)
        {
            String mykey = (String) keysegs[i];
            if (mykey == null || mykey.length () == 0)
                continue;
            //If leaf key, put() value
            if (i == keysegs.length - 1)
            {
                myH.putValue (mykey, value);
                return this;
            }
            
            //Otherwise continue locating/creating next level of key
            Object otree = myH.get (mykey);
            HashTreeNode mytree
            = (otree != null && otree instanceof HashTreeNode)
            ? (HashTreeNode) otree
            : null
            ;
            if (mytree == null)
            {
                mytree = new HashTreeNode ();
                myH.putValue (mykey, mytree);
            }
            myH = mytree;
        }
        
        return this;
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
	 * @return this Hash Vector
	 */
    public HashTreeNode clear (String key)
    {
        
        VectorNode myV = new VectorNode (5);
        char sepr = getKeyDelimiter ();
        myV.fromString (key, sepr);
        
        HashTreeNode myH = this;
        for (int i = 0; i < myV.size (); i++)
        {
            String mykey = (String) myV.get (i);
            if (mykey == null || mykey.length () == 0)
                continue;
            //If leaf key, clear
            if (i == myV.size () - 1)
            {
                myH.clear ();
                return this;
            }
            
            //Otherwise continue locating/creating next level of key
            Object otree = myH.get (mykey);
            HashTreeNode mytree
            = (otree != null && otree instanceof HashTreeNode)
            ? (HashTreeNode) otree
            : null
            ;
            if (mytree == null)
                return this;
            myH = mytree;
        }
        
        return this;
    }
    
    /**
	 * Put value.
	 * 
	 * @param key
	 * @param value
	 * 
	 * @return Put value as HashTreeNode
	 */
    protected HashTreeNode putValue (String key, Object value)
    {
        if(value==null)
            value=Empty;
        ( (Hashtable)this).put (key, value);
        KeysVector.add (key);
        if (value instanceof TreeNode)
            ((TreeNode)value).setParentNode (this);
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
	 * Cut.
	 * 
	 * @param key
	 * 
	 * @return Cut as Object
	 */
    public Object cut (String key)
    {
        Object myH = getParentOf (key);
        Object item = get (key);
        
        if (myH.getClass ().isInstance (this))
        {
            VectorNode myV = new VectorNode (5);
            char sepr = getKeyDelimiter ();
            Object[] keys = myV.fromString (key, sepr).toArray ();
            
            ( (Hashtable) myH).remove (keys[keys.length - 1]);
            ( (HashTreeNode) myH).KeysVector.removeElement (keys[keys.length - 1]);
            return item;
        }
        else if (myH instanceof Hashtable)
        {
            ( (Hashtable) myH).remove (key);
            return item;
        }
        else if (myH instanceof Vector)
        {
            ( (Vector) myH).removeElement (key);
            return item;
        }
        return null;
    }
    
    /**
	 * Converts to Array.
	 * 
	 * @return Array as Object[]
	 */
    public Object[] toArray ()
    {
        Object[] ar = new Object[size ()];
        
        for (int i = 0; i < size (); i++)
        {
            ar[i] = get (i);
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
        toStringBuffer tostrbuf = new toStringBuffer ();
        tostrbuf.ItemDelimiter = itemdelimiter;
        tostrbuf.NodeDelimiter = nodedelimiter;
        
        return toStringBuffer (tostrbuf);
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#toStringBuffer(org.synthful.util.toStringBuffer)
     */
    public StringBuffer toStringBuffer (toStringBuffer tostrbuf)
    {
        return tostrbuf.toStringBuffer (this);
    }

    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#toStringBuffer(long)
     */
    public StringBuffer toStringBuffer (long format)
    {
        return new toStringBuffer (format).toStringBuffer (this);
    }

    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#getParentNode()
     */
    public TreeNode getParentNode ()
    {
        return ParentNode;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#getNode(int)
     */
    public TreeNode getNode (int i)
    {
        Object o = get (i);
        if (o instanceof TreeNode)
            return (TreeNode) o;
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#setKeyDelimiter(char)
     */
    public TreeNode setKeyDelimiter (char delimiter)
    {
        KeyDelimiter = delimiter;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.util.TreeNode#setParentNode(org.synthful.util.TreeNode)
     */
    public TreeNode setParentNode (TreeNode node)
    {
        ParentNode = node;
        return this;
    }
    
    /** Variable KeysVector. */
    protected Vector KeysVector = new Vector ();
    
    /** Variable KeyDelimiter. */
    protected char KeyDelimiter = '/';
    
    /** Variable ParentNode. */
    protected TreeNode ParentNode;
}
