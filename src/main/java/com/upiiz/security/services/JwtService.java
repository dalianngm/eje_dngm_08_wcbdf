package com.upiiz.security.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

//Gnenera un Bean - Frijolito - Es un elemento que se crea en el ecosistem
//Si se requiere se busca, se encuentra y es usado
@Service
public class JwtService {
    //Duracion de un dia = 60 segundo * 60 minutos * 24 horas * 1000 para convertirlo a milisegundos
    private int TIEMPO_PARA_EXPIRAR=60*60*24*1000;
    private String LLAVE_SECRETA = "Mi_llave_muy_muy_secreta_que_no_la_debo_de_compartir";

    //Generar el Token
    //No registramos Claims = Nombre completo, Rol, Telefono, etc
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TIEMPO_PARA_EXPIRAR))
                .signWith(obtenerLLave(), SignatureAlgorithm.HS256)
                .compact(); //user1
    }
    //Obtener el usuario del Token - Decodifica
    public String obtenerNombreUsuario(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerLLave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); //user1
    }
    //Obtener si el token el valido
    //1.- No esta vencido
    //2.- Nombre de usuario le corresponde al token
    //user2
    public boolean tokenValido(String userName, String token) {
        return obtenerNombreUsuario(token).equals(userName) && !estaExpirado(token);
    }
    //Verificar si esta expirado
    public boolean estaExpirado(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerLLave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
    //Generar una llave para firmarlo
    private Key obtenerLLave() {
        return Keys.hmacShaKeyFor(LLAVE_SECRETA.getBytes());
    }

}
