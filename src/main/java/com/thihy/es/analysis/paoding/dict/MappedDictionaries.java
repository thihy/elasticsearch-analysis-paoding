package com.thihy.es.analysis.paoding.dict;

import java.util.EnumMap;

import net.paoding.analysis.dictionary.BinaryDictionary;
import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.Word;
import net.paoding.analysis.dictionary.support.detection.DifferenceListener;
import net.paoding.analysis.ext.PaodingAnalyzerListener;
import net.paoding.analysis.knife.Dictionaries;

public class MappedDictionaries implements Dictionaries {
	private static final Dictionary EMTPY_DICTIONARY = new BinaryDictionary(new Word[0]);
	private final EnumMap<DictionaryType, Dictionary> map = new EnumMap<>(DictionaryType.class);

	@Override
	public Dictionary getVocabularyDictionary() {
		return getDictionary(DictionaryType.WORDS);
	}

	@Override
	public Dictionary getConfucianFamilyNamesDictionary() {
		return getDictionary(DictionaryType.SURNAME);
	}

	@Override
	public Dictionary getNoiseCharactorsDictionary() {
		return getDictionary(DictionaryType.NOISE);
	}

	@Override
	public Dictionary getNoiseWordsDictionary() {
		return getDictionary(DictionaryType.NOISE);
	}

	@Override
	public Dictionary getUnitsDictionary() {
		return getDictionary(DictionaryType.UNIT);
	}

	@Override
	public Dictionary getCombinatoricsDictionary() {
		return getDictionary(DictionaryType.COMB);
	}

	void setDictionary(DictionaryType dictionaryType, Dictionary dictionary) {
		synchronized (map) {
			map.put(dictionaryType, dictionary);
		}
	}

	private Dictionary getDictionary(DictionaryType dictionaryType) {
		synchronized (map) {
			Dictionary dictionary = map.get(dictionaryType);
			if (dictionary == null) {
				return EMTPY_DICTIONARY;
			}
			return dictionary;
		}
	}

	@Override
	public void startDetecting(int interval, DifferenceListener l) {

	}

	@Override
	public void stopDetecting() {

	}

	@Override
	public void setAnalyzerListener(PaodingAnalyzerListener listener) {

	}

}
