package model;

import java.util.ArrayList;
import java.util.List;

public class VetorDeRoteamento {

    private List<ColunaVetorRoteamento> vertices = new ArrayList<>();

    public VetorDeRoteamento() {}

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

    public List<ColunaVetorRoteamento> getColunasFila() {
        List<ColunaVetorRoteamento> colunas = new ArrayList<>();

        for (ColunaVetorRoteamento coluna : this.vertices) {
            if (!coluna.isPercorrido()) {
                colunas.add(coluna);
            }
        }
        return colunas;
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
            if (c.getDistancia() < distancia) {
                coluna = c;
                distancia = c.getDistancia();
            }
        }
        return coluna;
    }

    @Override
    public String toString() {
        String str = "";
        for (ColunaVetorRoteamento c : this.vertices) {
            str += "Vertice = " + c.getVertice().toString() + ", D = " + c.getDistancia() + ", Pai = " + c.getPai() + "\n";
        }
        return str;
    }
}
