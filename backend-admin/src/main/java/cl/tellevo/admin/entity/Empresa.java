package cl.tellevo.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @NotBlank(message = "El código de país es obligatorio")
    @Size(max = 2, message = "El código de país debe tener exactamente 2 caracteres")
    @Column(name = "codigo_pais", nullable = false, length = 2)
    private String codigoPais;

    @NotBlank(message = "El dominio es obligatorio")
    @Size(max = 255, message = "El dominio no puede exceder 255 caracteres")
    @Column(name = "dominio", nullable = false, length = 255, unique = true)
    private String dominio;

    @NotBlank(message = "La URL del logo es obligatoria")
    @Pattern(regexp = ".*\\.svg$", message = "La URL del logo debe terminar en .svg")
    @Size(max = 500, message = "La URL del logo no puede exceder 500 caracteres")
    @Column(name = "logo_url", nullable = false, length = 500)
    private String logoUrl;

    // Constructors
    public Empresa() {}

    public Empresa(String nombre, String dominio, String logoUrl) {
        this.nombre = nombre;
        this.codigoPais = "CL"; // Always set to CL
        this.dominio = dominio;
        this.logoUrl = logoUrl;
    }

    public Empresa(String nombre, String codigoPais, String dominio, String logoUrl) {
        this.nombre = nombre;
        this.codigoPais = codigoPais;
        this.dominio = dominio;
        this.logoUrl = logoUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
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
        return "Empresa{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigoPais='" + codigoPais + '\'' +
                ", dominio='" + dominio + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
