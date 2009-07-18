package org.synthful.util;


public interface TreeNode
{
  public String toString(
    String itemdelimiter, String recordterminator);
  
  public char getKeyDelimiter();
  public TreeNode getParentNode();
  public TreeNode getNode(int i);
  public Object get(int key);

  public TreeNode setKeyDelimiter(char delimiter);
  public TreeNode setParentNode(TreeNode node);
  
  public int size();
}