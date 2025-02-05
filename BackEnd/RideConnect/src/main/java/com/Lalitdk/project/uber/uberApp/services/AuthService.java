package com.Lalitdk.project.uber.uberApp.services;

import com.Lalitdk.project.uber.uberApp.dto.*;

public interface AuthService {

     LoginResponseDto login(LoginRequestDto loginrequestDto);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId, String vehicleId);

    LoginResponseDto refreshToken(String refreshToken);
}
