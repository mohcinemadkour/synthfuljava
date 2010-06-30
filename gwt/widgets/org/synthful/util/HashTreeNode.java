/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HashTreeNode Class. Is a combination of HashMap and List extended to allow
 * instances to be hooked up to form a network or a tree. Extended to allow
 * objects to be stored or retrieved using hash key progressions. A hashkey
 * progression is of the String form denoting a hierarchy "keya/keyb/keyc" where
 * "keya" is a hash key leading to another HashVector, which in turn has a hash
 * key leading to yet another HashVector, which then has a hash key leading to
 * the desired location of retrieval/storage. Also allows storage and retrieval
 * by position.
 */
@SuppressWarnings("serial")
public class HashTreeNode<V>
extends HashMapTreeNode<String, V>{

	/**
	 * Instantiates a new HashTreeNode.
	 */
	public HashTreeNode() {
		super();
	}

	/**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 */
	public HashTreeNode(int initSz) {
		super(initSz);
	}
	
	/**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 * @param factor
	 */
	public HashTreeNode(int initSz, float factor) {
		super(initSz, factor);
	}

	/**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param map
	 */
	public HashTreeNode(Map<? extends String, ? extends V> map) {
		add(map);
	}


	/**
	 * Adds the.
	 * 
	 * @param map
	 * 
	 * @return Adds the as HashTreeNode
	 */
	public HashTreeNode<V> add(Map<? extends String, ? extends V> map) {
		super.add(map);
		return this;
	}

	/**
	 * get stored object by hashkey progression.
	 * 
	 * @param key
	 *            is a hash key progression
	 * 
	 * @return stored object indexed by key
	 */
	public V get(String key) {
		List<String> myV = fromString(key, ""+KeyDelimiter);
		String[] a = null;
		String[] keysegs = myV.toArray(a);

		TreeNode<String,  V> t = super.get(keysegs);
		return t.getValue();
	}

	/**
	 * Gets the ParentOf.
	 * 
	 * @param key
	 * 
	 * @return the ParentOf as Object
	 */
	public HashMapTreeNode<String, V> getParentOf(String key) {
		List<String> myV = fromString(key, ""+KeyDelimiter);
		String[] a = null;
		String[] keysegs = myV.toArray(a);
		
		return super.getParentOf(keysegs);
	}

	/**
	 * 
	 * 
	 * @return the delimiter used for separating key segments.
	 */
	public char getKeyDelimiter() {
		return KeyDelimiter;
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

	public HashTreeNode<V> put(String key, V value) {

		char sepr = getKeyDelimiter();
		String[] keysegs = key.split("[" + sepr + ']');
		super.put(keysegs, value);

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
	protected HashTreeNode<V> putValue(String key, V value) {
		return put(key, value);
	}

	/**
	 * Cut.
	 * 
	 * @param key
	 * 
	 * @return Cut as Object
	 */
	public TreeNode<String, V> cut(String key) {
		char sepr = getKeyDelimiter();
		String[] keysegs = key.split("[" + sepr + ']');
		return super.cut(keysegs);
	}
	/**
	 * @see org.synthful.util.HashTreeNode<String, V>#setKeyDelimiter(char)
	 */
	public HashTreeNode<V> setKeyDelimiter(char delimiter) {
		KeyDelimiter = delimiter;
		return this;
	}

    public List<String> fromString (String s, String sSeparator)
    {
		List<String> myV = new ArrayList<String>();
        if (s == null)
            return myV;
        String[] ss = s.split (sSeparator);
        for(String s1:ss)
        	myV.add(s1);
        return myV;
    }
    
    /** Variable KeysVector. */
	protected List<String> KeysVector = new ArrayList<String>();

	/** Variable KeyDelimiter. */
	protected char KeyDelimiter = '/';
}
