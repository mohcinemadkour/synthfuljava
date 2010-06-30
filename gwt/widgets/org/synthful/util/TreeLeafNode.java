/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2003
 * 
 */
package org.synthful.util;

public class TreeLeafNode<K, V>
implements TreeNode<K, V>{

	/** Variable ParentNode. */
	protected TreeNode<K, V> ParentNode;

	protected V value;

	/**
	 * Instantiates a new HashTreeNode.
	 */
	public TreeLeafNode(V value) {
		this.value = value;
	}

	/**
	 * get stored object by position.
	 * 
	 * @param position
	 *            Position of stored object in the hash vector.
	 * 
	 * @return Object stored at that position.
	 */
	@Override
	public V getValue() {
		return value;
	}

	/**
	 * @see org.synthful.util.HashTreeNode<K, V>#getParentNode()
	 */
	public TreeNode<K, V> getParentNode() {
		return ParentNode;
	}

	/**
	 * @see org.synthful.util.HashTreeNode<K, V>#getNode(int)
	 */
	@Override
	public TreeNode<K, V> getNode(int i) {
		return null;
	}

	/**
	 * @see org.synthful.util.HashTreeNode<K, V>#setParentNode(org.synthful.util.HashTreeNode<K, V>)
	 */
	@Override
	public TreeNode<K, V> setParentNode(TreeNode<K, V> node) {
		ParentNode = node;
		return this;
	}

	@Override
	public TreeNode<K, V> getNode(K k) {
		return null;
	}

	@Override
	public TreeNode<K, V> putNode(int i, K k, TreeNode<K, V> node) {
		return null;
	}

	@Override
	public TreeNode<K, V> putNode(K k, TreeNode<K, V> node) {
		return null;
	}

}
