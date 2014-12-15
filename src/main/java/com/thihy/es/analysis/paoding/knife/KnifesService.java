package com.thihy.es.analysis.paoding.knife;

import java.util.Map;

import net.paoding.analysis.knife.Dictionaries;
import net.paoding.analysis.knife.Knife;

import org.elasticsearch.common.Preconditions;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

public class KnifesService extends AbstractComponent {

	private final Map<String, KnifeFactory> knifes;

	@Inject
	public KnifesService(Settings settings, Map<String, KnifeFactory> knifes) {
		super(settings);
		this.knifes = ImmutableMap.copyOf(knifes);
	}

	public boolean hasKnife(String type) {
		return knifes.containsKey(type);
	}

	public Knife createKnife(String type, Dictionaries dictionaries) {
		KnifeFactory knifeFactory = knifes.get(type);
		Preconditions.checkArgument(knifeFactory != null, "The knife type [{}] does not exist. All knife types are {}.", type,
				knifes.keySet());
		return knifeFactory.create(dictionaries);
	}
}
