package cl.tellevo.admin.dto;

public class EmpresaResponse {

    private Long id;
    private String nombre;
    private String codigoPais;
    private String dominio;
    private String logoUrl;
    private String message;

    // Constructors
    public EmpresaResponse() {}

    public EmpresaResponse(Long id, String nombre, String codigoPais, String dominio, String logoUrl) {
        this.id = id;
        this.nombre = nombre;
        this.codigoPais = codigoPais;
        this.dominio = dominio;
        this.logoUrl = logoUrl;
    }

    public EmpresaResponse(Long id, String nombre, String codigoPais, String dominio, String logoUrl, String message) {
        this.id = id;
        this.nombre = nombre;
        this.codigoPais = codigoPais;
        this.dominio = dominio;
        this.logoUrl = logoUrl;
        this.message = message;
    }

    public EmpresaResponse(String message) {
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmpresaResponse{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigoPais='" + codigoPais + '\'' +
                ", dominio='" + dominio + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
