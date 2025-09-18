package cl.tellevo.admin.service;

import cl.tellevo.admin.dto.LoginRequest;
import cl.tellevo.admin.dto.LoginResponse;
import cl.tellevo.admin.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${spring.security.user.name}")
    private String adminUsername;
    
    @Value("${spring.security.user.password}")
    private String adminPassword;
    
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try {
            logger.debug("Attempting authentication for user: {}", loginRequest.getUsername());
            
            // For this demo, we're using hardcoded credentials from application.properties
            // In production, you would validate against a database
            if (adminUsername.equals(loginRequest.getUsername()) && 
                adminPassword.equals(loginRequest.getPassword())) {
                
                // Generate JWT token
                String token = jwtUtil.generateJwtToken(loginRequest.getUsername());
                
                logger.info("Authentication successful for user: {}", loginRequest.getUsername());
                return new LoginResponse(token, "Inicio de sesión exitoso");
                
            } else {
                logger.warn("Authentication failed for user: {}", loginRequest.getUsername());
                throw new BadCredentialsException("Credenciales inválidas");
            }
            
        } catch (Exception e) {
            logger.error("Authentication error for user: {}", loginRequest.getUsername(), e);
            throw new BadCredentialsException("Error al iniciar sesión");
        }
    }
}
