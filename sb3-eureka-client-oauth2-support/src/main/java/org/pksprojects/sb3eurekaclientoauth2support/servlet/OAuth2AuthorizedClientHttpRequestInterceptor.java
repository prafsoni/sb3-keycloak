package org.pksprojects.sb3eurekaclientoauth2support.servlet;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

public class OAuth2AuthorizedClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	final ClientRegistration clientRegistration;

	private OAuth2AccessToken accessToken;

	public OAuth2AuthorizedClientHttpRequestInterceptor(ClientRegistration clientRegistration) {
		this.clientRegistration = clientRegistration;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		Instant now = Clock.systemUTC().instant();
		if (accessToken == null || now.isAfter(accessToken.getExpiresAt())) {
			DefaultClientCredentialsTokenResponseClient tokenResponseClient = new DefaultClientCredentialsTokenResponseClient();
			OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest = new OAuth2ClientCredentialsGrantRequest(
					clientRegistration);
			accessToken = tokenResponseClient.getTokenResponse(clientCredentialsGrantRequest).getAccessToken();
		}

		request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue());
		return execution.execute(request, body);
	}
}
