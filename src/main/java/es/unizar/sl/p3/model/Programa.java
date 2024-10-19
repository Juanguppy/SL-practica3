package es.unizar.sl.p3.model;

public class Programa {
    private int numero;
    private String nombre;
    private String tipo;
    private String cinta;
    private String registro;

    // Constructor
    public Programa(int numero, String nombre, String tipo, String cinta, String registro) {
        this.numero = numero;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cinta = cinta;
        this.registro = registro;
    }

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCinta() {
        return cinta;
    }

    public void setCinta(String cinta) {
        this.cinta = cinta;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    @Override
    public String toString() {
        return "Programa{" +
                "numero=" + numero +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", cinta='" + cinta + '\'' +
                ", registro='" + registro + '\'' +
                '}';
    }
}