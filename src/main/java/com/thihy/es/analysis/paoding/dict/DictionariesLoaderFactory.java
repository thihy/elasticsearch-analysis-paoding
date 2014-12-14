package com.thihy.es.analysis.paoding.dict;

import org.elasticsearch.common.settings.Settings;

public interface DictionariesLoaderFactory {
	DictionariesLoader create(Settings settings);
}
