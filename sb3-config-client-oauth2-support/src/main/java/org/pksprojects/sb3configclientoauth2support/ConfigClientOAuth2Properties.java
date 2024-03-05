package org.pksprojects.sb3configclientoauth2support;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = ConfigClientOAuth2Properties.PREFIX)
public class ConfigClientOAuth2Properties {

	protected static final String PREFIX = "spring.cloud.config.client.oauth2";

	private String clientId;

	private String clientSecret;

	private String accessTokenUri;

	private Set<String> scope;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

	public void setAccessTokenUri(String accessTokenUri) {
		this.accessTokenUri = accessTokenUri;
	}

	public Set<String> getScope() {
		return scope;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}
}
