

import java.util.ArrayList;
import java.util.List;

public class Vertice {

    private String nome;

    private List<Aresta> arestas = new ArrayList<>();

    private Integer grau = 0;

    public Vertice() {}

    public Vertice(String nome) {
        this.setNome(nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void addAresta(Vertice destino, int peso) {
        if (this.equals(destino)) {
            grau++;
        }
        this.arestas.add(new Aresta(destino, peso));
        grau++;
    }

    public boolean hasAresta(Vertice vertice) {
        for (Aresta a : this.arestas) {
            if (a.getDestino().equals(vertice)) {
                return true;
            }
        }
        return false;
    }

    public Aresta getAresta(int posicao) {
        return this.arestas.get(posicao);
    }

    public Aresta getAresta(Vertice vertice) {
        for (Aresta a : this.arestas) {
            if (a.getDestino().equals(vertice)) {
                return a;
            }
        }
        return null;
    }

    public List<Aresta> getArestas() {
        return this.arestas;
    }

    public void deleteAresta(Vertice vertice) {
        Aresta aresta = getAresta(vertice);
        if (aresta != null) {
            this.arestas.remove(aresta);
            this.grau--;
        }
    }

    public void deleteAresta(int posicao) {
        if (this.arestas.size() + 1 >= posicao) {
            this.arestas.remove(posicao);
            this.grau--;
        }
    }

    public void deleteAresta(Aresta aresta) {
        if (aresta != null) {
            this.arestas.remove(aresta);
            this.grau--;
        }
    }

    public Integer getGrau() {
        return this.grau;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertice other = (Vertice) obj;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        return true;
    }

    @Override
    public String toString() {
        String str = this.getNome() + (arestas.size() > 0 ? " -> " : "");
        for (int i = 0; i < arestas.size(); i++) {
            if (i != arestas.size() - 1) {
                str += arestas.get(i) + " -> ";
            } else {
                str += arestas.get(i);
            }
        }
        return str;
    }
}
