package model;

import java.time.LocalDateTime;

public class Sessao {
    private int id;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    
    // [ASSOCIAÇÃO]
    private Filme filme;
    private Sala sala;

    public Sessao() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDateTime getInicio() {
        return inicio;
    }
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
    public LocalDateTime getFim() {
        return fim;
    }
    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }
    
    //Getters
    public Filme getFilme() {
        return filme;
    }
    public void setFilme(Filme filme) {
        this.filme = filme;
    }
    public Sala getSala() {
        return sala;
    }
    public void setSala(Sala sala) {
        this.sala = sala;
    }
}