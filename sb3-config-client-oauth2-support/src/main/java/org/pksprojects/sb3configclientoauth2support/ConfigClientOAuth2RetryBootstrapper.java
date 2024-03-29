/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pksprojects.sb3configclientoauth2support;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.config.client.ConfigServerBootstrapper.LoaderInterceptor;
import org.springframework.cloud.config.client.ConfigServerConfigDataResource;
import org.springframework.cloud.config.client.RetryProperties;
import org.springframework.cloud.config.client.RetryTemplateFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ClassUtils;

/**
 * Bootstrapper.
 *
 * @author Marcin Grzejszczak
 * @since 3.0.0
 */
public class ConfigClientOAuth2RetryBootstrapper implements BootstrapRegistryInitializer {

	static final boolean RETRY_IS_PRESENT = ClassUtils.isPresent("org.springframework.retry.annotation.Retryable",
			null);

	@Override
	public void initialize(BootstrapRegistry registry) {
		if (!RETRY_IS_PRESENT) {
			return;
		}

		registry.registerIfAbsent(LoaderInterceptor.class, context -> loadContext -> {
//			var config = context.get(Binder.class)
//					.bind(ConfigClientOAuth2Properties.PREFIX, ConfigClientOAuth2Properties.class)
//					.orElseGet(ConfigClientOAuth2Properties::new);
			var config2 = loadContext.getLoaderContext().getBootstrapContext().get(Binder.class)
					.bind(ConfigClientOAuth2Properties.PREFIX, ConfigClientOAuth2Properties.class)
					.orElseGet(ConfigClientOAuth2Properties::new);
			ConfigServerConfigDataResource resource = loadContext.getResource();
			if (resource.getProperties().isFailFast()) {
				RetryProperties properties = resource.getRetryProperties();
				RetryTemplate retryTemplate = RetryTemplateFactory.create(properties, resource.getLog());
				return retryTemplate.execute(retryContext -> {
					if (resource.getLog().isDebugEnabled()) {
						resource.getLog().debug("Retry: count=" + retryContext.getRetryCount());
					}
					return loadContext.getInvocation().apply(loadContext.getLoaderContext(), resource);
				});
			}
			return loadContext.getInvocation().apply(loadContext.getLoaderContext(), resource);
		});

	}

}
