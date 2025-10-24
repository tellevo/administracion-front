package cl.tellevo.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class EmpresaUploadRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombre;

    @NotBlank(message = "El dominio es obligatorio")
    @Pattern(regexp = "^@[a-zA-Z0-9.-]+\\.[a-z]{2,}$", message = "El dominio debe tener el formato @empresa.dominio válido (ej: @empresa.cl, @empresa.com)")
    @Size(max = 255, message = "El dominio no puede exceder 255 caracteres")
    private String dominio;

    @NotNull(message = "El archivo del logo es obligatorio")
    private MultipartFile logoFile;

    // Constructors
    public EmpresaUploadRequest() {}

    public EmpresaUploadRequest(String nombre, String dominio, MultipartFile logoFile) {
        this.nombre = nombre;
        this.dominio = dominio;
        this.logoFile = logoFile;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public MultipartFile getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(MultipartFile logoFile) {
        this.logoFile = logoFile;
    }

    // Validation methods
    public boolean hasLogoFile() {
        return logoFile != null && !logoFile.isEmpty();
    }

    @Override
    public String toString() {
        return "EmpresaUploadRequest{" +
                "nombre='" + nombre + '\'' +
                ", dominio='" + dominio + '\'' +
                ", hasLogoFile=" + hasLogoFile() +
                '}';
    }
}