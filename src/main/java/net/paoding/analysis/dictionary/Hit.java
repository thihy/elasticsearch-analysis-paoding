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
package net.paoding.analysis.dictionary;

import org.elasticsearch.common.hash.HashCode;

/**
 * Hit是检索字典时返回的结果。检索字典时，总是返回一个非空的Hit对象表示可能的各种情况。
 * <p/>
 * <p/>
 * Hit对象包含2类判断信息：
 * <li>要检索的词语是否存在于词典中: {@link #isHit()}</li>
 * <li>词典是否含有以给定字符串开头的其他词语: {@link #isUnclosed()}</li>
 * <br>
 * 如果上面2个信息都是否定的，则 {@link #isUndefined()}返回true，否则返回false. <br>
 * <br>
 * <p/>
 * 如果{@link #isHit()}返回true，则{@link #getWord()}返回查找结果，{@link #getNext()}返回下一个词语。<br>
 * 如果{@link #isHit()}返回false，但{@link #isUnclosed()}返回true，{@link #getNext()}返回以所查询词语开头的位置最靠前的词语。
 * <p/>
 *
 * @author Zhiliang Wang [qieqie.wang@gmail.com]
 * @see Dictionary
 * @see BinaryDictionary
 * @see FstDictionary
 * @since 1.0
 */
public class Hit {

	// -------------------------------------------------

	public final static Hit UNDEFINED = new Hit(null, null);

	// -------------------------------------------------


	private final Word word;


	private final Word next;

	// -------------------------------------------------

	public static Hit exactHit(Word word, Word next) {
		assert word != null;
		return new Hit(word, next);
	}

	public static Hit prefixHit(Word next) {
		assert next != null;
		return new Hit(null, next);
	}

	public static Hit unmatch() {
		return UNDEFINED;
	}

	/**
	 * @param word 查找命中时，词典中相应的词
	 * @param next 词典中命中词的下一个单词，或{@link #isUnclosed()}为true时最接近的下一个词(参见本类的注释)
	 */
	private Hit(Word word, Word next) {
		this.word = word;
		this.next = next;
	}

	// -------------------------------------------------

	/**
	 * 查找命中时，词典中相应的词
	 */
	public Word getWord() {
		return word;
	}

	/**
	 * 词典中命中词的下一个单词，或{@link #isUnclosed()}为true时最接近的下一个词(参见本类的注释)
	 *
	 * @return
	 */
	public Word getNext() {
		return next;
	}

	/**
	 * 是否在字典中检索到要检索的词语
	 *
	 * @return
	 */
	public boolean isHit() {
		return this.word != null;
	}

	/**
	 * 是否有以当前检索词语开头的词语
	 *
	 * @return
	 */
	public boolean isUnclosed() {
		return this.word == null && this.next != null;
	}

	/**
	 * 字典中没有当前检索的词语，或以其开头的词语
	 *
	 * @return
	 */
	public boolean isUndefined() {
		return this.word == null && this.next == null;
	}


	// -------------------------------------------------

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((word == null) ? 0 : word.hashCode());
		result = PRIME * result + ((next == null) ? 0 : next.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Hit other = (Hit) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		if (this.next == null) {
			if (other.next != null) return false;
			else if (!next.equals(other.next)) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		if (isUnclosed()) {
			return "[UNCLOSED]";
		} else if (isUndefined()) {
			return "[UNDEFINED]";
		}
		return word.toString();
	}

}
