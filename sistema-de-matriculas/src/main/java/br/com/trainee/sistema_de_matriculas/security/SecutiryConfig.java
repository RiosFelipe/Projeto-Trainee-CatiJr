package br.com.trainee.sistema_de_matriculas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // habilita segurança personalizada
public class SecutiryConfig {

    @Autowired
    private SecutiryFilter secutiryFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())//desabilita o csrf, ja que em api rest com jwt nao e necessaria essa protecao
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //nao guarda estado no servidor
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/aluno/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/aluno/").permitAll()
                    .anyRequest().authenticated() //qualquer outra requisição que não seja de login/cadastro exige autenticação 
                )
                .addFilterBefore(secutiryFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
