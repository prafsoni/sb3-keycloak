package org.pksprojects.sb3configclientoauth2support;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServerConfigDataResource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;

public class ConfigClientOAuth2BootstrapRegistryInitializer implements BootstrapRegistryInitializer {
	@Override
	public void initialize(BootstrapRegistry registry) {
		registry.register(RestTemplate.class, context -> {
			RestTemplate restTemplate = new RestTemplate();
			var cd = context.isRegistered(ConfigDataResource.class);
			var lo = context.isRegistered(ConfigDataLoaderContext.class);
			var e = context.isRegistered(ConfigurableEnvironment.class);
			var ee = registry.isRegistered(Environment.class);
			final var binder = context.get(Binder.class);
			var bool = binder.bind("eureka.client.enabled", Boolean.class).orElse(true);
			var bool2 = binder.bind("spring.cloud.config.client.oauth2", Boolean.class).orElse(false);
			//BindHandler bindHandler = context.get(BindHandler.class); //getBindHandler(context);
			//var p = binder.bind(ConfigClientOAuth2Properties.PREFIX, Bindable.of(ConfigClientOAuth2Properties.class), bindHandler)
			//		.orElse(null);
			//var result = binder.bind("spring.cloud.config.client.oauth2", ConfigClientOAuth2Properties.class);
			//var r = registry.getRegisteredInstanceSupplier(ConfigurableEnvironment.class);
			//var props = context.get(ConfigClientOAuth2Properties.class);

			var config = context.get(Binder.class).bind(ConfigClientOAuth2Properties.PREFIX, ConfigClientOAuth2Properties.class).orElseGet(ConfigClientOAuth2Properties::new);

//			var supplier = registry.getRegisteredInstanceSupplier(ConfigServerConfigDataResource.class);
//			supplier.get(context);
//			var env =	context.get();
//			var configClientOAuth2Properties = env.getProperty("spring.cloud.config.client.oauth2", ConfigClientOAuth2Properties.class);
			ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("config-client")
					.clientId("sb3-config-client")
					.clientSecret("yCKT1xCZEc3oObmFEeZmL7TqpnZEdO53")
					.scope("openid")
					.tokenUri("http://localhost:8180/realms/sb3-keycloak/protocol/openid-connect/token")
					.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
					.build();
			restTemplate.getInterceptors().add(new OAuth2AuthorizedClientHttpRequestInterceptor(clientRegistration));
			return restTemplate;
		});
	}
}
