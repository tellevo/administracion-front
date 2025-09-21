package cl.tellevo.admin.config;

import cl.tellevo.admin.websocket.VentasWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final VentasWebSocketHandler ventasWebSocketHandler;

    public WebSocketConfig(VentasWebSocketHandler ventasWebSocketHandler) {
        this.ventasWebSocketHandler = ventasWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ventasWebSocketHandler, "/ws/ventas")
                .setAllowedOriginPatterns(
                    "http://localhost:*",
                    "https://*.tellevoapp.com",
                    "https://*tellevoapp.com:*",
                    "https://admin.tellevoapp.com*",
                    "http://*.tellevoapp.com",     // HTTP support for teLlevoapp.com domains
                    "http://*tellevoapp.com:*"     // HTTP support with ports
                );
    }
}
