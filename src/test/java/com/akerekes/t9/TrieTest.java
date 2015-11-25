package com.akerekes.t9;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrieTest {

	@Test
	public void testAddLettersToEmptyTrieAddsNewChildWithAllLetters() throws Exception {
		Trie trie = new Trie().addLetters("abc");
		Collection<TrieNode> children = trie.getChildren();
		Assertions.assertThat(children).hasSize(1);
		TrieNode child = children.iterator().next();
		Assertions.assertThat(child.getLetters()).isEqualTo("abc".toCharArray());
	}

	@Test
	// TODO clean this up
	public void testAddLettersToTrieWithNonMatchingFirstLetterAddsNewChild() {
		Trie trie = new Trie().addLetters("abc").addLetters("def");
		Collection<TrieNode> children = trie.getChildren();
		Assertions.assertThat(children).hasSize(2);
		TrieNode child = children.iterator().next();
		Assertions.assertThat(child.getLetters()).isIn("abc".toCharArray(), "def".toCharArray());
		child = children.iterator().next();
		Assertions.assertThat(child.getLetters()).isIn("abc".toCharArray(), "def".toCharArray());
	}

	@Test
	// TODO clean this up
	public void testAddLettersToTrieWithNonMatchingFirstLetterAddsNewChild() {
		Trie trie = new Trie().addLetters("abc").addLetters("def");
		Collection<TrieNode> children = trie.getChildren();
		Assertions.assertThat(children).hasSize(2);
		TrieNode child = children.iterator().next();
		Assertions.assertThat(child.getLetters()).isIn("abc".toCharArray(), "def".toCharArray());
		child = children.iterator().next();
		Assertions.assertThat(child.getLetters()).isIn("abc".toCharArray(), "def".toCharArray());
	}
}