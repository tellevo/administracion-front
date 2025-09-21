package cl.tellevo.admin.websocket;

import cl.tellevo.admin.service.VentasGrpcClient;
import cl.tellevo.admin.grpc.ventas.VentasResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VentasWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(VentasWebSocketHandler.class);

    private final VentasGrpcClient grpcClient;
    private final ObjectMapper objectMapper;
    private final Map<String, StreamObserver<VentasResponse>> activeStreams;
    private final Map<String, WebSocketSession> activeSessions;

    @Autowired
    public VentasWebSocketHandler(VentasGrpcClient grpcClient, ObjectMapper objectMapper) {
        this.grpcClient = grpcClient;
        this.objectMapper = objectMapper;
        this.activeStreams = new ConcurrentHashMap<>();
        this.activeSessions = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("WebSocket connection established: {}", session.getId());

        activeSessions.put(session.getId(), session);

        // Create gRPC stream observer for this WebSocket session
        StreamObserver<VentasResponse> streamObserver = new StreamObserver<VentasResponse>() {

            @Override
            public void onNext(VentasResponse venta) {
                try {
                    // Log the received gRPC data for debugging
                    logger.info("🔥 RECEIVED gRPC VENTA (Session: {}): ID={}, Email={}, Empresa={}, Fecha={}",
                        session.getId(),
                        venta.getId(),
                        venta.getEmail(),
                        venta.getNombreEmpresa(),
                        venta.getFechaEnvio()
                    );

                    // Convert the protobuf message to JSON for the frontend
                    Map<String, Object> ventaData = Map.of(
                        "id", venta.getId(),
                        "email", venta.getEmail(),
                        "nombre_empresa", venta.getNombreEmpresa(),
                        "fecha_envio", venta.getFechaEnvio()
                    );

                    String jsonMessage = objectMapper.writeValueAsString(ventaData);
                    logger.info("📤 SENDING WebSocket JSON (Session: {}): {}", session.getId(), jsonMessage);

                    session.sendMessage(new TextMessage(jsonMessage));

                    logger.info("✅ SUCCESS: Sent venta data to WebSocket session {}", session.getId());
                } catch (IOException e) {
                    logger.error("Error sending WebSocket message for session {}", session.getId(), e);
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("🚨 gRPC stream error for session {}: {}", session.getId(), t.getMessage());
                logger.debug("Full gRPC error details", t);

                try {
                    // Send error message to frontend
                    Map<String, Object> errorMessage = Map.of(
                        "type", "error",
                        "message", "gRPC server unavailable: " + t.getMessage(),
                        "timestamp", System.currentTimeMillis()
                    );
                    String jsonError = objectMapper.writeValueAsString(errorMessage);
                    logger.info("📤 SENDING Error to WebSocket session {}: {}", session.getId(), jsonError);

                    session.sendMessage(new TextMessage(jsonError));

                    // Close slowly to allow error message to be sent
                    Thread.sleep(100);
                    session.close(CloseStatus.POLICY_VIOLATION);

                } catch (IOException | InterruptedException e) {
                    logger.error("Error sending error message and closing WebSocket session", e);
                    try {
                        session.close(CloseStatus.SERVER_ERROR);
                    } catch (IOException closeError) {
                        logger.error("Failed to close WebSocket session after error", closeError);
                    }
                }
            }

            @Override
            public void onCompleted() {
                logger.info("gRPC stream completed for session {}", session.getId());
                try {
                    session.close(CloseStatus.NORMAL);
                } catch (IOException e) {
                    logger.error("Error closing WebSocket session", e);
                }
            }
        };

        // Store the stream observer for cleanup
        activeStreams.put(session.getId(), streamObserver);

        // Start the gRPC stream
        try {
            grpcClient.streamVentas(streamObserver);
            logger.info("Started gRPC stream for WebSocket session: {}", session.getId());
        } catch (Exception e) {
            logger.error("Failed to start gRPC stream", e);
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("WebSocket connection closed: {} with status: {}", session.getId(), status);

        // Clean up resources
        String sessionId = session.getId();
        activeSessions.remove(sessionId);

        StreamObserver<VentasResponse> streamObserver = activeStreams.remove(sessionId);
        if (streamObserver != null) {
            // Note: In a real implementation, you'd need a way to cancel the gRPC stream
            // This depends on the specific gRPC client implementation
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming WebSocket messages if needed
        logger.debug("Received WebSocket message from {}: {}", session.getId(), message.getPayload());
        // For now, we don't expect client messages, but this could be extended
        // for features like pause/resume, filtering, etc.
    }
}
