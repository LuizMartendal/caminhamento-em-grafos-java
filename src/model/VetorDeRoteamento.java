package model;

public class VetorDeRoteamento {

    private Vertice[] pais;

    private Integer[] distancia;

    public VetorDeRoteamento(Vertice[] pais, Integer[] distancia) {
        this.setPais(pais);
        this.setDistancia(distancia);
    }

    public Vertice[] getPais() {
        return this.pais;
    }

    public void setPais(Vertice[] pais) {
        this.pais = pais;
    }

    public Integer[] getDistancia() {
        return this.distancia;
    }

    public void setDistancia(Integer[] distancia) {
        this.distancia = distancia;
    }
}
