package org.pksprojects.sb3eurekaclientoauth2support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Set;

@ConfigurationProperties(prefix = "eureka.client.oauth2")
public record EurekaClientOAuth2Properties(
		String clientId,

		String clientSecret,

		String accessTokenUri,

		Set<String> scope
) {

	@ConstructorBinding
	public EurekaClientOAuth2Properties {

	}
}
