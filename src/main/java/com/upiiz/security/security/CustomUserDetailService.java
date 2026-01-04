package com.upiiz.security.security;

import com.upiiz.security.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    //Hacer uso del servicio
    final UserService userService;

    //Constructor
    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    //Implementamos el metodo que se sobreescribe
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Obtener el usuario de la base de datos
        return userService.getUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(username+" not found"));
    }
}
