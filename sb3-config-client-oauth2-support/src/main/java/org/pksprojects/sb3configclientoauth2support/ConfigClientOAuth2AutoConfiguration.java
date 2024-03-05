package org.pksprojects.sb3configclientoauth2support;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ConfigClientOAuth2Properties.class)
public class ConfigClientOAuth2AutoConfiguration {

//	@Bean
//	@ConditionalOnMissingBean
//	public ConfigClientOAuth2Properties configClientOAuth2Properties(Environment environment, ApplicationContext context) {
//		if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(),
//				ConfigClientOAuth2Properties.class).length > 0) {
//			return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), ConfigClientOAuth2Properties.class);
//		}
//		ConfigClientOAuth2Properties client = new ConfigClientOAuth2Properties();
//		return client;
//	}
}
