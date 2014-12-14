package com.thihy.es.analysis.paoding.knife;

import net.paoding.analysis.knife.Dictionaries;
import net.paoding.analysis.knife.LetterKnife;

public class LetterKnifeFactory implements KnifeFactory<LetterKnife> {

	@Override
	public LetterKnife create(Dictionaries dictionaries) {
		return new LetterKnife();
	}

}
