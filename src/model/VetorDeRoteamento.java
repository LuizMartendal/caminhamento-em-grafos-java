package model;

import java.util.ArrayList;
import java.util.List;

public class VetorDeRoteamento {

    private List<ColunaVetorRoteamento> vertices = new ArrayList<>();

    private final Vertice verticeInicial;

    public VetorDeRoteamento(Vertice verticeInicial) {
        this.verticeInicial = verticeInicial;
    }

    public Vertice getVerticeInicial() {
        return this.verticeInicial;
    }

    public void addColunaVetorDeRoteamento(Vertice v, Integer distancia) {
        vertices.add(new ColunaVetorRoteamento(
            v, distancia
        ));
    }

    public void addColunaVetorDeRoteamento(Vertice v) {
        vertices.add(new ColunaVetorRoteamento(v));
    }

    public ColunaVetorRoteamento getColuna(int posicao) {
        return vertices.get(posicao);
    }

    public ColunaVetorRoteamento getColuna(Vertice vertice) {
        for (ColunaVetorRoteamento coluna : this.vertices) {
            if (coluna.getVertice().equals(vertice)) {
                return coluna;
            }
        }
        return null;
    }

    public List<ColunaVetorRoteamento> getColunasFilaNaoPercorrido() {
        List<ColunaVetorRoteamento> colunas = new ArrayList<>();

        for (ColunaVetorRoteamento coluna : this.vertices) {
            if (!coluna.isPercorrido()) {
                colunas.add(coluna);
            }
        }
        return colunas;
    }

    public List<ColunaVetorRoteamento> getColunasFila() {
        return vertices;
    }

    public List<Vertice> getVertices() {
        List<Vertice> vertices = new ArrayList<>();
        for (ColunaVetorRoteamento coluna : this.vertices) {
            vertices.add(coluna.getVertice());
        }
        return vertices;
    }

    public void removeFila(Vertice v) {
        for (ColunaVetorRoteamento c : this.vertices) {
            if (v.equals(c.getVertice())) {
                c.setPercorrido(true);
            }
        }
    }

    public List<Aresta> getVerticesAdjacentes(Vertice v) {
        return v.getArestas();
    }

    public ColunaVetorRoteamento getMin(List<ColunaVetorRoteamento> lista) {
        ColunaVetorRoteamento coluna = null;
        Integer distancia = Integer.MAX_VALUE;
        for (ColunaVetorRoteamento c : lista) {
            if (c.getDistancia() < distancia && !c.isPercorrido()) {
                coluna = c;
                distancia = c.getDistancia();
            }
        }
        return coluna;
    }

    @Override
    public String toString() {
        String V = "";
        String D = "\nD";
        String pai = "\nPai";
        String Q = "\nQ";
        String S = "\nS";
        for (ColunaVetorRoteamento c : this.vertices) {
            V += "\t" + c.getVertice().getNome();
            D += "\t" + (c.getDistancia().equals(Integer.MAX_VALUE) ? "?" : c.getDistancia());
            pai += "\t" + (c.getPai() != null ? c.getPai().getVertice().getNome() : "NIL");
            Q += "\t" + (c.isPercorrido() ? "" : "X");
            S += "\t" + (c.isPercorrido() ? "X" : "");
        }
        return V + D + pai + Q + S;
    }
}
