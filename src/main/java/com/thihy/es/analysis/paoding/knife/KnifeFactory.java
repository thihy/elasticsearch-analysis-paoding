package com.thihy.es.analysis.paoding.knife;

import net.paoding.analysis.knife.Dictionaries;
import net.paoding.analysis.knife.Knife;

public interface KnifeFactory<T extends Knife> {
	T create(Dictionaries dictionaries);
}
