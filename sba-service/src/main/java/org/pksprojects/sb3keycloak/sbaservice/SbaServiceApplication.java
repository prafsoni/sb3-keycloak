package org.pksprojects.sb3keycloak.sbaservice;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import jakarta.annotation.PostConstruct;
import org.pksprojects.sb3configclientoauth2support.ConfigClientOAuth2Properties;
import org.pksprojects.sb3configclientoauth2support.OAuth2AuthorizedClientHttpRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;

@EnableAdminServer
@SpringBootApplication
public class SbaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbaServiceApplication.class, args);
	}

	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) throws Exception {

		return http
				.authorizeExchange(authorizeExchangeSpec -> {
					authorizeExchangeSpec.pathMatchers("/actuator/health/**").permitAll();
					authorizeExchangeSpec.anyExchange().authenticated();
				})
				.oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()))
				.oauth2Login(Customizer.withDefaults())
				.logout(logoutSpec -> logoutSpec.logoutUrl("/logout").logoutSuccessHandler(this.logoutSuccessHandler(clientRegistrationRepository)))
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.build();
	}

	private ServerLogoutSuccessHandler logoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
		OidcClientInitiatedServerLogoutSuccessHandler successHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
		successHandler.setPostLogoutRedirectUri("{baseUrl}/login?logout");
		return successHandler;
	}

//	@Bean
//	@ConditionalOnMissingBean(ConfigServicePropertySourceLocator.class)
//	@ConditionalOnProperty(prefix = "spring.cloud.config.client.oauth2",
//			name = { "client-id", "client-secret", "access-token-uri" })
//	public ConfigServicePropertySourceLocator configServicePropertySourceLocator(
//			ConfigClientProperties configClientProperties, ConfigClientOAuth2Properties configClientOAuth2Properties) {
//		ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("config-client")
//				.clientId(configClientOAuth2Properties.getClientId())
//				.clientSecret(configClientOAuth2Properties.getClientSecret())
//				.scope(configClientOAuth2Properties.getScope())
//				.tokenUri(configClientOAuth2Properties.getAccessTokenUri())
//				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.build();
//		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.getInterceptors().add(new OAuth2AuthorizedClientHttpRequestInterceptor(clientRegistration));
//
//		ConfigServicePropertySourceLocator configServicePropertySourceLocator = new ConfigServicePropertySourceLocator(
//				configClientProperties);
//		configServicePropertySourceLocator.setRestTemplate(restTemplate);
//		return configServicePropertySourceLocator;
//	}

//	@Bean
//	@Primary
//	public ConfigClientOAuth2Configurer configClientOAuth2Configurator(ConfigServicePropertySourceLocator locator,
//																	   ConfigClientOAuth2Properties configClientOAuth2Properties) {
//		return new ConfigClientOAuth2Configurer(locator, configClientOAuth2Properties);
//	}
//
//	protected static class ConfigClientOAuth2Configurer {
//
//		private final ConfigServicePropertySourceLocator locator;
//
//		private final  ConfigClientOAuth2Properties configClientOAuth2Properties;
//
//		public ConfigClientOAuth2Configurer(ConfigServicePropertySourceLocator locator,
//											ConfigClientOAuth2Properties configClientOAuth2Properties) {
//			this.locator = locator;
//			this.configClientOAuth2Properties = configClientOAuth2Properties;
//		}
//
//		@PostConstruct
//		public void init() {
//			ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("config-client")
//					.clientId(configClientOAuth2Properties.getClientId())
//					.clientSecret(configClientOAuth2Properties.getClientSecret())
//					.scope(configClientOAuth2Properties.getScope())
//					.tokenUri(configClientOAuth2Properties.getAccessTokenUri())
//					.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//					.build();
//			RestTemplate restTemplate = new RestTemplate();
//			restTemplate.getInterceptors().add(new OAuth2AuthorizedClientHttpRequestInterceptor(clientRegistration));
//			this.locator.setRestTemplate(restTemplate);
//		}
//
//	}

//	@Bean
//	@ConditionalOnMissingBean
//	public ConfigClientOAuth2Properties configClientOAuth2Properties() {
//		return new ConfigClientOAuth2Properties();
//	}
}
