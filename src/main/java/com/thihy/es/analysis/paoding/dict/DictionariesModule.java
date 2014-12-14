package com.thihy.es.analysis.paoding.dict;

import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.assistedinject.FactoryProvider;
import org.elasticsearch.common.inject.multibindings.MapBinder;

import com.google.common.collect.Maps;

public class DictionariesModule extends AbstractModule {

	private final Map<String, Class<? extends DictionariesLoader>> dictionariesLoaders = Maps.newHashMap();

	public DictionariesModule() {
		this.dictionariesLoaders.put("file", FileDictionariesLoader.class);
	}

	public void register(String type, Class<? extends DictionariesLoader> dictionariesLoaderFactory) {
		this.dictionariesLoaders.put(type, dictionariesLoaderFactory);
	}

	@Override
	protected void configure() {
		MapBinder<String, DictionariesLoaderFactory> dictionariesLoaderFactoriesBinder = MapBinder.newMapBinder(binder(), String.class,
				DictionariesLoaderFactory.class);
		for (Entry<String, Class<? extends DictionariesLoader>> dictionariesLoaderFactoryEntry : this.dictionariesLoaders.entrySet()) {
			dictionariesLoaderFactoriesBinder.addBinding(dictionariesLoaderFactoryEntry.getKey()).toProvider(
					FactoryProvider.newFactory(DictionariesLoaderFactory.class, dictionariesLoaderFactoryEntry.getValue()));
		}

		bind(WordLoader.class).asEagerSingleton();
		bind(DictionaryLoader.class).asEagerSingleton();
	}
}
