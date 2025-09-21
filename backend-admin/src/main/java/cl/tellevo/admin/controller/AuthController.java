package cl.tellevo.admin.controller;

import cl.tellevo.admin.dto.LoginRequest;
import cl.tellevo.admin.dto.LoginResponse;
import cl.tellevo.admin.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import cl.tellevo.admin.service.VentasGrpcClient;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;

    @Autowired
    private VentasGrpcClient ventasGrpcClient;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for user: {}", loginRequest.getUsername());
            
            LoginResponse response = authService.authenticateUser(loginRequest);
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for user: {}", loginRequest.getUsername());
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Credenciales inválidas");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            
        } catch (Exception e) {
            logger.error("Unexpected error during login for user: {}", loginRequest.getUsername(), e);
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "TeLlevo Admin Backend is running");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "TeLlevo Admin Backend is running");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grpc/health")
    public ResponseEntity<Map<String, Object>> grpcHealth() {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean healthy = ventasGrpcClient.isHealthy();
            String connectionInfo = ventasGrpcClient.getConnectionInfo();

            response.put("status", healthy ? "UP" : "DOWN");
            response.put("healthy", healthy);
            response.put("connection", connectionInfo);
            response.put("message", healthy ?
                "gRPC connection is healthy" :
                "gRPC connection is unavailable");
            response.put("timestamp", System.currentTimeMillis());

            return healthy ? ResponseEntity.ok(response) :
                           ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);

        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("healthy", false);
            response.put("error", e.getMessage());
            response.put("message", "Failed to check gRPC connection");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
