package com.thihy.es.analysis.paoding.dict;

import java.io.IOException;

import net.paoding.analysis.knife.Dictionaries;

public interface DictionariesLoader {

	Dictionaries load() throws IOException;
}
