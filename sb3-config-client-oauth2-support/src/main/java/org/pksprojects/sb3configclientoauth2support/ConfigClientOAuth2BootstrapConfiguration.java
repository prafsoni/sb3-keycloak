package org.pksprojects.sb3configclientoauth2support;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;

//@AutoConfiguration(before = ConfigServiceBootstrapConfiguration.class)
@Configuration(proxyBeanMethods = false)
//@ConditionalOnClass({ ConfigClientProperties.class })
@EnableConfigurationProperties(ConfigClientOAuth2Properties.class)
public class ConfigClientOAuth2BootstrapConfiguration {

	@Bean
	//@ConditionalOnMissingBean(ConfigServicePropertySourceLocator.class)
//	@ConditionalOnProperty(prefix = "spring.cloud.config.client.oauth2",
//			name = { "client-id", "client-secret", "access-token-uri" })
	public ConfigServicePropertySourceLocator configServicePropertySourceLocator(
			ConfigClientProperties configClientProperties, ConfigClientOAuth2Properties configClientOAuth2Properties) {
		ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("config-client")
				.clientId(configClientOAuth2Properties.getClientId())
				.clientSecret(configClientOAuth2Properties.getClientSecret())
				.scope(configClientOAuth2Properties.getScope())
				.tokenUri(configClientOAuth2Properties.getAccessTokenUri())
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.build();
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new OAuth2AuthorizedClientHttpRequestInterceptor(clientRegistration));

		ConfigServicePropertySourceLocator configServicePropertySourceLocator = new ConfigServicePropertySourceLocator(
				configClientProperties);
		configServicePropertySourceLocator.setRestTemplate(restTemplate);
		return configServicePropertySourceLocator;
	}
}
