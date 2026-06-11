package br.cinema.model;

public class Filme {
    
    private int id;
    private String titulo;
    private int duracao;
    private String genero;
    private String classificacao;
    private String sinopse;

    public Filme() {
    }

    public Filme(int id, String titulo, int duracao, String genero, String classificacao, String sinopse) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.genero = genero;
        this.classificacao = classificacao;
        this.sinopse = sinopse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
}