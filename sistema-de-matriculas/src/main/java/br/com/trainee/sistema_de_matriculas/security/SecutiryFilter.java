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
public class SecutiryFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private  IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        
        String header = request.getHeader("Authorization");

        if (header != null) {
            String subjectId = jwtService.validateToken(header);

            if (subjectId != null){
                
                userRepository.findById(UUID.fromString(subjectId))
                .map(usuario -> new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList()))
                .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
