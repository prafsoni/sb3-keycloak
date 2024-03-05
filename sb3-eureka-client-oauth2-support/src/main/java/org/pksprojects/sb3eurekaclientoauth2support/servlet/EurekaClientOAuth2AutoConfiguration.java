package org.pksprojects.sb3eurekaclientoauth2support.servlet;

import com.netflix.discovery.EurekaClientConfig;
import org.pksprojects.sb3eurekaclientoauth2support.EurekaClientOAuth2Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.RestTemplateTimeoutProperties;
import org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration;
import org.springframework.cloud.netflix.eureka.http.DefaultEurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfiguration(before = DiscoveryClientOptionalArgsConfiguration.class)
@EnableConfigurationProperties({ EurekaClientOAuth2Properties.class, RestTemplateTimeoutProperties.class })
@ConditionalOnClass({ EurekaClientConfig.class })
@ConditionalOnProperty(prefix = "eureka.client.oauth2", name = { "client-id", "client-secret", "access-token-uri" })
public class EurekaClientOAuth2AutoConfiguration {


	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(name = "org.springframework.web.client.RestTemplate")
	EurekaClientHttpRequestFactorySupplier eurekaClientOAuth2HttpRequestFactorySupplier(
			EurekaClientOAuth2Properties eurekaClientOAuth2Properties,
			RestTemplateTimeoutProperties restTemplateTimeoutProperties) {
		var clientRegistration = ClientRegistration.withRegistrationId("eureka-client")
				.clientId(eurekaClientOAuth2Properties.clientId())
				.clientSecret(eurekaClientOAuth2Properties.clientSecret())
				.scope(eurekaClientOAuth2Properties.scope())
				.tokenUri(eurekaClientOAuth2Properties.accessTokenUri())
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.build();

		var oAuth2AuthorizedClientHttpRequestInterceptor = new OAuth2AuthorizedClientHttpRequestInterceptor(
				clientRegistration);
		var defaultEurekaClientHttpRequestFactorySupplier = new DefaultEurekaClientHttpRequestFactorySupplier(
				restTemplateTimeoutProperties);

		return new EurekaClientOAuth2HttpRequestFactorySupplier(defaultEurekaClientHttpRequestFactorySupplier,
				oAuth2AuthorizedClientHttpRequestInterceptor);
	}

}
