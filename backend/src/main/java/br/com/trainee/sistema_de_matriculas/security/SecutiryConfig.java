package br.com.trainee.sistema_de_matriculas.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecutiryConfig {

    private final SecutiryFilter secutiryFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //ativa a configuração de CORS que definimos abaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) //libera o frontend de acordo com o codigo do corsconfiguration
                .csrf(csrf -> csrf.disable()) //desabilita a protecao contra CSRF (Cross-Site Request Forgery), ja que o jwt nao precisa se preocupar com isso
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/aluno/login").permitAll() //permite que qualquer pessoa faça post em /aluno/login
                    .requestMatchers(HttpMethod.POST, "/aluno").permitAll() //permite que qualquer pessoa faça post em /aluno/
                    .requestMatchers(HttpMethod.POST, "/aluno/esqueci-senha").permitAll()//permite que qualquer pessoa possa alterar a senha
                    .anyRequest().authenticated() //qualquer outra acao em qualquer rota precisa de autenticacao
                )
                .addFilterBefore(secutiryFilter, UsernamePasswordAuthenticationFilter.class) //roda o security filter antes da autenticacao por user e senha do securityfilter
                .build();
    }

    //configura quais origens e métodos são permitidos
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); //permite requisicoes do endereço do frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); //fala quais metodos http o frontend pode fazer
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));//fala quais headers http o front pode enviar, no caso o authorization serve pro jwt
        configuration.setAllowCredentials(true);//permite o envio de headers de autenticacao 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //aplica essas configuracoes em todas as rotas 
        return source;
    }
}