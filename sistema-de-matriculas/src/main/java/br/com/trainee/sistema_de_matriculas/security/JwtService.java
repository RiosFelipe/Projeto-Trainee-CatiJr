package br.com.trainee.sistema_de_matriculas.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret-key}") //busca a chave definida em aplication.properties
    private String secretKey;

    @Value("${jwt.expiration-time}")//busca o tempo de expiracao em aplication.properties tambem
    private long expirationTime;

    private SecretKey getSigningKey() {// Transforma a secretKey na chave do JWT
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String userID){//funcao que gera o token do usuario (aluno)
        return Jwts.builder()
                .subject(userID)// id do aluno vai ser colocado no token
                .issuedAt(new Date(System.currentTimeMillis()))// momento em que foi criado, no caso o momento atual, com data e hora
                .expiration(new Date(System.currentTimeMillis()+expirationTime)) //quando vence, ou seja, momento que foi criado + tempo de expiracao
                .signWith(getSigningKey()) //assina usando a chave que esta em properties
                .compact(); //constroi a string do token 
    }

    public String validateToken(String token) {
        token = token.replace("Bearer ", "");

        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())//Valida a assinatura digital usando a secret-key do properties
                    .build()//Reconstrói o decodificador configurado
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            } catch (ExpiredJwtException e) {
                System.out.println("Token expirado.");
            } catch (MalformedJwtException e) {
                System.out.println("Token inválido.");
            } catch (JwtException e) {
                System.out.println("Erro inesperado ou assinatura inválida: " + e.getMessage());
            }

        return null;
    }
}
