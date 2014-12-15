package com.thihy.es.analysis.paoding.dict;

import java.io.IOException;
import java.util.Map;

import net.paoding.analysis.knife.Dictionaries;

import org.elasticsearch.common.Preconditions;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;

import com.google.common.collect.ImmutableMap;

public class DictionariesService extends AbstractComponent {

	private final Map<String, DictionariesLoaderFactory> dictionariesLoaders;

	@Inject
	public DictionariesService(Settings settings, Map<String, DictionariesLoaderFactory> dictionariesLoaders) {
		super(settings);
		this.dictionariesLoaders = ImmutableMap.copyOf(dictionariesLoaders);
	}

	public Dictionaries load(String type, DictionariesLoadContext context) throws IOException {
		DictionariesLoaderFactory dictionariesLoaderFactory = dictionariesLoaders.get(type);
		Preconditions.checkArgument(dictionariesLoaderFactory != null, "The type [{}] does not exist. It should be any of {}.", type,
				dictionariesLoaders.keySet());
		DictionariesLoader dictionariesLoader = dictionariesLoaderFactory.create(context);
		return dictionariesLoader.load();
	}

}
