package org.synthful.util;

public interface TreeNode<K, V> {
  public TreeNode<K, V> getParentNode();
  public TreeNode<K, V> getNode(int i);
  public TreeNode<K, V> getNode(K k);
  public TreeNode<K, V> setParentNode(TreeNode<K, V> node);
  public TreeNode<K, V> putNode(int i, K k, TreeNode<K, V> node);
  public TreeNode<K, V> putNode(K k, TreeNode<K, V> node);
  public V getValue();
}