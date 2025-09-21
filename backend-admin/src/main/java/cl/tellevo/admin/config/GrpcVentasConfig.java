package cl.tellevo.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grpc.ventas")
public class GrpcVentasConfig {
    private String host = "localhost";
    private int port = 9090;

    public GrpcVentasConfig() {}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "GrpcVentasConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
