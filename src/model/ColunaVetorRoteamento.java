package model;

public class ColunaVetorRoteamento {
    private Vertice vertice;

    private Integer distancia;

    private Vertice pai;

    private boolean percorrido;

    public ColunaVetorRoteamento() {}

    public ColunaVetorRoteamento(Vertice vertice) {
        this.vertice = vertice;
        this.distancia = Integer.MAX_VALUE;
        this.percorrido = false;
    }

    public ColunaVetorRoteamento(Vertice vertice, Integer distancia) {
        this.setVertice(vertice);
        this.setDistancia(distancia);
        this.setPercorrido(false);
    }

    public Vertice getVertice() {
        return vertice;
    }

    public void setVertice(Vertice vertice) {
        this.vertice = vertice;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public Vertice getPai() {
        return pai;
    }

    public void setPai(Vertice pai) {
        this.pai = pai;
    }

    public boolean isPercorrido() {
        return percorrido;
    }

    public void setPercorrido(boolean percorrido) {
        this.percorrido = percorrido;
    }
}
