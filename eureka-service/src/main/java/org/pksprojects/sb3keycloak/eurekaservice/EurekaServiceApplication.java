package org.pksprojects.sb3keycloak.eurekaservice;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.Jersey3DiscoveryClientOptionalArgs;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.configuration.SSLContextFactory;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.security.GeneralSecurityException;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServiceApplication.class, args);
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(a -> {
					a.requestMatchers("/actuator/health", "/actuator/health/**").permitAll();
					a.anyRequest().authenticated();
				})
				.oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer.jwt(Customizer.withDefaults()));
		http.oauth2Login(Customizer.withDefaults());
		return http.build();
	}


//	@Bean
//	public Jersey3DiscoveryClientOptionalArgs discoveryClientOptionalArgs(TlsProperties tlsProperties) throws GeneralSecurityException, IOException {
//		Jersey3DiscoveryClientOptionalArgs result = new Jersey3DiscoveryClientOptionalArgs();
//		setupTLS(result, tlsProperties);
//		result.setAdditionalFilters();
//		return result;
//	}
//
//	private static void setupTLS(AbstractDiscoveryClientOptionalArgs<?> args, TlsProperties properties)
//			throws GeneralSecurityException, IOException {
//		if (properties.isEnabled()) {
//			SSLContextFactory factory = new SSLContextFactory(properties);
//			args.setSSLContext(factory.createSSLContext());
//		}
//	}

}
