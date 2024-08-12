package site.ph0en1x.task_management_sys.service;

import site.ph0en1x.task_management_sys.model.auth.JwtRequest;
import site.ph0en1x.task_management_sys.model.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
