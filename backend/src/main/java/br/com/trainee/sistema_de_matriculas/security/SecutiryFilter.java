package br.com.trainee.sistema_de_matriculas.security;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.trainee.sistema_de_matriculas.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecutiryFilter extends OncePerRequestFilter{ //once per request pq roda uma vez por requisicao
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private  IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        
        String header = request.getHeader("Authorization"); //pega o header "authorization"

        if (header != null) { //se tem um token
            String subjectId = jwtService.validateToken(header);

            if (subjectId != null){ // se o token for valido
                
                userRepository.findById(UUID.fromString(subjectId)) //procura o aluno no banco de dados pelo id extraido
                .map(usuario -> new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList()))
                .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth)); //libera o acesso as rotas que exigem token
            }
        }

        filterChain.doFilter(request, response); //se o token e valido, vai pro controller. se nao tinha, decide se barra ou nao na securityconfig
    }
    
}
