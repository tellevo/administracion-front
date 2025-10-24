package cl.tellevo.admin;

import cl.tellevo.admin.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendAdminApplication.class, args);
    }

    @Bean
    CommandLineRunner init(FileStorageService fileStorageService) {
        return (args) -> {
            fileStorageService.init();
        };
    }

}
