package org.pksprojects.sb3eurekaclientoauth2support.reactive;

import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import org.pksprojects.sb3eurekaclientoauth2support.EurekaClientOAuth2Properties;
import org.pksprojects.sb3eurekaclientoauth2support.servlet.EurekaClientOAuth2AutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration;
import org.springframework.cloud.netflix.eureka.http.WebClientTransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration(before = {EurekaClientOAuth2AutoConfiguration.class, DiscoveryClientOptionalArgsConfiguration.class})
@ConditionalOnClass(name = "org.springframework.web.reactive.function.client.WebClient")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@EnableConfigurationProperties(value = {EurekaClientOAuth2Properties.class})
@ConditionalOnProperty(prefix = "eureka.client.oauth2", name = { "client-id", "client-secret", "access-token-uri" })
public class EurekaWebClientOAuth2AutoConfiguration {

	@Bean
	@ConditionalOnMissingClass("org.glassfish.jersey.client.JerseyClient")
	@ConditionalOnClass(name = "org.springframework.web.reactive.function.client.WebClient")
	@ConditionalOnProperty(prefix = "eureka.client", name = "webclient.enabled", havingValue = "true")
	@ConditionalOnMissingBean(value = TransportClientFactories.class, search = SearchStrategy.CURRENT)
	public WebClientTransportClientFactories webClientTransportClientFactories(EurekaClientOAuth2Properties eurekaClientOAuth2Properties,
																			   ObjectProvider<WebClient.Builder> builder) {
		var webClientBuilder = builder.getIfAvailable();
		Assert.notNull(webClientBuilder, "Unable to get WebClientBuilder from: " + builder);
		final var clientRegistration = ClientRegistration.withRegistrationId("eureka-client")
				.clientId(eurekaClientOAuth2Properties.clientId())
				.clientSecret(eurekaClientOAuth2Properties.clientSecret())
				.scope(eurekaClientOAuth2Properties.scope())
				.tokenUri(eurekaClientOAuth2Properties.accessTokenUri())
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.build();
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = getServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistration);
		webClientBuilder.filter(oauth);
		return new WebClientTransportClientFactories(() -> webClientBuilder);
	}

	private static ServerOAuth2AuthorizedClientExchangeFilterFunction getServerOAuth2AuthorizedClientExchangeFilterFunction(ClientRegistration clientRegistration) {
		final var clientRegistrationRepository = new InMemoryReactiveClientRegistrationRepository(clientRegistration);
		InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
		oauth.setDefaultClientRegistrationId(clientRegistration.getRegistrationId());
		return oauth;
	}
}
