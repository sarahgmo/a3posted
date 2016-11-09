package a3posted;

//  COMP 250 - Introduction to Computer Science - Fall 2016
//  Assignment #3 - Question 1

import java.util.*;

/*
 *  Trie class.  Each node is associated with a prefix of some key 
 *  stored in the trie.   (Any string is a prefix of itself.)
 */

public class Trie
{
	private TrieNode root;

	// Empty trie has just a root node.  All the children are null.

	public Trie() 
	{
		root = new TrieNode();
	}

	public TrieNode getRoot(){
		return root;
	}

	
	/*
	 * Insert key into the trie.  First, find the longest 
	 * prefix of a key that is already in the trie (use getPrefixNode() below). 
	 * Then, add TrieNode(s) such that the key is inserted 
	 * according to the specification in PDF.
	 */
	public void insert(String key)
	{
		//  ADD YOUR CODE BELOW HERE
		//use getPrefixNode() to check for similar prefixes
		TrieNode start = getPrefixNode(key); //find where to start adding characters
		int curDepth = start.getDepth();
		while(curDepth < key.length()){
		//for(int i = curDepth; i < key.length(); i++){
			start = start.createChild(key.charAt(curDepth)); //start is parent
			curDepth++;
		}
		start.setEndOfKey(true);
		//  ADD YOUR ABOVE HERE
	}

	// insert each key in the list (keys)

	public void loadKeys(ArrayList<String> keys)
	{
		for (int i = 0; i < keys.size(); i++)
		{
			insert(keys.get(i));
		}
		return;
	}

	/*
	 * Given an input key, return the TrieNode corresponding the longest prefix that is found. 
	 * If no prefix is found, return the root. 
	 * In the example in the PDF, running getPrefixNode("any") should return the
	 * dashed node under "n", since "an" is the longest prefix of "any" in the trie. 
	 */
	private TrieNode getPrefixNode(String key)
	{
		//   ADD YOUR CODE BELOW HERE

		//go through each character of new word until end or until no more matching characters
		TrieNode current = root;
		for (int i = 0; i < key.length(); i++){
			if(key.charAt(i) == current.getChild(key.charAt(i)).charInParent){ //must match child's charInParent
				current = current.getChild(key.charAt(i)); //next node
			}
		}		
		return current;
		//
		//   ADD YOUR CODE ABOVE HERE

	}

	/*
	 * Similar to getPrefixNode() but now return the prefix as a String, rather than as a TrieNode.
	 */

	public String getPrefix(String key)
	{
		return getPrefixNode(key).toString();
	}

	
	/*
	 *  Return true if key is contained in the trie (i.e. it was added by insert), false otherwise.
	 *  Hint:  any string is a prefix of itself, so you can use getPrefixNode().
	 */
	public boolean contains(String key)
	{  
		//   ADD YOUR CODE BELOW HERE
		//getPrefixNode(), then see if there's any characters left; also watch for root
		TrieNode prefix = getPrefixNode(key);
		//int depth = prefix.getDepth();
		if(prefix == null){
			return false;
		}
		
		if((prefix.getDepth() == key.length()) && (prefix.endOfKey)){ //no characters left
			return true;
		}
		else{
			return false;
		}
		
		//   ADD YOUR CODE ABOVE HERE
		
	}

	/*
	 *  Return a list of all keys in the trie that have the given prefix. 
	 */
	public ArrayList<String> getAllPrefixMatches( String prefix )
	{
		//  ADD YOUR CODE BELOW 
		ArrayList<String> stringList = new ArrayList<String>();
		TrieNode start = getPrefixNode(prefix);
		TrieNode current;
		//while there are still more characters possible
		if (start.isEndOfKey()){//if prefix is a key in itself
		   stringList.add(start.toString());
	    }
		while(start.getDepth() <= root.getDepth()){
			//find element to add
			//get all nodes from children []
			for (int i = 0; i < TrieNode.NUMCHILDREN; i++){
				if (start.getChild((char)i) != null){ //if char child exists
					current = start.getChild((char)i);
					if(current.isEndOfKey()){
						stringList.add(current.toString());
					}
				}
			}
			
		}
		
		//  ADD YOUR CODE ABOVE HERE

		return stringList;
	}

	
	/*
	 *  A node in a Trie (prefix) tree.  
	 *  It contains an array of children: one for each possible character.
	 *  The ith child of a node corresponds to character (char)i
	 *  which is the UNICODE (and ASCII) value of i. 
	 *  Similarly the index of character c is (int)c.
	 *  So children[97] = children[ (int) 'a']  would contain the child for 'a' 
	 *  since (char)97 == 'a'   and  (int)'a' == 97.
	 * 
	 * References:
	 * -For all unicode charactors, see http://unicode.org/charts
	 *  in particular, the ascii characters are listed at http://unicode.org/charts/PDF/U0000.pdf
	 * -For ascii table, see http://www.asciitable.com/
	 * -For basic idea of converting (casting) from one type to another, see 
	 *  any intro to Java book (index "primitive type conversions"), or google
	 *  that phrase.   We will cover casting of reference types when get to the
	 *  Object Oriented Design part of this course.
	 */

