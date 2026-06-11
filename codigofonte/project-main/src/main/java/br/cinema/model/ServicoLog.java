package br.cinema.model;

public class ServicoLog {
    private String tipo;
    private int numeroSala;
    private String dataHora;
    private String status;

    public ServicoLog() {}

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getNumeroSala() { return numeroSala; }
    public void setNumeroSala(int numeroSala) { this.numeroSala = numeroSala; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}