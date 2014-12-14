package com.thihy.es.analysis.paoding.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.knife.Dictionaries;

import org.elasticsearch.common.Classes;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class FileDictionariesLoader implements DictionariesLoader {
	private static final ESLogger LOG = Loggers.getLogger(FileDictionariesLoader.class);

	private static final String DEFAULT_DICT_RESOURCE_DIR = "com/thihy/es/analysis/paoding/dict";

	private static final String DEFAULT_FILE_SUFFIX = ".dic";
	private static final EnumMap<DictionaryType, String> FILE_NAME_CONFIGS;
	private static final boolean DEFAULT_FAIL_IF_ERROR = false;

	static {
		EnumMap<DictionaryType, String> tmpFileNameConfigs = new EnumMap<>(DictionaryType.class);
		tmpFileNameConfigs.put(DictionaryType.WORDS, "words");
		tmpFileNameConfigs.put(DictionaryType.STOP, "stop");
		tmpFileNameConfigs.put(DictionaryType.NOISE, "noise");
		tmpFileNameConfigs.put(DictionaryType.UNIT, "unit");
		tmpFileNameConfigs.put(DictionaryType.SURNAME, "surname");
		tmpFileNameConfigs.put(DictionaryType.COMB, "comb");
		FILE_NAME_CONFIGS = tmpFileNameConfigs;
	}

	private final DictionaryLoader dictionaryLoader;
	private final String path;
	private final String fileSuffix;
	private final Map<DictionaryType, String> fileNames;
	private final boolean failIfError;

	@Inject
	public FileDictionariesLoader(DictionaryLoader dictionaryLoader, @Assisted Settings settings) {
		super();
		this.dictionaryLoader = dictionaryLoader;
		this.failIfError = settings.getAsBoolean("fail_if_error", settings.getAsBoolean("failIfError", DEFAULT_FAIL_IF_ERROR));
		this.path = settings.get("path");
		if (this.path == null) {
			this.fileSuffix = DEFAULT_FILE_SUFFIX;
			this.fileNames = ImmutableMap.copyOf(FILE_NAME_CONFIGS);
		} else {
			this.fileSuffix = settings.get("suffix", DEFAULT_FILE_SUFFIX);
			Map<DictionaryType, String> tmpFileNames = Maps.newHashMap();
			for (Entry<DictionaryType, String> fileNameConfigEntry : FILE_NAME_CONFIGS.entrySet()) {
				String fileName = settings.get(fileNameConfigEntry.getValue(), fileNameConfigEntry.getValue());
				tmpFileNames.put(fileNameConfigEntry.getKey(), fileName);
			}
			fileNames = ImmutableMap.copyOf(tmpFileNames);
		}
	}

	@Override
	public Dictionaries load() throws IOException {
		MappedDictionaries dictionaries = new MappedDictionaries();
		for (DictionaryType dictionaryType : DictionaryType.values()) {
			String fileName = fileNames.get(dictionaryType);
			if (fileName == null) {
				fileName = dictionaryType.name().toLowerCase(Locale.ROOT);
			}
			Dictionary dictionary = loadDictionary(dictionaryType, fileName);
			if (dictionary != null) {
				dictionaries.setDictionary(dictionaryType, dictionary);
			}
		}
		return dictionaries;
	}

	private Dictionary loadDictionary(DictionaryType dictionaryType, String fileName) throws IOException {
		InputStream in = null;
		String localPath = this.path;
		if (localPath == null) {
			localPath = DEFAULT_DICT_RESOURCE_DIR;
		}

		String filePath;
		if (!localPath.endsWith("/")) {
			filePath = localPath + "/" + fileName + fileSuffix;
		} else {
			filePath = localPath + fileName + fileSuffix;
		}

		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logExceptionWhenLoadDictonary(dictionaryType, file, e);
			}
		} else {
			ClassLoader classLoader = this.getClass().getClassLoader();
			if (classLoader == null) {
				classLoader = Classes.getDefaultClassLoader();
			}
			in = classLoader.getResourceAsStream(filePath);
		}
		if (in == null) {
			LOG.debug("Skip to load [{}] dict from path [{}].", dictionaryType, file);
			return null;
		}
		try {
			LOG.debug("Begin to load [{}] dict from file [{}].", dictionaryType, file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			return dictionaryLoader.loadDictionary(reader);
		} catch (UnsupportedEncodingException e) {
			logExceptionWhenLoadDictonary(dictionaryType, file, e);
		} catch (IOException e) {
			logOrRethrowExceptionWhenLoadDictonary(dictionaryType, file, e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logOrRethrowExceptionWhenLoadDictonary(dictionaryType, file, e);
			} finally {
				LOG.debug("End to load [{}] dict from file [{}].", dictionaryType, file);
			}
		}
		return null;
	}

	private <T extends Exception> void logOrRethrowExceptionWhenLoadDictonary(DictionaryType dictionaryType, File file, T exception)
			throws T {
		if (failIfError) {
			throw exception;
		} else {
			logExceptionWhenLoadDictonary(dictionaryType, file, exception);
		}
	}

	private <T extends Exception> void logExceptionWhenLoadDictonary(DictionaryType dictionaryType, File file, T exception) {
		LOG.warn("Failed to read [{}] dictionary, the file path is [{}].", exception, dictionaryType, file);
	}
}
