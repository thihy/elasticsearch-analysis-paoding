package com.thihy.es.analysis.paoding.knife;

import net.paoding.analysis.knife.Dictionaries;
import net.paoding.analysis.knife.NumberKnife;

public class NumberKnifeFactory implements KnifeFactory<NumberKnife> {

	@Override
	public NumberKnife create(Dictionaries dictionaries) {
		return new NumberKnife(dictionaries);
	}

}
