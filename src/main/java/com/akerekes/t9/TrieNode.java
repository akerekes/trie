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
		ensureCharsAndLettersStartsWithSameCharacter(chars);
		for (int i = 0; i < Integer.min(letters.length, chars.length); i++) {
			if (letters[i] != chars[i]) {
				splitNodeReassignChildren(chars, i);
				return;
			}
		}
		if (letters.length < chars.length) {
			// letters is prefix of chars
			// find child with next letter or add new child with rest of chars
			TrieNode child = findChildFor(chars[letters.length]);
			if (child == null) {
				addChild(new TrieNode(Arrays.copyOfRange(chars, letters.length, chars.length)));
			} else {
				child.addLetters(Arrays.copyOfRange(chars, letters.length, chars.length));
			}
		} else if (letters.length > chars.length) {
			// chars is prefix of letters, split current node at the prefix, create a child with rest of letters and reassign children to that
			splitNodeReassignChildren(chars, chars.length);
		}
		// letters == chars
	}

	private void ensureCharsAndLettersStartsWithSameCharacter(char[] chars) {
		if (letters[0] != chars[0]) {
			throw new IllegalStateException("First letters must match to allow updating trie, " +
					"maybe this input should have been handled by the parent node?");
		}
	}

	private void splitNodeReassignChildren(char[] chars, int i) {
		char[] newLetters = Arrays.copyOfRange(letters, 0, i);
		char[] child1Letters = Arrays.copyOfRange(letters, i, letters.length);
		letters = newLetters;
		TrieNode child = new TrieNode(child1Letters);
		child.children.addAll(children);
		children.clear();
		addChild(child);
		if (i < chars.length) {
			makeNonTerminal();
			char[] child2Letters = Arrays.copyOfRange(chars, i, chars.length);
			addChild(new TrieNode(child2Letters));
		} else {
			makeTerminal();
		}
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
