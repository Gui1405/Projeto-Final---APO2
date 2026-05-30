package model;

import java.time.LocalDateTime;

public class Manutencao {
    private int id;
    private LocalDateTime dataManutencao;
    private String status;
    private String observacao;
    
    // [ASSOCIAÇÃO]
    private Sala sala;

    public Manutencao() {}

    public Manutencao(LocalDateTime dataManutencao, String status, String observacao, Sala sala) {
        this.dataManutencao = dataManutencao;
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

    public LocalDateTime getDataManutencao() {
        return dataManutencao;
    }

    public void setDataManutencao(LocalDateTime dataManutencao) {
        this.dataManutencao = dataManutencao;
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