package com.project.insurtech.service;

import com.project.insurtech.components.JwtTokenUtil;
import com.project.insurtech.dtos.UserDTO;
import com.project.insurtech.entities.Role;
import com.project.insurtech.entities.User;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.repositories.IRoleRepository;
import com.project.insurtech.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager auth;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //check if phone number already exists
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataNotFoundException("Phone number already exists");
        }

        //check if role exists
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new Exception("You cannot register an Admin account");
        }

        //check retype password
        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new Exception("Password and retype password are not the same");
        }

        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .email(userDTO.getEmail())
//                .facebookAccountId(userDTO.getFacebookAccountId())
//                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        newUser.setRole(role);

        //encrypt password
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws Exception {
    //check exit user by phone number
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Invalid phone number or password"));

        //check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid phone number or password");
        }

        //check role
        if (!user.getRole().getId().equals(roleId)) {
            throw new DataNotFoundException("Role is incorrect");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                phoneNumber,
                password
        );
        auth.authenticate(authenticationToken);

        //generate token
        logger.info("Login successful");
        return jwtTokenUtil.generateToken(user);
    }


}
