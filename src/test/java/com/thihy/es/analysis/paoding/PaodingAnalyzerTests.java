package com.thihy.es.analysis.paoding;

import java.io.IOException;

import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.test.ElasticsearchTokenStreamTestCase;
import org.junit.Test;

public class PaodingAnalyzerTests extends ElasticsearchTokenStreamTestCase {

	private static final String RESOURCE = "com/thihy/es/analysis/paoding/paoding_analysis.json";

	@Test
	public void testMaxLengthTokens() throws IOException {
		AnalysisService analysisService = AnalysisTestsHelper.createAnalysisServiceFromClassPath(RESOURCE);
		NamedAnalyzer analyzer = analysisService.analyzer("paoding_1");
		assertTokenStreamContents(analyzer.tokenStream("field", "我有一百元，你有多少钱"), new String[] { "我有", "100", "元", "你有", "有多少", "多少钱" });
		assertTokenStreamContents(analyzer.tokenStream("field", "北京明天再迎6级大风,冷空气前锋已达河套地区"), new String[] { "北京", "明天", "迎", "6", "级", "大风",
				"冷空气", "前锋", "已达", "河套", "地区" });
		assertTokenStreamContents(analyzer.tokenStream("field", "提前预售IPhone 5S"), new String[] { "提前", "预售", "iphone", "5s" });
	}

}
