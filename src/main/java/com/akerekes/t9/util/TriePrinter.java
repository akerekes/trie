package com.akerekes.t9.util;

import java.io.IOException;
import java.io.OutputStream;

import com.akerekes.t9.TrieNode;

public class TriePrinter {

	public void printNode(TrieNode node, OutputStream os) throws IOException {
		printNode(node, 0, os);
	}

	private void printNode(TrieNode trieNode, int indent, OutputStream os) throws IOException {
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			line.append('\t');
		}
		line.append(trieNode.getLetters());
		if (trieNode.isTerminal()) {
			line.append(" - t");
		}
		line.append('\n');
		os.write(line.toString().getBytes());
//		System.out.println(line);
		for (TrieNode node : trieNode.getChildren()) {
			printNode(node, indent + 1, os);
		}
	}
}
