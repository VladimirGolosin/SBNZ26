package com.ftn.sbnz.leservice;

import com.ftn.sbnz.dasmodel.User;
import com.ftn.sbnz.dto.RegisterRequestDTO;
import com.ftn.sbnz.dto.UserDTO;
import com.ftn.sbnz.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ftn.sbnz.dto.LoginRequestDTO;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequestDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
    }

    public UserDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        UserDTO result = new UserDTO();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setSurname(user.getSurname());
        result.setEmail(user.getEmail());

        return result;
    }

    
}