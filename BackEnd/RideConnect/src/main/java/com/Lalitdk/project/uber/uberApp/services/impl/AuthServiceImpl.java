package com.Lalitdk.project.uber.uberApp.services.impl;

import com.Lalitdk.project.uber.uberApp.Security.JWTService;
import com.Lalitdk.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.Lalitdk.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.Lalitdk.project.uber.uberApp.repositories.UserRepository;
import com.Lalitdk.project.uber.uberApp.dto.DriverDto;
import com.Lalitdk.project.uber.uberApp.dto.LoginRequestDto;
import com.Lalitdk.project.uber.uberApp.dto.LoginResponseDto;
import com.Lalitdk.project.uber.uberApp.dto.SignupDto;
import com.Lalitdk.project.uber.uberApp.dto.UserDto;
import com.Lalitdk.project.uber.uberApp.entities.Driver;
import com.Lalitdk.project.uber.uberApp.entities.User;
import com.Lalitdk.project.uber.uberApp.entities.enums.Role;
import com.Lalitdk.project.uber.uberApp.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverSevice;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final SessionService sessionService;


    @Override
     public LoginResponseDto login(LoginRequestDto loginRequestDto) {
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
            );
            User user = (User) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            sessionService.generateNewSession(user,refreshToken);
            return new LoginResponseDto(user.getId(), accessToken, refreshToken);
        }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(user != null)
                throw new RuntimeConflictException("Cannot signup, User already exists with email "+signupDto.getEmail());

        User mappedUser = modelMapper.map(signupDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);


//        create user related entities
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId)
        .orElseThrow(()-> new ResourceNotFoundException("User not found with id" + userId));
        if(user.getRoles().contains(Role.DRIVER)){
            throw new RuntimeConflictException("User with id" + userId + "is already a Driver");
        }
        Driver createDriver = Driver .builder()
                .user(user)
                .available(true)
                .rating(0.0)
                .vehicleId(vehicleId)
                .build();

        user.getRoles().add(Role.DRIVER);
        userRepository.save(user);
        Driver savedDriver = driverSevice.createNewDriver(createDriver);
        return modelMapper.map(savedDriver, DriverDto.class);

    }

    @Override
    public  LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtService.generateUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException(
                "User not found with id" + userId
        ));
        String newAccessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(), newAccessToken , refreshToken);}

    }
  

    
