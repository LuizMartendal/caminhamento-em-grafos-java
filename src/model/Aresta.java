package model;

public class Aresta {

    private Vertice destino;
    private Integer peso;

    public Aresta(Vertice destino, Integer peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getDestino() {
        return destino;
    }

    public void setDestino(Vertice destino) {
        this.destino = destino;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return this.getDestino().getNome() + ", Peso " + this.getPeso();
    }
}
