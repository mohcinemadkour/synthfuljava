/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

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
public class HashMapTreeNode<K, V>
extends HashMap<K, TreeNode<K, V>>
implements TreeNode<K, V>{

	/**
	 * Instantiates a new HashTreeNode.
	 */
	public HashMapTreeNode() {
		super();
	}

	/**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 */
	public HashMapTreeNode(int initSz) {
		super(initSz);
	}

	/**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param initSz
	 * @param factor
	 */
	public HashMapTreeNode(int initSz, float factor) {
		super(initSz, factor);
	}

	/**
	 * Instantiates a new HashTreeNode.
	 * 
	 * @param map
	 */
	public HashMapTreeNode(Map<K, V> map) {
		add(map);
	}

	/**
	 * Adds the.
	 * 
	 * @param map
	 * 
	 * @return Adds the as HashTreeNode
	 */
	public HashMapTreeNode<K, V> add(Map<? extends K, ? extends V>  map) {
        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<? extends K, ? extends V> e = i.next();
			put(e.getKey(), new TreeLeafNode<K, V>(e.getValue()) );
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
	public String getString(K key) {
		TreeNode<K, V> o = get(key);
		if (o == null)
			return "";
		return o.toString();
	}

    /**
	 * Gets the ParentOf.
	 * 
	 * @param key
	 * 
	 * @return the ParentOf as Object
	 */
    public HashMapTreeNode<K, V> getParentOf (K[] keysegs)
    {
    	TreeNode<K, V> myH = this;
		for (int i = 0; i < keysegs.length-1; i++) {
            myH =  myH.getNode (keysegs[i]);
            if (myH == null || !myH.getClass().isInstance(this))
                return null;
        }
        return (HashMapTreeNode<K, V>) myH;
    }
    
    /**
	 * Gets the node.
	 * 
	 * @param key segments
	 * 
	 * @return the TreeNode
	 */
    public TreeNode<K, V> get (K[] keysegs)
    {
    	TreeNode<K, V> myH = this;
		for (int i = 0; i < keysegs.length; i++) {
            myH =  myH.getNode (keysegs[i]);
            if (myH == null || !myH.getClass().isInstance(this))
                return null;
        }
        return myH;
    }

    /**
	 * get stored node by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash vector.
	 * 
	 * @return Object stored at that position.
	 */
	public TreeNode<K, V> getNode(int position) {
		if (position < KeysVector.size() && position >= 0){
			return getNode(KeysVector.get(position));
		}

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
	public K getKey(int position) {
		if (position < KeysVector.size() && position >= 0)
			return KeysVector.get(position);

		return null;
	}

	/**
	 * Gets the KeyPosition.
	 * 
	 * @param key
	 * 
	 * @return the KeyPosition as int
	 */
	public int getKeyPosition(K key) {
		for (int i = 0; i < KeysVector.size(); i++)
			if (key.equals(KeysVector.get(i)))
				return i;
		return -1;
	}

	/**
	 * Gets the KeysVector.
	 * 
	 * @return the KeysVector as Vector
	 */
	public List<K> getKeysVector() {
		return KeysVector;
	}

	/**
	 * 
	 * 
	 * @param keysegs
	 *            keypath in array form
	 * @param value
	 *            value to be put into hash for key <BR>
	 *            would locate for, or create if not existent, a key,
	 *            and place the value object in a leaf node at that key level.
	 *            If a leaf node already exists along the path, no operation is performed.
	 * 
	 * @return this
	 * 
	 *         Algorithm uses iteration rather than recursion to progress from
	 *         one dot level to the next.
	 */

	public HashMapTreeNode<K, V> put(K[] keysegs, V value) {

		HashMapTreeNode<K, V> myH = this;
		for (int i = 0; i < keysegs.length; i++) {
			K mykey = keysegs[i];
			if (mykey == null || mykey.toString().length() == 0)
				continue;
			// If leaf key, put() value
			if (i == keysegs.length - 1) {
				myH.putNode(mykey, new TreeLeafNode<K, V>(value));
				return this;
			}

			// Otherwise continue locating/creating next level of key
			TreeNode<K, V> otree = myH.get(mykey);
			HashMapTreeNode<K, V> mytree
				= (otree != null && otree.getClass().isInstance(this))
				? (HashMapTreeNode<K, V>) otree
				: null;
			if (mytree == null) {
				mytree = new HashMapTreeNode<K, V>();
				myH.putNode(mykey, mytree);
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
	public HashMapTreeNode<K, V> clear(K[] keysegs) {

		HashMapTreeNode<K, V> myH = this;
		for (int i = 0; i < keysegs.length; i++) {
			K mykey = keysegs[i];
			if (mykey == null || mykey.toString().length() == 0)
				continue;
			// If leaf key, put() value
			if (i == keysegs.length - 1) {
				myH.clear();
				return this;
			}

			// Otherwise continue locating/creating next level of key
			TreeNode<K, V> otree = myH.get(mykey);
			HashMapTreeNode<K, V> mytree
				= (otree != null && otree.getClass().isInstance(this))
				? (HashMapTreeNode<K, V>) otree
				: null;
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
	@Override
	public HashMapTreeNode<K, V> putNode(K key, TreeNode<K, V> t) {
		if (key == null || t == null)
			return this;
		super.put(key, t);
		KeysVector.add(key);
		t.setParentNode(this);
		return this;
	}

	@Override
	public HashMapTreeNode<K, V> putNode(int i, K key,  TreeNode<K, V> t) {
		if (key == null || t == null)
			return this;
		super.put(key, t);
		if(KeysVector.size()<=i || i<0)
			KeysVector.add(key);
		else
			KeysVector.add(i, key);
		t.setParentNode(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Hashtable#clear()
	 */
	public void clear() {
		super.clear();
		KeysVector.clear();
	}

	/**
	 * Cut.
	 * 
	 * @param key
	 * 
	 * @return Cut as Object
	 */
	public TreeNode<K, V>  cut(K[] keys) {
		HashMapTreeNode<K, V> myH = getParentOf(keys);
		if (myH == null) return null;
		
		K lastKey = keys[keys.length-1];
		TreeNode<K, V> item = myH.get(lastKey);
		myH.remove(lastKey);
		myH.KeysVector.remove(lastKey);
		
		return item;
	}

	/**
	 * Converts to Array.
	 * 
	 * @return Array as Object[]
	 */
	@SuppressWarnings("unchecked")
	public TreeNode<K, V>[] toArray() {
		Object[] ar = new Object[size()];

		for (int i = 0; i < size(); i++) {
			ar[i] = getNode(i);
		}

		return (TreeNode<K, V>[]) ar;
	}

	/**
	 * Converts to List.
	 * 
	 * @param v
	 * 
	 * @return child nodes as List
	 */
	public List<TreeNode<K, V>> toVector(List<TreeNode<K, V>> v) {
		for (int i = 0; i < size(); i++) {
			v.add(getNode(i));
		}

		return v;
	}

	/**
	 * @see org.synthful.util.HashTreeNode<K, V>#getParentNode()
	 */
	public HashMapTreeNode<K, V> getParentNode() {
		return ParentNode;
	}

	/**
	 * @see org.synthful.util.HashTreeNode<K, V>#setParentNode(org.synthful.util.HashTreeNode<K, V>)
	 */
	public HashMapTreeNode<K, V> setParentNode(HashMapTreeNode<K, V> node) {
		ParentNode = node;
		return this;
	}

    /** Variable KeysVector. */
	protected List<K> KeysVector = new ArrayList<K>();

	/** Variable ParentNode. */
	protected HashMapTreeNode<K, V> ParentNode;

	@Override
	public TreeNode<K, V> getNode(K k) {
		return super.get(k);
	}

	@Override
	public V getValue() {
		return null;
	}


	@Override
	public TreeNode<K, V> setParentNode(TreeNode<K, V> node) {
		// TODO Auto-generated method stub
		return null;
	}

}
