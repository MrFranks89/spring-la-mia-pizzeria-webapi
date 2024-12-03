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
            // Permessi endpoint REST
            .requestMatchers("/api/pizze/**").permitAll() // Consenti l'accesso agli endpoint REST (modifica se necessario)
            
            // Permessi web
            .requestMatchers("/pizze/create", "/pizze/edit/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.POST, "/pizze/**").hasAuthority("ADMIN")
            .requestMatchers("/ingredienti", "/ingredienti/**").hasAuthority("ADMIN")
            .requestMatchers("/pizze", "/pizze/**").hasAnyAuthority("USER", "ADMIN") 
            .requestMatchers("/**").permitAll() // 

            .and().formLogin()
                .defaultSuccessUrl("/pizze", true)
            .and().logout()
            .and().csrf().disable();

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
