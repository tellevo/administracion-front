package cl.tellevo.admin.service;

import cl.tellevo.admin.config.GrpcVentasConfig;
import cl.tellevo.admin.grpc.ventas.VentasServiceGrpc;
import cl.tellevo.admin.grpc.ventas.VentasRequest;
import cl.tellevo.admin.grpc.ventas.VentasResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentasGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(VentasGrpcClient.class);

    private final GrpcVentasConfig config;
    private ManagedChannel channel;

    @Autowired
    public VentasGrpcClient(GrpcVentasConfig config) {
        this.config = config;
        initializeChannel();
    }

    private void initializeChannel() {
        try {
            this.channel = ManagedChannelBuilder
                .forAddress(config.getHost(), config.getPort())
                .usePlaintext() // Use TLS in production
                .build();
            logger.info("gRPC channel initialized: {}:{}", config.getHost(), config.getPort());
        } catch (Exception e) {
            logger.error("Failed to initialize gRPC channel", e);
            throw new RuntimeException("Failed to initialize gRPC channel", e);
        }
    }

    /**
     * Start streaming ventas data to the provided observer
     * @param responseObserver The observer to receive venta messages
     */
    public void streamVentas(StreamObserver<VentasResponse> responseObserver) {
        if (channel == null || channel.isShutdown()) {
            logger.warn("gRPC channel is not available, reinitializing...");
            try {
                initializeChannel();
            } catch (Exception e) {
                logger.error("Failed to reinitialize gRPC channel to {}:{}", config.getHost(), config.getPort(), e);
                responseObserver.onError(new RuntimeException("gRPC server not available", e));
                return;
            }
        }

        try {
            // Create the gRPC async stub
            VentasServiceGrpc.VentasServiceStub stub = VentasServiceGrpc.newStub(channel);

            // Create the request (empty as per the proto definition)
            VentasRequest request = VentasRequest.newBuilder().build();

            // Make the streaming call
            logger.info("🚀 STARTING gRPC call to {}:{} with empty request", config.getHost(), config.getPort());
            stub.streamVentas(request, responseObserver);
            logger.info("📡 gRPC call initiated, waiting for responses...");

        } catch (Exception e) {
            logger.error("Failed to start gRPC ventas stream to {}:{}", config.getHost(), config.getPort(), e);
            // Try to provide some fallback data or clear error message
            try {
                // For development, you could simulate empty stream
                logger.warn("gRPC server unavailable - completing stream gracefully");
                responseObserver.onCompleted();
            } catch (Exception ex) {
                logger.error("Failed to complete empty stream", ex);
                responseObserver.onError(e);
            }
        }
    }

    /**
     * Shutdown the gRPC channel
     */
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            logger.info("gRPC channel shutdown");
        }
    }

    /**
     * Check if the gRPC channel is healthy
     * @return true if channel is ready
     */
    public boolean isHealthy() {
        return channel != null && !channel.isShutdown() && !channel.isTerminated();
    }

    /**
     * Get connection info for debugging
     * @return connection details string
     */
    public String getConnectionInfo() {
        return String.format("gRPC connection to %s:%d (healthy: %s)",
            config.getHost(), config.getPort(), isHealthy());
    }
}
