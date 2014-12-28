package com.thihy.es.analysis.paoding.dict;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import net.paoding.analysis.dictionary.*;

import org.elasticsearch.common.inject.Inject;

public class DictionaryLoader {
	private static final boolean USE_FST_DICT = true;

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
		if (USE_FST_DICT) {
			return new FstDictionary(words);
		} else  {
			return new BinaryDictionary(words);
		}
	}
}
