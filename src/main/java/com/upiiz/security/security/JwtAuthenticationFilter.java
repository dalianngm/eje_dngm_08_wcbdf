package com.upiiz.security.security;

import com.upiiz.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //Usemoos el servicio que ya tenemos
    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    //Creamos su constructor, para pasarle una instancia de jwtService y userDetailService
    JwtAuthenticationFilter(final JwtService jwtService, final UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    //Interceptar la solicitud del cliente - Navegador
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        //Traes el token o boleto o reservacion
        String authHeader=request.getHeader("Authorization");
        String jwtToken;
        String username=null;

        //No traigo el token o boleto
        if(authHeader==null || !authHeader.startsWith("Bearer ")) {
            //Bearer - Portador -
            //Aqui no permito el paso, puede ser que otros end point no pidan
            filterChain.doFilter(request, response);
            return;
        }
        //Obtener el Token1|
        jwtToken=authHeader.substring(7);
        //Obtener el nombre de usuario
        //Generalmente cuando tenemos NullPointerExcception - New
        username=jwtService.obtenerNombreUsuario(jwtToken);
        if(username!=null &&
                SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            //Tambien verificamos si el token valido
            if(jwtService.tokenValido(userDetails.getUsername(),jwtToken)){
                //Hacemos la utenticacion de manera implicita
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
