package cl.tellevo.admin.dto;

import jakarta.validation.constraints.NotBlank;
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

    private String logoUrl;

    private MultipartFile logoFile;

    // Constructors
    public EmpresaUploadRequest() {}

    public EmpresaUploadRequest(String nombre, String dominio, String logoUrl, MultipartFile logoFile) {
        this.nombre = nombre;
        this.dominio = dominio;
        this.logoUrl = logoUrl;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public MultipartFile getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(MultipartFile logoFile) {
        this.logoFile = logoFile;
    }

    // Validation methods
    public boolean hasLogoUrl() {
        return logoUrl != null && !logoUrl.trim().isEmpty();
    }

    public boolean hasLogoFile() {
        return logoFile != null && !logoFile.isEmpty();
    }

    public boolean hasLogo() {
        return hasLogoUrl() || hasLogoFile();
    }

    @Override
    public String toString() {
        return "EmpresaUploadRequest{" +
                "nombre='" + nombre + '\'' +
                ", dominio='" + dominio + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", hasLogoFile=" + hasLogoFile() +
                '}';
    }
}