package com.thihy.es.analysis.paoding;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.test.ElasticsearchTokenStreamTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.eventbus.AllowConcurrentEvents;
import com.carrotsearch.randomizedtesting.annotations.Repeat;

@Repeat(iterations = 10)
public class PaodingAnalyzerTests extends ElasticsearchTokenStreamTestCase {

	private static final String RESOURCE = "com/thihy/es/analysis/paoding/paoding_analysis.json";
	//
	private static NamedAnalyzer paoding_max_length;
	private static NamedAnalyzer paoding_most_tokens;

	@BeforeClass
	public static void initAnalyzers() {
		AnalysisService analysisService = AnalysisTestsHelper.createAnalysisServiceFromClassPath(RESOURCE);
		paoding_max_length = analysisService.analyzer("paoding_1");
		paoding_most_tokens = analysisService.analyzer("paoding_2");
	}

	@AfterClass
	public static void closeAnalyzers() {
		paoding_max_length = null;
		paoding_most_tokens = null;
	}

	@Test
	@AllowConcurrentEvents
	public void testTokenizerForMaxLength() throws IOException {
		assertTokenStreamContents(paoding_max_length.tokenStream("field", "我有一百元，你有多少钱"), new String[] { "我有", "100", "元", "你有", "有多少",
				"多少钱" });
		assertTokenStreamContents(paoding_max_length.tokenStream("field", "北京明天再迎6级大风,冷空气前锋已达河套地区"), new String[] { "北京", "明天", "迎", "6",
				"级", "大风", "冷空气", "前锋", "已达", "河套", "地区" });
		assertTokenStreamContents(paoding_max_length.tokenStream("field", "提前预售IPhone 5S"), new String[] { "提前", "预售", "iphone", "5s" });
	}

	@Test
	@AllowConcurrentEvents
	public void testPosIncForMaxLength() throws IOException {
		assertTokenStreamContents(paoding_max_length.tokenStream("field", "我有一百元，你有多少钱"), //
				new String[] { "我有", "100", "元", "你有", "有多少", "多少钱" },//
				new int[] { 1, 1, 1, 1, 1, 1 });
	}

	@Test
	@AllowConcurrentEvents
	public void testPosIncForMostTokens() throws IOException {
		assertTokenStreamContents(paoding_most_tokens.tokenStream("field", "我有一百元，你有多少钱"), //
				new String[] { "我", "我有", "100", "元", "你", "你有", "有多", "有多少", "多少", "多少钱" },//
				new int[] { 1, 0, 1, 1, 1, 0, 1, 0, 1, 0 }//posInc
		);
	}

	@Test
	@AllowConcurrentEvents
	public void testStartOffsetForMostTokens() throws IOException {
		assertTokenStreamContents(paoding_most_tokens.tokenStream("field", "我有一百元，你有多少钱"), //
				new String[] { "我", "我有", "100", "元", "你", "你有", "有多", "有多少", "多少", "多少钱" },//
				new int[] { 0, 0, 2, 4, 6, 6, 7, 7, 8, 8 },//startOffsets
				new int[] { 1, 2, 4, 5, 7, 8, 9, 10, 10, 11 }//endOffsets
		);
	}

	@Test
	@AllowConcurrentEvents
	public void testLongText() throws IOException {
		StringBuilder buf = new StringBuilder();
		int expectedCount = 0;
		for (int i = 0; i < random().nextInt(1024) + 1; ++i) {
			buf.append(" ");
			expectedCount++;
			for (int j = 0; j < 5; ++j) {
				buf.append("a");
			}
		}
		TokenStream ts = paoding_most_tokens.tokenStream("field", buf.toString());
		ts.reset();
		int count = 0;
		while (ts.incrementToken()) {
			count++;
		}
		ts.close();
		assertEquals(expectedCount, count);
	}

}
