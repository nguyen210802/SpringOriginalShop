package com.example.identityService.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WebSecurityConfig {
    String SIGNER_KEY = "xolCXXnMNhOdT3hIZF5LTFM9koeXWe25Q5RzsG4ZU9x70X1WRHB18ymjIxo0/s6w";

    String[] PUBLIC_POST = {"/users/registration",
            "/auth/login",
            "/auth/introspect",
            "/auth/loginWithGoogle",
            "/auth/refresh-token",
            "/users/confirmOtpAndCreateUser",
            "otp/generate",
            "otp/validate"};
    String[] PUBLIC_GET = {"/users/product",
            "/users/product/getAll",
            "/users/product/comment/getAllByProduct",
            "/users/product/getProducts",
            "/users/product/getAllByName",
            "/users/product/image/getByProduct/**",
            "/users/product/image/getMainImage/**"};
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) -> request
                    .requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()
                    .requestMatchers(PUBLIC_GET).permitAll()
                    .requestMatchers("/OriginShop/login").permitAll()
                    .requestMatchers("/oauth2/authorization/google/**").permitAll()
                        .requestMatchers("/auth/loginWithGoogle").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oath2 ->
                                oath2.jwt(jwtConfigurer ->
                                                jwtConfigurer.decoder(jwtDecoder())
                                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )

                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }
}
