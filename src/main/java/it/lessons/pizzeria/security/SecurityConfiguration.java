package it.lessons.pizzeria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.requestMatchers("/pizze/create", "/pizze/edit/**").hasAuthority("ADMIN")
			.requestMatchers(HttpMethod.POST, "/pizze/**").hasAuthority("ADMIN")
			.requestMatchers("/ingredienti","/ingredienti/**").hasAuthority("ADMIN")
			.requestMatchers("/pizze","/pizze/**").hasAnyAuthority("USER", "ADMIN")  //va tutto in ordine gerarchico, le cose da tenere chiuse vanno sopra, qui è quel che rimane ossia solo read
			.requestMatchers("/**").permitAll()
			.and().formLogin()
            	.defaultSuccessUrl("/pizze", true)
			.and().formLogin()
			.and().logout()
			.and().exceptionHandling();
		
		return http.build();
	}
	
	@Bean
	DatabaseUserDetailsService userDetailService() {
		return new DatabaseUserDetailsService();
	}
		
	@Bean
	PasswordEncoder passwordEncoder() {
	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}

