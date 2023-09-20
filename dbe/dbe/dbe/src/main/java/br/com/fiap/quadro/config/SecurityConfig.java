package br.com.fiap.quadro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    AuthorizationFilter authorizationFilter;

    @Autowired
    Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationFilter authorizationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/api/registrar").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("open")){
            http.authorizeHttpRequests().anyRequest().permitAll();
        }else{
            http.authorizeHttpRequests().anyRequest().authenticated();
        }

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
