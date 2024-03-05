package org.pksprojects.sb3keycloak.config;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.configuration.SSLContextFactory;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.http.WebClientDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		return httpSecurity.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
						.pathMatchers("/actuator/health/**")
						.permitAll()
						//.pathMatchers("/actuator/**").hasAnyRole("ROLE_ACTUATOR")
						.anyExchange()
						.authenticated()
				)
				.oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()))
				.build();
	}


}
