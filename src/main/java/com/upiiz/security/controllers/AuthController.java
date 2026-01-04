package com.upiiz.security.controllers;

import com.upiiz.security.entities.User;
import com.upiiz.security.services.JwtService;
import com.upiiz.security.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//Este es un conrolador
@RestController
//Especificar una ruta general
@RequestMapping("/empresa/public/v1/auth")
public class AuthController {
    //Estos END POINT NO REQUIEREN ESTAR AUTENTICADOS
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    //Registro de un usuario - POST
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody User user) {
        //Verificar que no exista otro nombre de usuario igual
        if(userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(409).body(
                    Map.of("estado",0,
                            "mensaje", "El usuario ya existe")
            );
        }else //Verificar que si se pudo registrar el usuario
            return ResponseEntity.status(201).body(
                    Map.of("estado",1,
                            "mensaje", "Usuario registrado",
                            "usuario", userService.saveUser(user))
            );
    }

    //Login de un usuario - POST - Regresamos el JWT = Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User usuario = userService.getUserByUsername(user.getUsername()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body(
                    Map.of("estado",0,
                            "mensaje","Usuario no encontrado"
            ));
        }else
            return ResponseEntity.status(200).body(
                    Map.of("estado",1,
                            "mensaje","Usuario logueado",
                            "token", jwtService.generateJwtToken(usuario.getUsername()))
            );
    }

    @GetMapping("/productos")
    public String productos() {
        return "productos";
    }

    @GetMapping("/permiso")
    public String saludar(){
        return "Hola";
    }
}
