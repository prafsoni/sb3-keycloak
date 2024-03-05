package org.pksprojects.sb3eurekaclientoauth2support.servlet;

import org.springframework.cloud.netflix.eureka.http.DefaultEurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.List;

public class EurekaClientOAuth2HttpRequestFactorySupplier  implements EurekaClientHttpRequestFactorySupplier {

	private final EurekaClientHttpRequestFactorySupplier defaultEurekaClientHttpRequestFactorySupplier;

	private final OAuth2AuthorizedClientHttpRequestInterceptor oAuth2AuthorizedClientHttpRequestInterceptor;

	public EurekaClientOAuth2HttpRequestFactorySupplier(
			DefaultEurekaClientHttpRequestFactorySupplier defaultEurekaClientHttpRequestFactorySupplier,
			OAuth2AuthorizedClientHttpRequestInterceptor oAuth2AuthorizedClientHttpRequestInterceptor) {
		this.defaultEurekaClientHttpRequestFactorySupplier = defaultEurekaClientHttpRequestFactorySupplier;
		this.oAuth2AuthorizedClientHttpRequestInterceptor = oAuth2AuthorizedClientHttpRequestInterceptor;
	}

	@Override
	public ClientHttpRequestFactory get(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
		var clientHttpRequestFactory = defaultEurekaClientHttpRequestFactorySupplier.get(sslContext, hostnameVerifier);

		return new InterceptingClientHttpRequestFactory(clientHttpRequestFactory,
				List.of(oAuth2AuthorizedClientHttpRequestInterceptor));
	}
}
