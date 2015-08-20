package com.akerekes.t9.util;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.akerekes.t9.TrieNode;

public class TriePrinterTest {

	@Test
	public void testPrintNode() throws Exception {
		TrieNode node = new TrieNode("alma");
		node.addLetters("almafa");
		node.addLetters("almabor");
		node.addLetters("almaborecet");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new TriePrinter().printNode(node, os);
		System.out.println(os.toString("utf-8"));
	}

	@Test
	public void testPrintNode2() throws Exception {
		TrieNode node = new TrieNode("abc");
		node.addLetters("ab");
		node.addLetters("ac");
		node.addLetters("abcd");
		node.addLetters("abbd");
		node.addLetters("accd");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new TriePrinter().printNode(node, os);
		System.out.println(os.toString("utf-8"));
	}
}