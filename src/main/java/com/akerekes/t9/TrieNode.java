package com.akerekes.t9;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class TrieNode {
	char[] letters;
	boolean terminal;
//	int count;
	Collection<TrieNode> children;

	public TrieNode(String letters) {
		this(letters.toCharArray());
	}

	public TrieNode(char[] letters) {
		if (letters == null || letters.length == 0) {
			throw new IllegalArgumentException("letters cannot be null or empty");
		}
		this.letters = letters;
		terminal = true;
		children = initChildrenCollection();
	}

	private Collection<TrieNode> initChildrenCollection() {
		return new LinkedList<>();
	}

	public char[] getLetters() {
		return letters;
	}

	public boolean isTerminal() {
		return terminal;
	}

//	public int getCount() {
//		return count;
//	}

	public Collection<TrieNode> getChildren() {
		return children;
	}

	public void addLetters(String chars) {
		addLetters(chars.toCharArray());
	}

	public void addLetters(char[] chars) {
		if (letters[0] != chars[0]) {
			throw new IllegalStateException("First letters must match to allow updating trie, " +
					"maybe this input should have been handled by the parent node?");
		}
		if (Arrays.equals(letters, chars)) {
			return;
		}
		if (letters.length < chars.length) {
			for (int i = 1; i < letters.length; i++) {
				if (letters[i] != chars[i]) {
					// difference, split into child
					splitNodeReassignChildren(chars, i);
					return;
				}
			}
			// letters is prefix of chars
			// find child with next letter or add new child with rest of chars
			TrieNode child = findChildFor(chars[letters.length]);
			if (child == null) {
				addChild(new TrieNode(Arrays.copyOfRange(chars, letters.length, chars.length)));
			} else {
				child.addLetters(Arrays.copyOfRange(chars, letters.length, chars.length));
			}
		} else if (letters.length > chars.length) {
			for (int i = 1; i < chars.length; i++) {
				if (letters[i] != chars[i]) {
					// difference, split into child
					splitNodeReassignChildren(chars, i);
					return;
				}
			}
			// chars is prefix of letters, split current node at the prefix, create a child with rest of letters and reassign children to that
			char[] newLetters = Arrays.copyOfRange(letters, 0, chars.length);
			char[] childLetters = Arrays.copyOfRange(letters, chars.length, letters.length);
			letters = newLetters;
			makeTerminal();
			TrieNode child = new TrieNode(childLetters);
			child.children.addAll(children);
			children.clear();
			addChild(child);
		} else {
			// letters and chars and same length
			for (int i = 1; i < letters.length; i++) {
				if (letters[i] != chars[i]) {
					// difference, split into child
					splitNodeReassignChildren(chars, i);
					return;
				}
			}
			// letters and chars are the same, but this has been checked earlier in this function
			throw new IllegalStateException("Letters and chars are the same, but this should have been checked earlier in this function");
		}
	}

	private void splitNodeReassignChildren(char[] chars, int i) {
		char[] newLetters = Arrays.copyOfRange(letters, 0, i);
		char[] child1Letters = Arrays.copyOfRange(letters, i, letters.length);
		char[] child2Letters = Arrays.copyOfRange(chars, i, chars.length);
		letters = newLetters;
		makeNonTerminal();
		TrieNode child = new TrieNode(child1Letters);
		child.children.addAll(children);
		children.clear();
		addChild(child);
		addChild(new TrieNode(child2Letters));
	}

	private TrieNode findChildFor(char aChar) {
		for (TrieNode child : children) {
			if (child.letters[0] == aChar) {
				return child;
			}
		}
		return null;
	}

	private void addChild(TrieNode child) {
		children.add(child);
	}

	public void makeTerminal() {
		terminal = true;
	}

	public void makeNonTerminal() {
		terminal = false;
	}
}
