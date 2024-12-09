package com.project.insurtech.service.Impl;

import com.project.insurtech.components.JwtTokenUtil;
import com.project.insurtech.components.mappers.UserMapper;
import com.project.insurtech.dtos.ProviderDTO;
import com.project.insurtech.dtos.UserDTO;
import com.project.insurtech.entities.Provider;
import com.project.insurtech.entities.Role;
import com.project.insurtech.entities.User;
import com.project.insurtech.enums.RoleEnum;
import com.project.insurtech.enums.UserStatusEnum;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.repositories.IProviderRepository;
import com.project.insurtech.repositories.IRoleRepository;
import com.project.insurtech.repositories.IUserRepository;
import com.project.insurtech.responses.User.UserDetailResponse;
import com.project.insurtech.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager auth;
    private final IProviderRepository providerRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        //check if phone number already exists
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataNotFoundException("Phone number already exists");
        }

        //check if role exists
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN) ||
                role.getName().toUpperCase().equals(Role.PROVIDER)) {
            throw new Exception("You cannot register an Admin or Provider account");
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
                .status(UserStatusEnum.ACTIVE.getValue())
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

    @Override
    @Transactional
    public User createProvider(ProviderDTO providerDTO) throws Exception {
        //check if phone number already exists
        if (userRepository.existsByPhoneNumber(providerDTO.getPhoneNumber())) {
            throw new DataNotFoundException("Phone number already exists");
        }

        //check if email already exists
        if (userRepository.existsByEmail(providerDTO.getEmail())) {
            throw new DataNotFoundException("Email already exists");
        }

        //convert from providerDTO => user
        User newUser = User.builder()
                .fullName(providerDTO.getFullName())
                .phoneNumber(providerDTO.getPhoneNumber())
                .address(providerDTO.getAddress())
                .email(providerDTO.getEmail())
                .status(1)
                .build();
        newUser.setRole(roleRepository.findById(RoleEnum.PROVIDER.getValue().longValue())
                .orElseThrow(() -> new DataNotFoundException("Role not found")));

        //encrypt password
        if (!Objects.equals(providerDTO.getPassword(), providerDTO.getRetypePassword())) {
            throw new DataNotFoundException("Password and retype password are not the same");
        }
        newUser.setPassword(passwordEncoder.encode(providerDTO.getPassword()));
        // Save the User entity first to generate the ID
        User savedUser = userRepository.save(newUser);

        // Create and associate the Provider entity with the saved User
        Provider provider = Provider.builder()
                .name(providerDTO.getFullName())
                .description(providerDTO.getDescription())
                .user(savedUser) // Set the saved User
                .build();
        providerRepository.save(provider);
        return savedUser;
    }

    @Override
    public UserDetailResponse getUserDetail(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return UserDetailResponse.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        //check if phone number already exists
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber()) &&
                !user.getPhoneNumber().equals(userDTO.getPhoneNumber())) {
            throw new DataNotFoundException("Phone number already exists");
        }

        //check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail()) &&
                !user.getEmail().equals(userDTO.getEmail())) {
            throw new DataNotFoundException("Email already exists");
        }

        //convert from userDTO => user
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        if (userDTO.getPassword() != null && userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllProviders() throws Exception {
        List<User> providers =
                userRepository.findAllUserByRoleIdAndStatus(RoleEnum.PROVIDER.getValue(), UserStatusEnum.ACTIVE.getValue());
        return providers.stream()
                .map(userMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsers() throws Exception {
        List<User> users = userRepository.findAllUserByRoleIdAndStatus(RoleEnum.USER.getValue(), UserStatusEnum.ACTIVE.getValue());
        return users.stream()
                .map(userMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    private String generateRandomPassword() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }


}
