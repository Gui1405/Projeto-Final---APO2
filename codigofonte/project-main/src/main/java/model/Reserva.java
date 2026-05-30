package model;

public class Reserva {

    private int id;
    private String status;
    
    private Poltrona poltrona;

    public Reserva(int id, String status, Poltrona poltrona) {
        this.id = id;
        this.status = status;
        this.poltrona = poltrona;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    // Getter do Objeto
    public Poltrona getPoltrona() {
        return poltrona;
    }
}