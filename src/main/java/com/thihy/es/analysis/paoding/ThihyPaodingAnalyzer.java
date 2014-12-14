package com.thihy.es.analysis.paoding;

import java.io.Reader;

import net.paoding.analysis.analyzer.PaodingTokenizer;
import net.paoding.analysis.analyzer.TokenCollector;
import net.paoding.analysis.analyzer.impl.MaxWordLengthTokenCollector;
import net.paoding.analysis.analyzer.impl.MostWordsTokenCollector;
import net.paoding.analysis.knife.Paoding;

import org.apache.lucene.analysis.Analyzer;

public class ThihyPaodingAnalyzer extends Analyzer {
	private final ThihyPaodingMode mode;
	private final Paoding paoding;

	public ThihyPaodingAnalyzer(ThihyPaodingMode mode, Paoding paoding) {
		super();
		this.mode = mode;
		this.paoding = paoding;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		if (paoding == null) {
			throw new NullPointerException("paoding should be set before token");
		}
		// PaodingTokenizer是TokenStream实现，使用knife解析reader流入的文本
		return new TokenStreamComponents(new PaodingTokenizer(reader, paoding, createTokenCollector()));
	}

	protected TokenCollector createTokenCollector() {
		switch (mode) {
		case MOST_WORDS:
			return new MostWordsTokenCollector();
		case MAX_WORD_LENGTH:
			return new MaxWordLengthTokenCollector();
		default:
			throw new Error("never happened");
		}
	}
}
