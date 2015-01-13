/**
 * Copyright 2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.paoding.analysis.analyzer.impl;

import java.util.Iterator;
import java.util.LinkedList;

import net.paoding.analysis.analyzer.TokenCollector;
import net.paoding.analysis.knife.Token;

/**
 * 
 * @author Zhiliang Wang [qieqie.wang@gmail.com]
 * 
 * @since 1.1
 */
public class MostWordsTokenCollector implements TokenCollector {

	private final LinkedList<Token> queue = new LinkedList<>();

	@Override
	public void clear() {
		queue.clear();
	}

	/**
	 * Collector接口实现。<br>
	 * 构造词语Token对象，并放置在tokens中
	 * 
	 */
	public void collect(String word, int begin, int end) {
		Token token = new Token(word, begin, end);
		Token lastToken = queue.isEmpty() ? null : queue.getLast();
		if (lastToken != null) {
			assert lastToken.startOffset() <= token.startOffset();
			if (lastToken.startOffset() == token.startOffset()) {
				assert lastToken.endOffset() <= token.endOffset();
				if (lastToken.endOffset() == token.endOffset()) {
					// same range, skip
					return;
				}
			}
		}
		queue.add(token);
	}

	@Override
	public Iterator<Token> iterator() {
		return queue.iterator();
	}
}
