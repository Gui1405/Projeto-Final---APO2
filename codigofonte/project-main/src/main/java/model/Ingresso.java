package model;

import java.time.LocalDateTime;

public class Ingresso {
    private Integer id; 
    private String status;
    private LocalDateTime dataCompra;
    
    private Cliente cliente;
    private Sessao sessao;
    private Poltrona poltrona;

    public Ingresso() {}

    public Ingresso(String status, LocalDateTime dataCompra, Cliente cliente, Sessao sessao, Poltrona poltrona) {
        this.status = status;
        this.dataCompra = dataCompra;
        this.cliente = cliente;
        this.sessao = sessao;
        this.poltrona = poltrona;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Sessao getSessao() { return sessao; }
    public void setSessao(Sessao sessao) { this.sessao = sessao; }

    public Poltrona getPoltrona() { return poltrona; }
    public void setPoltrona(Poltrona poltrona) { this.poltrona = poltrona; }
}