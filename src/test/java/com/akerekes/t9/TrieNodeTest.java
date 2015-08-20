package com.akerekes.t9;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TrieNodeTest {

	@Test
	public void testConstructor() {
		TrieNode trieNode = new TrieNode("asd");
		assertThat(trieNode.getLetters()).isEqualTo(new char[]{'a', 's', 'd'});
		assertThat(trieNode.isTerminal()).isTrue();
		assertThat(trieNode.getChildren()).isEmpty();
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorWithEmptyShouldThrowError() {
		new TrieNode("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorWithNullShouldThrowError() {
		new TrieNode((char[]) null);
	}

	@Test
	public void sameLettersDoNotChangeState() {
		TrieNode node = new TrieNode("asd");
		Collection<TrieNode> children = new LinkedList<>(node.getChildren());
		char[] letters = node.getLetters().clone();
		boolean terminal = node.isTerminal();
		node.addLetters("asd");
		assertThat(node.getChildren()).isEqualTo(children);
		assertThat(node.getLetters()).isEqualTo(letters);
		assertThat(node.isTerminal()).isEqualTo(terminal);
	}

	@Test(expected = IllegalStateException.class)
	public void addLettersWhenFirstLetterIsDifferentShouldThrowError() {
		TrieNode node = new TrieNode("a");
		node.addLetters("b");
	}

	@Test
	public void twoLettersAddedToOneLetterTerminalNodeWithoutChildrenAddsAChild() {
		TrieNode node = new TrieNode("a");

		node.addLetters("ab");

		assertNode(node, "a", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 0);
	}

	@Test
	public void twoLettersAddedToTwoLetterTerminalNodeWithoutChildrenWillCreateTwoChildren() {
		TrieNode node = new TrieNode("ab");

		node.addLetters("ac");

		assertNode(node, "a", false, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"b", "c"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void twoLettersAddedToNonTerminalTwoLetterNodeWithoutChildrenWillCreateTwoChildren() {
		TrieNode node = new TrieNode("ab");
		node.makeNonTerminal();

		node.addLetters("ac");

		assertNode(node, "a", false, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"b", "c"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void twoLettersAddedToOneLetterTerminalNodeWithNonMatchingChildrenWillCreateAnotherChild() {
		TrieNode node = new TrieNode("a");
		node.getChildren().add(new TrieNode("b"));

		node.addLetters("ac");

		assertNode(node, "a", true, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"b", "c"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void twoLettersAddedToOneLetterNonTerminalNodeWithNonMatchingChildrenWillCreateAnotherChild() {
		TrieNode node = new TrieNode("a");
		node.makeNonTerminal();
		node.getChildren().add(new TrieNode("b"));

		node.addLetters("ac");

		assertNode(node, "a", false, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"b", "c"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void twoLettersAddedToOneLetterTerminalNodeWithMatchingChildrenWillNotChangeState() {
		TrieNode node = new TrieNode("a");
		node.getChildren().add(new TrieNode("b"));

		node.addLetters("ab");

		assertNode(node, "a", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 0);
	}

	@Test
	public void twoLettersAddedToOneLetterNonTerminalNodeWithMatchingChildrenWillNotChangeState() {
		TrieNode node = new TrieNode("a");
		node.makeNonTerminal();
		node.getChildren().add(new TrieNode("b"));

		node.addLetters("ab");

		assertNode(node, "a", false, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 0);
	}

	@Test
	public void threeLettersAddedToTwoLetterTerminalPrefixNodeWithoutChildrenAddNewChild() {
		TrieNode node = new TrieNode("ab");

		node.addLetters("abc");

		assertNode(node, "ab", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "c", true, 0);
	}

	@Test
	public void threeLettersAddedToTwoLetterTerminalPrefixNodeWithMatchingChildDoesNotChangeState() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));

		node.addLetters("abc");

		assertNode(node, "ab", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "c", true, 0);
	}

	@Test
	public void threeLettersAddedToTwoLetterNonTerminalPrefixNodeWithMatchingChildDoesNotChangeState() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));
		node.makeNonTerminal();

		node.addLetters("abc");

		assertNode(node, "ab", false, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "c", true, 0);
	}

	@Test
	public void threeLettersAddedToTwoLetterTerminalPrefixNodeWithoutMatchingChildAddNewChild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));

		node.addLetters("abd");

		assertNode(node, "ab", true, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"c", "d"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void threeLettersAddedToTwoLetterNonTerminalPrefixNodeWithoutMatchingChildAddNewChild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));
		node.makeNonTerminal();

		node.addLetters("abd");

		assertNode(node, "ab", false, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"c", "d"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void oneLetterPrefixAddedToTwoLetterTerminalNodeWithoutChildrenSplitsNode() {
		TrieNode node = new TrieNode("ab");

		node.addLetters("a");

		assertNode(node, "a", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 0);
	}

	@Test
	public void oneLetterPrefixAddedToTwoLetterNonTerminalNodeWithoutChildrenSplitsNode() {
		TrieNode node = new TrieNode("ab");
		node.makeNonTerminal();

		node.addLetters("a");

		assertNode(node, "a", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 0);
	}

	@Test
	public void oneLetterPrefixAddedToTwoLetterTerminalNodeWithChildSplitsNodeReassignsChild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));

		node.addLetters("a");

		assertNode(node, "a", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 1);
		TrieNode grandChild = child.getChildren().iterator().next();
		assertNode(grandChild, "c", true, 0);
	}

	@Test
	public void oneLetterPrefixAddedToTwoLetterNonTerminalNodeWithChildSplitsNodeReassignsChild() {
		TrieNode node = new TrieNode("ab");
		node.makeNonTerminal();
		node.getChildren().add(new TrieNode("c"));

		node.addLetters("a");

		assertNode(node, "a", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "b", true, 1);
		TrieNode grandChild = child.getChildren().iterator().next();
		assertNode(grandChild, "c", true, 0);
	}

	@Test
	public void twoLetterPrefixAddedToTwoLetterTerminalNodeWithChildSplitsNodeReassignsChild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));

		node.addLetters("ac");

		assertNode(node, "a", false, 2);
		Iterator<TrieNode> children = node.getChildren().iterator();
		TrieNode child = children.next();
		assertNode(child, "b", true, 1);
		TrieNode grandChild = child.getChildren().iterator().next();
		assertNode(grandChild, "c", true, 0);
		TrieNode otherChild = children.next();
		assertNode(otherChild, "c", true, 0);
	}

	@Test
	public void twoLetterPrefixAddedToThreeLetterTerminalNodeWithChildSplitsNodeReassignsChild() {
		TrieNode node = new TrieNode("abc");
		node.getChildren().add(new TrieNode("d"));

		node.addLetters("ac");

		assertNode(node, "a", false, 2);
		Iterator<TrieNode> children = node.getChildren().iterator();
		TrieNode child = children.next();
		assertNode(child, "bc", true, 1);
		TrieNode grandChild = child.getChildren().iterator().next();
		assertNode(grandChild, "d", true, 0);
		TrieNode otherChild = children.next();
		assertNode(otherChild, "c", true, 0);
	}

	@Test
	public void threeLetterPrefixAddedToTwoLetterTerminalMatchingNodeWithChildAddsNewChild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("c"));

		node.addLetters("acd");

		assertNode(node, "a", false, 2);
		Iterator<TrieNode> children = node.getChildren().iterator();
		TrieNode child = children.next();
		assertNode(child, "b", true, 1);
		TrieNode grandChild = child.getChildren().iterator().next();
		assertNode(grandChild, "c", true, 0);
		TrieNode newChild = children.next();
		assertNode(newChild, "cd", true, 0);
	}

	@Test
	public void lettersAreLongerPrefixOfCharsWithoutChildAddsNewChild() {
		TrieNode node = new TrieNode("ab");

		node.addLetters("abcd");

		assertNode(node, "ab", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "cd", true, 0);
	}

	@Test
	public void charsAreLongerPrefixOfLettersWithoutChildSplitsNode() {
		TrieNode node = new TrieNode("abcd");

		node.addLetters("ab");

		assertNode(node, "ab", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "cd", true, 0);
	}

	@Test
	public void lettersHaveLongerCommonPartWithLongerCharsWithoutChildAddsNewChild() {
		TrieNode node = new TrieNode("abdc");

		node.addLetters("abcde");

		assertNode(node, "ab", false, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"cde", "dc"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void charsHaveLongerCommonPartWithLongerLettersWithoutChildAddsNewChild() {
		TrieNode node = new TrieNode("abcde");

		node.addLetters("abdc");

		assertNode(node, "ab", false, 2);
		Collection<TrieNode> children = node.getChildren();
		assertChildren(children, new String[]{"cde", "dc"}, new Boolean[]{true}, new Collection[]{Collections.EMPTY_LIST});
	}

	@Test
	public void charsMatchesChildNodeButLongerAddsNewGrandchild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("cd"));

		node.addLetters("abcdef");

		assertNode(node, "ab", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "cd", true, 1);
		TrieNode grandchild = child.getChildren().iterator().next();
		assertNode(grandchild, "ef", true, 0);
	}

	@Test
	public void charsMatchesLetterAndPartiallyChildSplitsChild() {
		TrieNode node = new TrieNode("ab");
		node.getChildren().add(new TrieNode("cd"));

		node.addLetters("abce");

		assertNode(node, "ab", true, 1);
		TrieNode child = node.getChildren().iterator().next();
		assertNode(child, "c", false, 2);
		Collection<TrieNode> children = child.getChildren();
		assertChildren(children, new String[]{"d", "e"}, new Boolean[]{true}, new Collection[]{Collections.emptyList()});
	}

	@Test
	public void test() {

	}
	private void assertChildren(Collection<TrieNode> children, String[] letters, Boolean[] isTerminals, Collection[] childrens) {
		assertThat(children).extracting("letters").containsOnly(Arrays.stream(letters).map(String::toCharArray).toArray());
		assertThat(children).extracting("terminal").containsOnly((Object[]) isTerminals);
		assertThat(children).extracting("children").containsOnly((Object[]) childrens);
	}

	private void assertNode(TrieNode node, String letters, boolean isTerminal, int childrenSize) {
		assertThat(node.getLetters()).isEqualTo(letters.toCharArray());
		assertThat(node.isTerminal()).isEqualTo(isTerminal);
		if (childrenSize == 0) {
			assertThat(node.getChildren()).isEmpty();
		} else {
			assertThat(node.getChildren()).hasSize(childrenSize);
		}
	}
}