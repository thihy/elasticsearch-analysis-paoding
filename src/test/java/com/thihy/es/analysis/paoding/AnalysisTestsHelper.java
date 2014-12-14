/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.thihy.es.analysis.paoding;

import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.elasticsearch.plugins.PluginsModule;
import org.elasticsearch.plugins.PluginsService;

public class AnalysisTestsHelper {

	public static AnalysisService createAnalysisServiceFromClassPath(String resource) {
		Settings settings = ImmutableSettings.settingsBuilder().loadFromClasspath(resource).build();

		return createAnalysisServiceFromSettings(settings);
	}

	public static AnalysisService createAnalysisServiceFromSettings(Settings settings) {
		Index index = new Index("test");
		if (settings.get(IndexMetaData.SETTING_VERSION_CREATED) == null) {
			settings = ImmutableSettings.builder().put(settings).put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT).build();
		}
		Environment env = new Environment(settings);
		PluginsService pluginsService = new PluginsService(settings, env);
		Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings), new EnvironmentModule(new Environment(settings)),
				new IndicesAnalysisModule(), new PluginsModule(settings, pluginsService)).createInjector();

		AnalysisModule analysisModule = new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class));
		pluginsService.processModule(analysisModule);
		Injector injector = new ModulesBuilder().add(new IndexSettingsModule(index, settings), new IndexNameModule(index), analysisModule)
				.createChildInjector(parentInjector);

		return injector.getInstance(AnalysisService.class);
	}
}
