package com.thihy.es.analysis.paoding.dict;

import static org.junit.Assert.*;

import java.io.IOException;

import net.paoding.analysis.knife.Dictionaries;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.junit.Test;

public class FileDictionariesLoaderTest {

	private final WordLoader wordLoader = new WordLoader();
	private final DictionaryLoader dictionaryLoader = new DictionaryLoader(wordLoader);

	@Test
	public void testLoadDefaultDict() throws IOException {
		DictionariesLoadContext context = DictionariesLoadContext.builder().dictSettings(ImmutableSettings.EMPTY).build();
		FileDictionariesLoader fileDictionariesLoader = new FileDictionariesLoader(dictionaryLoader, context);
		Dictionaries dict1 = fileDictionariesLoader.load();
		Dictionaries dict2 = fileDictionariesLoader.load();
		assertSame(dict1, dict2);
		assertNotNull(dict1.getVocabularyDictionary());
		assertEquals(238051, dict1.getVocabularyDictionary().size());
	}

}
