package net.paoding.analysis.dictionary.support;

import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.Word;

/**
 * Created by thihy on 14/12/28.
 */
public interface ListDictionary extends Dictionary {

	int size();


	Word get(int index);
}
