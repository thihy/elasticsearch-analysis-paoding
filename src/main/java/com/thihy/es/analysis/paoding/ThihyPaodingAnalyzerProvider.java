package com.thihy.es.analysis.paoding;

import java.io.IOException;

import net.paoding.analysis.knife.Dictionaries;
import net.paoding.analysis.knife.Knife;
import net.paoding.analysis.knife.Paoding;

import org.elasticsearch.common.Preconditions;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

import com.thihy.es.analysis.paoding.dict.DictionariesLoadContext;
import com.thihy.es.analysis.paoding.dict.DictionariesService;
import com.thihy.es.analysis.paoding.knife.KnifesService;

public class ThihyPaodingAnalyzerProvider extends AbstractIndexAnalyzerProvider<ThihyPaodingAnalyzer> {
	private static final String MODE_MOST_TOKENS = "most_tokens";
	private static final String MODE_MAX_LENGTH = "max_length";
	private static final String[] DEFAULT_KNIFE_TYPES = { "letter", "number", "cjk" };

	private final ThihyPaodingAnalyzer analyzer;

	@Inject
	public ThihyPaodingAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, DictionariesService dictionariesService,
			KnifesService knifesService, @Assisted String name, @Assisted Settings settings) throws IOException {
		super(index, indexSettings, name, settings);
		// 1.read setting
		ThihyPaodingMode mode = readMode(settings);

		String dictType = settings.get("dict.type", "file");
		Settings dictSettings = settings.getAsSettings("dict." + dictType);
		String[] knifeTypes = settings.getAsArray("knife", DEFAULT_KNIFE_TYPES);
		// 2.1 create  dictionaries
		DictionariesLoadContext dictionariesLoadContext = DictionariesLoadContext.builder().index(index, indexSettings).analyzerOwner(name)
				.dictSettings(dictSettings).build();
		Dictionaries dictionaries = dictionariesService.load(dictType, dictionariesLoadContext);
		// 2.2 create knives
		Knife[] knives = new Knife[knifeTypes.length];
		for (int knifeNo = 0; knifeNo < knifeTypes.length; knifeNo++) {
			knives[knifeNo] = knifesService.createKnife(knifeTypes[knifeNo], dictionaries);
		}
		// 2.3 create paoding
		Paoding paoding = new Paoding();
		paoding.setKnives(knives);
		// 3. create analyzer
		this.analyzer = new ThihyPaodingAnalyzer(mode, paoding);
	}

	private ThihyPaodingMode readMode(Settings settings) {
		String strMode = settings.get("mode", MODE_MOST_TOKENS);
		if (MODE_MOST_TOKENS.equals(strMode)) {
			return ThihyPaodingMode.MOST_WORDS;
		} else if (MODE_MAX_LENGTH.equals(strMode)) {
			return ThihyPaodingMode.MAX_WORD_LENGTH;
		} else {
			throw new IllegalArgumentException("The 'mode' is invalid. It should be '" + MODE_MOST_TOKENS + "' or '" + MODE_MAX_LENGTH
					+ "'.");
		}
	}

	@Override
	public ThihyPaodingAnalyzer get() {
		return this.analyzer;
	}
}
