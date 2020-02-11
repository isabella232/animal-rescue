package io.spring.cloud.samples.animalrescue.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.pivotal.cfenv.core.CfEnv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		String authDomain = new CfEnv().findCredentialsByLabel("p.gateway").getString("auth_domain");
		if (authDomain != null) {
			httpSecurity.oauth2ResourceServer()
				.jwt().jwkSetUri(authDomain + "/token_keys");
		}

		// @formatter:off
		return httpSecurity
			.httpBasic().disable()
			.csrf().disable()
			.authorizeExchange()
			.pathMatchers("/animals").permitAll()
			.and()
			.authorizeExchange()
			.anyExchange().authenticated()
			.and()
			.oauth2ResourceServer()
			.jwt()
			.and()
			.and()
			.build();
		// @formatter:on
	}
}
