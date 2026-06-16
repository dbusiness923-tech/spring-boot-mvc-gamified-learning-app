package com.example.group35_base.config;

import com.example.group35_base.service.UserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetails userDetails;

    public SecurityConfig(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/register", "/login").permitAll()
                                .requestMatchers("/welcome", "/templates/profile", "/templates/profile/edit").authenticated() // Authenticated pages
                                .anyRequest().authenticated()).
                formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                                .defaultSuccessUrl("/welcome", true).permitAll()).logout(logout -> logout
                                .logoutSuccessUrl("/login").permitAll()).userDetailsService(userDetails);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
