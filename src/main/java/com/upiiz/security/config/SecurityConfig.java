package com.upiiz.security.config;

import com.upiiz.security.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    //Hacer uso de nuestro filtro
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //Constructor
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //Configuramos que endpints van a estar disponible sin autenticacion
    //empresa/public/v1/auth/registro
    //empresa/public/v1/auth/login
    //Para all lo demas se requiere autenticacion


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Cross site request forgery - csrf
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->
                        auth
                            .requestMatchers("/empresa/public/v1/auth/login").permitAll()
                            .requestMatchers("/empresa/public/v1/auth/registro").permitAll()
                            .anyRequest().authenticated()//clientes,productos,categorias - Enviar el Token para cada solicitud
                )
                .sessionManagement(sesiones ->
                        sesiones.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //Son elementos del ecosistemas que estan disponibles cuando se requiera
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //Si se requiere, busca en el ecosistema de Spring Boot
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
