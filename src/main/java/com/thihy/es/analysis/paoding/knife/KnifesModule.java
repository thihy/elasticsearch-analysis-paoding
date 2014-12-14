package com.thihy.es.analysis.paoding.knife;

import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.multibindings.MapBinder;

import com.google.common.collect.Maps;

public class KnifesModule extends AbstractModule {
	private final Map<String, Class<? extends KnifeFactory>> knifes = Maps.newHashMap();

	public KnifesModule() {
		knifes.put("number", NumberKnifeFactory.class);
		knifes.put("letter", LetterKnifeFactory.class);
		knifes.put("cjk", CJKKnifeFactory.class);
	}

	public void registerKnife(String name, Class<KnifeFactory> knife) {
		this.knifes.put(name, knife);
	}

	@Override
	protected void configure() {
		MapBinder<String, KnifeFactory> mapbinder = MapBinder.newMapBinder(binder(), String.class, KnifeFactory.class);
		for (Entry<String, Class<? extends KnifeFactory>> knifeEntry : knifes.entrySet()) {
			mapbinder.addBinding(knifeEntry.getKey()).to(knifeEntry.getValue());
		}
		bind(KnifesService.class);
	}

}
