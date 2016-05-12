package com.lothrazar.cyclicmagic.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class UtilString {

	// https://stackoverflow.com/questions/7528045/large-string-split-into-lines-with-maximum-length-in-java

	public static List<String> splitIntoLine(String input, int maxCharInLine) {

		// better than spell.getInfo().split("(?<=\\G.{25})")
		StringTokenizer tok = new StringTokenizer(input, " ");
		StringBuilder output = new StringBuilder(input.length());
		int lineLen = 0;
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken();

			while (word.length() > maxCharInLine) {
				output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
				word = word.substring(maxCharInLine - lineLen);
				lineLen = 0;
			}

			if (lineLen + word.length() > maxCharInLine) {
				output.append("\n");
				lineLen = 0;
			}
			output.append(word + " ");

			lineLen += word.length() + 1;
		}
		// output.split();
		// return output.toString();
		return Arrays.asList(output.toString().split("\n"));
	}

}
