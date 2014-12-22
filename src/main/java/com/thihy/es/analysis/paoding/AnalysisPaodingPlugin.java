package com.thihy.es.analysis.paoding;

import java.util.Collection;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

import com.thihy.es.analysis.paoding.dict.DictionariesModule;
import com.thihy.es.analysis.paoding.knife.KnifesModule;

public class AnalysisPaodingPlugin extends AbstractPlugin {

	@Override
	public String name() {
		return "analysis-paoding";
	}

	@Override
	public String description() {
		return "Lucene中文分词“庖丁解牛” Paoding Analysis";
	}

	@Override
	public Collection<Class<? extends Module>> modules() {
		return ImmutableList.<Class<? extends Module>> of(DictionariesModule.class, KnifesModule.class);
	}

	public void onModule(AnalysisModule module) {
		module.addAnalyzer("paoding", ThihyPaodingAnalyzerProvider.class);
		module.addTokenizer("paoding", ThihyPaodingTokenizerProvider.class);
	}
}
