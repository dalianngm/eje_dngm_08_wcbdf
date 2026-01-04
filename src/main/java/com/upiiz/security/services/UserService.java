package com.upiiz.security.services;

import com.upiiz.security.entities.User;
import com.upiiz.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //Constructor
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Recuperarlo por nombre de usuario
    public Optional<User> getUserByUsername(String username) {
        //Verificar la clave y usuario conciden
        return userRepository.findByUsername(username);
    }

    //Guardar un usuario
    public User saveUser(User user) {
        //IMPORTANTE - Ecriptar o codificar la clave - No se nos olvide
        user.setPasswordCodificado(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
