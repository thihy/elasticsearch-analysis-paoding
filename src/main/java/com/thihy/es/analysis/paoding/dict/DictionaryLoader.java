package com.thihy.es.analysis.paoding.dict;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import net.paoding.analysis.dictionary.BinaryDictionary;
import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.HashBinaryDictionary;
import net.paoding.analysis.dictionary.Word;

import org.elasticsearch.common.inject.Inject;

public class DictionaryLoader {
	private static final int HASH_BIN_DICT_MIN_WORD_COUNT = 128;
	private static final int HASH_BIN_DICT_DEFAULT_CAPICITY = 128;
	private static final float HASH_BIN_DICT_DEFAULT_LOAD_FACTOR = 0.75F;

	private final WordLoader wordLoader;

	@Inject
	public DictionaryLoader(WordLoader wordLoader) {
		super();
		this.wordLoader = wordLoader;
	}

	public Dictionary loadDictionary(Reader reader) throws IOException {
		Word[] words = wordLoader.load(reader);
		return loadDictionary(words);
	}

	public Dictionary loadDictionary(Word[] words) {
		Arrays.sort(words);
		if (words.length <= HASH_BIN_DICT_MIN_WORD_COUNT) {
			return new BinaryDictionary(words);
		} else {
			return new HashBinaryDictionary(words, HASH_BIN_DICT_DEFAULT_CAPICITY, HASH_BIN_DICT_DEFAULT_LOAD_FACTOR);
		}
	}
}
