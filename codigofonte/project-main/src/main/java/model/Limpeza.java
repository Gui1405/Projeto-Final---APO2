package model;

import java.time.LocalDateTime;

public class Limpeza {
    private int id;
    private LocalDateTime dataLimpeza;
    private String status;
    private String observacao;
    
    // [ASSOCIAÇÃO]
    private Sala sala;

    public Limpeza() {}

    public Limpeza(LocalDateTime dataLimpeza, String status, String observacao, Sala sala) {
        this.dataLimpeza = dataLimpeza;
        this.status = status;
        this.observacao = observacao;
        this.sala = sala;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataLimpeza() {
        return dataLimpeza;
    }

    public void setDataLimpeza(LocalDateTime dataLimpeza) {
        this.dataLimpeza = dataLimpeza;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
}