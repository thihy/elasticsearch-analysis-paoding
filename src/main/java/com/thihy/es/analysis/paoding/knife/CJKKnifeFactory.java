package com.thihy.es.analysis.paoding.knife;

import net.paoding.analysis.knife.CJKKnife;
import net.paoding.analysis.knife.Dictionaries;

public class CJKKnifeFactory implements KnifeFactory<CJKKnife> {

	@Override
	public CJKKnife create(Dictionaries dictionaries) {
		return new CJKKnife(dictionaries);
	}

}
