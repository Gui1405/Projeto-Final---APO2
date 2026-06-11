package br.cinema.model;

public class Sala {
    private int id;
    private int numeroSala;
    private int capacidade;
    private String tipoSala;
    private boolean disponivel;

    public Sala() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroSala() { return numeroSala; }
    public void setNumeroSala(int numeroSala) { this.numeroSala = numeroSala; }

    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    public String getTipoSala() { return tipoSala; }
    public void setTipoSala(String tipoSala) { this.tipoSala = tipoSala; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}