package com.akerekes.t9;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Trie {
	
	SortedMap<Character, TrieNode> children = new TreeMap<>();

	public Trie addLetters(String chars) {
		SortedMap<Character, TrieNode> tailMap = children.tailMap(chars.charAt(0));
		if (tailMap.isEmpty()) {
			children.put(chars.charAt(0), new TrieNode(chars));
		}
		Map.Entry<Character, TrieNode> entry = tailMap.entrySet().iterator().next();
		if (entry.getKey().charValue() == chars.charAt(0)) {
			entry.getValue().addLetters(chars);
		} else {
			children.put(chars.charAt(0), new TrieNode(chars));
		}
		return this;
	}

	public Collection<TrieNode> getChildren() {
		return children.values();
	}
}
