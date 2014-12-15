package com.thihy.es.analysis.paoding.dict;

import org.apache.lucene.index.MergeState.CheckAbort;
import org.elasticsearch.common.Preconditions;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;

public class DictionariesLoadContext {
	private final Index index;
	private final Settings indexSettings;
	private final OwnerType ownerType;
	private final String ownerName;
	private final Settings dictSettings;

	private DictionariesLoadContext(Index index, Settings indexSettings, OwnerType ownerType, String ownerName, Settings dictSettings) {
		super();
		this.index = index;
		this.indexSettings = indexSettings;
		this.ownerType = ownerType;
		this.ownerName = ownerName;
		this.dictSettings = dictSettings;
	}

	public Index getIndex() {
		return index;
	}

	public Settings getIndexSettings() {
		return indexSettings;
	}

	public OwnerType getOwnerType() {
		return ownerType;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public Settings getDictSettings() {
		return dictSettings;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Index index;
		private Settings indexSettings;
		private OwnerType ownerType;
		private String ownerName;
		private Settings dictSettings;

		public Builder index(Index index, Settings indexSettings) {
			this.index = index;
			this.indexSettings = indexSettings;
			return this;
		}

		public Builder analyzerOwner(String name) {
			this.ownerType = OwnerType.ANALYZER;
			this.ownerName = name;
			return this;
		}

		public Builder tokenizerOwner(String name) {
			this.ownerType = OwnerType.TOKENIZER;
			this.ownerName = name;
			return this;
		}

		public Builder filterOwner(String name) {
			this.ownerType = OwnerType.FILTER;
			this.ownerName = name;
			return this;
		}

		public Builder dictSettings(Settings dictSettings) {
			this.dictSettings = dictSettings;
			return this;
		}

		private void validate() {
			Preconditions.checkNotNull(this.index);
			Preconditions.checkNotNull(this.indexSettings);
			Preconditions.checkNotNull(this.ownerType);
			Preconditions.checkNotNull(this.ownerName);
			Preconditions.checkNotNull(this.dictSettings);
		}

		public DictionariesLoadContext build() {
			return new DictionariesLoadContext(index, indexSettings, ownerType, ownerName, dictSettings);
		}
	}

	public static enum OwnerType {
		ANALYZER, //
		TOKENIZER, //
		FILTER, //
		;
	}
}
