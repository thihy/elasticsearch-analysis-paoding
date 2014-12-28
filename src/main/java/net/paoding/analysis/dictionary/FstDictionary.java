package net.paoding.analysis.dictionary;

import com.google.common.collect.Lists;
import net.paoding.analysis.dictionary.support.ListDictionary;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by thihy on 14/12/28.
 */
public class FstDictionary implements ListDictionary {

	private final Word[] words;
	private final FST<Long> fst;

	public FstDictionary(Word[] sortedWords) {
		this.words = removeDup(sortedWords);

		// 2. build fst: word text -> word index
		try {
			PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
			Builder<Long> builder = new Builder<>(FST.INPUT_TYPE.BYTE4, outputs);
			final IntsRefBuilder scratchIntsRef = new IntsRefBuilder();
			for (int wordIndex = 0; wordIndex < words.length; ++wordIndex) {
				Word word = words[wordIndex];
				builder.add(Util.toUTF32(word.getText(), scratchIntsRef), Long.valueOf(wordIndex));
			}
			this.fst = builder.finish();
		} catch (IOException e) {
			throw new IllegalStateException("Failed to build fst");
		}
		//
	}


	@Override
	public int size() {
		return this.words.length;
	}

	@Override
	public Word get(int index) {
		return this.words[index];
	}

	@Override
	public Hit search(CharSequence fullText, int offset, int count) {
		CharSequence searchingText = fullText.subSequence(offset, offset + count);
		IntsRefFSTEnum<Long> intsRefFSTEnum = new IntsRefFSTEnum<>(fst);
		final IntsRefBuilder scratchIntsRef = new IntsRefBuilder();
		IntsRef inputIntsRef = Util.toUTF32(searchingText, scratchIntsRef);
		IntsRefFSTEnum.InputOutput<Long> inputOutput = null;
		try {
			inputOutput = intsRefFSTEnum.seekCeil(inputIntsRef);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to seekCeil.");
		}
		int targetWordIndex = inputOutput.output.intValue();
		Word targetWord = this.words[targetWordIndex];
		// 是否以待搜索词为前缀，如果不是，则肯定没有找到待搜索词，则返回找不到
		if (!startsWith(targetWord.getText(), searchingText)) {
			return Hit.UNDEFINED;
		}
		// 既然已经时前缀了，如果同时长度也相等，则说明是相等的
		if (targetWord.getText().length() == searchingText.length()) {
			Word nextWord = null;
			if (targetWordIndex + 1 < this.words.length) {
				nextWord = this.words[targetWordIndex + 1];
			}
			return Hit.exactHit(targetWord, nextWord);
		}
		// 以搜索词为前缀，但不是相等的
		return Hit.prefixHit(targetWord);
	}

	private static Word[] removeDup(final Word[] words) {
		if (words.length == 0 || words.length == 1) {
			return words;
		}
		boolean hasDup = false;
		for (int wordIndex = 1; wordIndex < words.length; ++wordIndex) {
			if (words[wordIndex - 1].getText().equals(words[wordIndex].getText())) {
				hasDup = true;
				break;
			}
		}
		if (!hasDup) {
			return words;
		}
		//
		List<Word> noDupWordList = Lists.newArrayListWithCapacity(words.length - 1);
		Word lastWord = words[0];
		noDupWordList.add(lastWord);
		for (int wordIndex = 1; wordIndex < words.length; ++wordIndex) {
			Word word =words[wordIndex];
			if (lastWord.getText().equals(word.getText())) {
				continue;
			}
			noDupWordList.add(word);
			lastWord = word;
		}

		return noDupWordList.toArray(new Word[noDupWordList.size()]);
	}

	private static boolean startsWith(CharSequence src, CharSequence target) {
		if (src.length() < target.length()) {
			return false;
		}
		for (int charIndex = 0, charEnd = target.length(); charIndex < charEnd; ++charIndex) {
			char srcChar = src.charAt(charIndex);
			char targetChar = target.charAt(charIndex);
			if (srcChar != targetChar) {
				return false;
			}
		}
		return true;
	}
}
