package cl.tellevo.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmpresaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombre;

    @NotBlank(message = "El dominio es obligatorio")
    @Pattern(regexp = "^@[a-zA-Z0-9.-]+\\.[a-z]{2,}$", message = "El dominio debe tener el formato @empresa.dominio válido (ej: @empresa.cl, @empresa.com)")
    @Size(max = 255, message = "El dominio no puede exceder 255 caracteres")
    private String dominio;

    @NotBlank(message = "La URL del logo es obligatoria")
    @Pattern(regexp = ".*\\.svg$", message = "La URL del logo debe terminar en .svg")
    @Size(max = 500, message = "La URL del logo no puede exceder 500 caracteres")
    private String logoUrl;

    // Constructors
    public EmpresaRequest() {}

    public EmpresaRequest(String nombre, String dominio, String logoUrl) {
        this.nombre = nombre;
        this.dominio = dominio;
        this.logoUrl = logoUrl;
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

    @Override
    public String toString() {
        return "EmpresaRequest{" +
                "nombre='" + nombre + '\'' +
                ", dominio='" + dominio + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