	private class TrieNode
	//each TrieNode has 4 properties:
	//children (TrieNode array), endKey, depth, charInParent
	{
		/*  
		 *   Highest allowable character index is NUMCHILDREN-1
		 *   (assuming one-byte ASCII i.e. "extended ASCII")
		 *   
		 *   NUMCHILDREN is constant (static and final)
		 *   To access it, write "TrieNode.NUMCHILDREN"
		 */

		public static final int NUMCHILDREN = 256; //any ASCII character

		private TrieNode   parent;
		private TrieNode[] children; //array of children
		private int        depth;            // 0 for root, 1 for root's children, 2 for their children, etc..


		private char       charInParent;    // Character associated with edge between this node and its parent.
		        			        		// See comment above for relationship between an index in 0 to 255 and a char value.
		private boolean endOfKey;   // Set to true if prefix associated with this node is also a key.

		// Constructor for new, empty node with NUMCHILDREN children.  All the children are null. 

		public TrieNode()
		{
			children = new TrieNode[NUMCHILDREN]; //NUMCHILDREN actually = number of nodes?
			endOfKey = false;
			depth = 0; 
			charInParent = (char)0; 
		}

		
		/*
		 *  Add a child to current node.  The child is associated with the character specified by
		 *  the method parameter.  Make sure you set all fields in the child node.
		 *  
		 *  To implement this method, see the comment above the inner class TrieNode declaration.  
		 */
		public TrieNode createChild(char  c) 
		{	   
			TrieNode child = new TrieNode(); //builds second circle! 
			

			// ADD YOUR CODE BELOW HERE
			//need to add next character of given prefix to end of trie path
			//add children to TrieNode [] children array?
			//children [depth] = charInParent;
			//child.setDepth(this.depth++, child);
			
			
			//build edge: child's parent is "this"
			child.parent = this; //this = root or other current node
			child.depth = depth++; //add 1 to depth
			child.charInParent = c; //set character
			//add child to parent's children array
			children[c] = child;
			
			// ADD YOUR CODE ABOVE HERE

			return child;
		}

		// Get the child node associated with a given character, i.e. that character is "on" 
		// the edge from this node to the child.  The child could be null.  

		public TrieNode getChild(char c) 
		{
			return children[ c ];
		}

		// Test whether the path from the root to this node is a key in the trie.  
		// Return true if it is, false if it is prefix but not a key.

		public boolean isEndOfKey() 
		{
			return endOfKey;
		}

		// Set to true for the node associated with the last character of an input word

		public void setEndOfKey(boolean endOfKey)
		{
			this.endOfKey = endOfKey;
		}

		/*  
		 *  Return the prefix (as a String) associated with this node.  This prefix
         *  is defined by descending from the root to this node.  However, you will
         *  find it is easier to implement by ascending from the node to the root,
         *  composing the prefix string from its last character to its first.  
		 *
		 *  This overrides the default toString() method.
		 */

		public String toString()
		{
			// ADD YOUR CODE BELOW HERE
			//if root, has not parent
			if (this.parent == null){
				return "";
			}
			else{
				return this.parent.toString() + this.charInParent;
				//recursive, similar to stack
			}
			
			
			// ADD YOUR CODE ABOVE HERE
		
		}
		
		//HELPER METHOD
		public int getDepth() {
			return this.depth;
		}
	}

}
