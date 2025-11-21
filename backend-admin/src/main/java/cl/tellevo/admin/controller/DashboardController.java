package cl.tellevo.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        Map<String, Object> data = new HashMap<>();
        // Mock data matching frontend expectations
        data.put("usuariosActivos", 1247);
        data.put("viajesCompletados", 3589);
        data.put("kmsCompartidos", 45678.5);
        data.put("co2Ahorrado", 2341.8);
        data.put("pagosRealizados", 89456);

        return ResponseEntity.ok(data);
    }
}
