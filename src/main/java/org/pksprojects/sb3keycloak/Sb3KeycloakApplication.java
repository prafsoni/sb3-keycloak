package org.pksprojects.sb3keycloak;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.configuration.SSLContextFactory;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.http.WebClientDiscoveryClientOptionalArgs;
import org.springframework.cloud.netflix.eureka.http.WebClientTransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class Sb3KeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sb3KeycloakApplication.class, args);
	}

	@Bean
	public WebClientDiscoveryClientOptionalArgs discoveryClientOptionalArgs(ReactiveClientRegistrationRepository clientRegistrations,
																			ObjectProvider<WebClient.Builder> builder,
																			TlsProperties tlsProperties) throws GeneralSecurityException, IOException {
		final var webClientBuilder = builder.getIfAvailable();
		InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
		oauth.setDefaultClientRegistrationId("keycloak2");
		webClientBuilder.filter(oauth);
		var result = new WebClientDiscoveryClientOptionalArgs(() -> webClientBuilder);
		setupTLS(result, tlsProperties);
		return result;
	}

	private static void setupTLS(AbstractDiscoveryClientOptionalArgs<?> args, TlsProperties properties)
			throws GeneralSecurityException, IOException {
		if (properties.isEnabled()) {
			SSLContextFactory factory = new SSLContextFactory(properties);
			args.setSSLContext(factory.createSSLContext());
		}
	}

	@Bean
	//@ConditionalOnMissingBean(value = TransportClientFactories.class, search = SearchStrategy.CURRENT)
	public WebClientTransportClientFactories webClientTransportClientFactories(
			ReactiveClientRegistrationRepository clientRegistrations,
			ObjectProvider<WebClient.Builder> builder) {
		final var webClientBuilder = builder.getIfAvailable();
		InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
		oauth.setDefaultClientRegistrationId("keycloak2");
		webClientBuilder.filter(oauth);
		return new WebClientTransportClientFactories(() -> webClientBuilder);
	}
}
