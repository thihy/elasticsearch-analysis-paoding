package com.thihy.es.analysis.paoding.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import net.paoding.analysis.dictionary.Word;

import com.google.common.collect.Lists;

/**
 * 从输入流中加载单词列表
 * @author thihy
 *
 */
public class WordLoader {
	private static final Word[] EMPTY_WORD_ARRAY = new Word[0];

	public Word[] load(Reader reader) throws IOException {
		List<Word> result = Lists.newArrayList();
		try (BufferedReader bufferedReader = new BufferedReader(reader)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				result.add(new Word(line));
			}
		}
		if (result.isEmpty()) {
			return EMPTY_WORD_ARRAY;
		}
		return result.toArray(EMPTY_WORD_ARRAY);
	}
}
