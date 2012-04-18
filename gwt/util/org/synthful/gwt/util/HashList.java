/**
 * 
 */
package org.synthful.gwt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.Serializable;

/**
 * Have your cake and eat it too. <br/> <br/>
 * 
 * An ordered hash to allow accessing mapped content either by hash key or by position index.
 * When an object is stored, its key is stored in an ordered list. If an object is stored under
 * hash key "a" and at position 5, then that object can be accessed either by <br/>
 * get("a"), or <br/>
 * get(5)  <br/> <br/>
 * 
 * 
 * @author blessedgeek
 * 
 */
public class HashList<K, V> extends HashMap<K, V> implements Serializable {
	/**
	 * Instantiates a new HashList.
	 */
	public HashList() {
		super();
	}

	/**
	 * Instantiates a new HashList.
	 * 
	 * @param map
	 */
	public HashList(Map<K,V> map) {
		super.putAll(map);
	}

	/**
	 * Instantiates a new HashList.
	 * 
	 * @param t
	 */
	public HashList(Object[][] t) {
		add(t);
	}

	/**
	 * Instantiates a new HashList.
	 * 
	 * @param initSz
	 */
	public HashList(int initSz) {
		super(initSz);
	}

	/**
	 * Instantiates a new HashList.
	 * 
	 * @param initSz
	 * @param factor
	 */
	public HashList(int initSz, float factor) {
		super(initSz, factor);
	}

	/**
	 * Adds the 2D object array [row][col] to the HashList,
	 * where for each row, using col=0 as key and col=1 as value.
	 * 
	 * @param t
	 * 
	 * @return Adds the as HashList
	 */
	public HashList<K, V> add(Object[][] t) {
		if (t != null)
			for (int i = 0; i < t.length; i++) {
				if (t[i] == null || t[i].length < 1)
					continue;
				Object key = t[i][0];
				Object value = t[i].length >= 2 ? t[i][1] : null;

				if (key == null || value == null)
					continue;

				try {
					this.put((K) key, (V) value);
				}
				catch (ClassCastException ex) {
				}
				catch (Exception ex) {
				}
			}

		return this;
	}
	
	/**
	 * Gets the String Value by Key.
	 * 
	 * @param key
	 * 
	 * @return the String as String
	 */
	public String getString(Object key) {
		Object o = get(key);
		if (o == null)
			return "";
		return o.toString();
	}

	/**
	 * get stored object by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash List.
	 * 
	 * @return Object stored at that position.
	 */
	public V get(int position) {
		if (position < keyList.size() && position >= 0)
			return this.get(keyList.get(position));

		return null;
	}

	/**
	 * get hash key of stored object by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash List.
	 * 
	 * @return Hash key of object stored at that position.
	 */
	public K getKey(int position) {
		if (position < keyList.size() && position >= 0)
			return keyList.get(position);

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
		for (int i = 0; i < keyList.size(); i++)
			if (key.equals(keyList.get(i)))
				return i;
		return -1;
	}

	/**
	 * Gets the KeyList.
	 * 
	 * @return the KeyList as List
	 */
	public List getKeyList() {
		return keyList;
	}

	public V put(K key, V value) {
		this.putValue(key, value);
		return value;
	}

	/**
	 * Put value.
	 * 
	 * @param key
	 * @param value
	 * 
	 * @return Put value as HashList
	 */
	protected HashList<K, V> putValue(K key, V value) {
		if (key == null || value == null)
			return this;
		super.put(key, value);
		if (!keyList.contains(key))
			keyList.add(key);
		return this;
	}

	@Override
	public V remove(Object o) {
		return this.removeValue((K) o);
	}

	public V removeValue(K key) {
		try {
			V val = this.get(key);
			super.remove(key);
			this.keyList.remove(key);
			return val;
		} catch (Exception e) {
			return null;
		}
	}

	public V remove(int i) {
		K key = this.keyList.get(i);
		return removeValue(key);
	}

	/**
	 * @see java.util.Hashtable#clear()
	 */
	public void clear() {
		super.clear();
		keyList.clear();
	}

	/**
	 * Converts to Array.
	 * 
	 * @return Array as Object[]
	 */
	public V[] toValueArray(V[] a) {
		return this.toList().toArray(a);
	}

	/**
	 * Converts to Array.
	 * 
	 * @return Array as Object[]
	 */
	public Object[][] toKeyValueArray() {
		Class<V> componentType = null;
		Object[][] ar = new Object[this.size()][2];

		for (int i = 0; i < size(); i++) {
			ar[i][0] = this.getKey(i);
			ar[i][1] = this.get(i);
		}

		return ar;
	}

	/**
	 * Converts to List.
	 * 
	 * @param v
	 * 
	 * @return List
	 */
	public List<V> toValueList(List<V> v) {
		for (int i = 0; i < size(); i++) {
			v.add(get(i));
		}

		return v;
	}

	/**
	 * Converts to List.
	 * 
	 * @return List as List
	 */
	public List<V> toList() {
		return toValueList(new ArrayList<V>(size()));
	}

	/** Variable KeyList. */
	protected List<K> keyList = new ArrayList<K>();

}
