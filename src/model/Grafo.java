//Luiz Henrique Martendal; Daniel Krüger
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grafo {

    private List<Vertice> vertices = new ArrayList<>();

    private Long custoTotal = 0L;

    public Grafo() {
    }

    public Grafo(List<Vertice> vertices) {
        this.vertices = vertices;
    }

    public void addVertice(Vertice v) {
        this.vertices.add(v);
    }

    public void carteiroChines(Vertice inicio) {
        List<Vertice> verticesImpares = getVerticesImpares();
        System.out.println("\n");

        while (verticesImpares.size() > 1) {
            List<VetorDeRoteamento> vetorDeRoteamentoList = new ArrayList<>();
            for (Vertice v : verticesImpares) {
                VetorDeRoteamento vetorDeRoteamento = dijkstra(v);
                System.out.println("Aplicando Dijkstra no v�rtice " + v.getNome() + "\n" + vetorDeRoteamento + "\n");
                vetorDeRoteamentoList.add(vetorDeRoteamento);
            }

            ColunaVetorRoteamento[][] matrizD = getMatrizD(vetorDeRoteamentoList, verticesImpares);
            imprimirMatrizD(matrizD, verticesImpares);

            List<ColunaVetorRoteamento[]> paresMinimos = getParesMinimos(matrizD);
            System.out.println();
            imprimirParesMinimos(paresMinimos);

            for (ColunaVetorRoteamento[] c : paresMinimos) {
                ColunaVetorRoteamento pai = c[1].getPai();
                ColunaVetorRoteamento atual = c[1];
                while (pai != null) {
                    atual.getVertice().addAresta(pai.getVertice(), atual.getDistancia() - pai.getDistancia());
                    pai.getVertice().addAresta(atual.getVertice(), atual.getDistancia() - pai.getDistancia());
                    atual = pai;
                    pai = pai.getPai();
                }
            }
            verticesImpares = getVerticesImpares();
        }

        if (verticesImpares.size() == 1) {
            throw new RuntimeException("Grafos com v�rtices de grau impar, n�o podem ser euleriano");
        }

        System.out.println("\nGrafo vers�o final:\n" + this);

        List<String> fleury = this.fleury(inicio);
        System.out.println("Ciclo euleriano: ");
        for (String v : fleury) {
            System.out.print(v + " ");
        }
        System.out.println("\nCusto total: " + custoTotal);
    }

    private List<String> fleury(Vertice verticeInicial) {
        List<Vertice> copiaVertices = this.copia();
        List<String> caminho = new ArrayList<>();
        Vertice inicio = null;
        for (Vertice v : copiaVertices) {
            if (verticeInicial.getNome().equals(v.getNome())) {
                inicio = v;
                break;
            }
        }
        caminho.add(inicio.getNome());
        criandoCaminho(copiaVertices, caminho, inicio, inicio);
        return caminho;
    }

    private void criandoCaminho(List<Vertice> copiaVertices, List<String> caminho, Vertice verticeInicial, Vertice verticeAtual) {
        do {
            Aresta aresta = null;
            for (int i = 0; i < verticeAtual.getArestas().size(); i++) {
                Aresta a = verticeAtual.getAresta(i);
                if (arestaValida(copiaVertices, verticeAtual, a) && !a.isPercorrido()) {
                    aresta = a;
                    break;
                }
            }
            if (aresta != null) {
                Vertice destino = aresta.getDestino();
                verticeAtual.deleteAresta(aresta);
                for (Aresta a : destino.getArestas()) {
                    if (a.getDestino().equals(verticeAtual) && a.getPeso().equals(aresta.getPeso())) {
                        aresta = a;
                        break;
                    }
                }
                custoTotal += aresta.getPeso();
                destino.deleteAresta(aresta);
                verticeAtual = destino;
                caminho.add(verticeAtual.getNome());
            }
        } while (!verticeAtual.equals(verticeInicial));
    }

    private boolean arestaValida(List<Vertice> copiaVertices, Vertice u, Aresta a) {
        if (u.getGrau() == 1) {
            return true;
        }
        int index = copiaVertices.indexOf(u);
        boolean[] visitado = new boolean[copiaVertices.size()];
        int cont1 = dfsCont(copiaVertices, u, index, visitado);
        visitado = new boolean[copiaVertices.size()];
        u.getArestas().remove(a);
        int cont2 = dfsCont(copiaVertices, u, index, visitado);
        u.getArestas().add(a);
        return cont1 > cont2 ? false : true;
    }

    private int dfsCont(List<Vertice> copiaVertices, Vertice u, int index, boolean[] visitado) {
        int cont = 1;
        visitado[index] = true;
        for (Aresta a : u.getArestas()) {
            if (!visitado[copiaVertices.indexOf(a.getDestino())]) {
                cont += dfsCont(copiaVertices, a.getDestino(), copiaVertices.indexOf(a.getDestino()), visitado);
            }
        }
        return cont;
    }

    private List<Vertice> copia() {
        List<Vertice> copiaVertices = new ArrayList<>();
        for (Vertice v : this.vertices) {
            Vertice novoVertice = new Vertice(v.getNome());
            for (Aresta a : v.getArestas()) {
                novoVertice.addAresta(a.getDestino(), a.getPeso());
            }
            copiaVertices.add(novoVertice);
        }
        return copiaVertices;
    }

    private List<ColunaVetorRoteamento[]> getParesMinimos(ColunaVetorRoteamento[][] vetorD) {
        List<ColunaVetorRoteamento[]> paresMinimos = new ArrayList<>();
        Integer[] controle = new Integer[vetorD.length];
        for (int i = 0; i < vetorD.length; i++) {
            ColunaVetorRoteamento min = new ColunaVetorRoteamento();
            min.setDistancia(Integer.MAX_VALUE);
            int columnIndex = 0;
            if (controle[i] == null || controle[i] == 0) {
                for (int j = 0; j < vetorD.length; j++) {
                    if ((controle[j] == null || controle[j] == 0) && i != j
                            && vetorD[i][j].getDistancia() < min.getDistancia()) {
                        min = vetorD[i][j];
                        columnIndex = j;
                    }
                }
                controle[i] = 1;
                controle[columnIndex] = 1;
                ColunaVetorRoteamento[] colunaVetorRoteamentos = new ColunaVetorRoteamento[2];
                colunaVetorRoteamentos[0] = vetorD[i][i];
                colunaVetorRoteamentos[1] = vetorD[i][columnIndex];
                paresMinimos.add(colunaVetorRoteamentos);
            }
        }
        return paresMinimos;
    }

    private ColunaVetorRoteamento[][] getMatrizD(List<VetorDeRoteamento> vetorDeRoteamentoList,
            List<Vertice> verticesImpares) {
        ColunaVetorRoteamento[][] matrizD = new ColunaVetorRoteamento[verticesImpares.size()][verticesImpares.size()];
        for (int i = 0; i < vetorDeRoteamentoList.size(); i++) {
            int cont = 0;
            for (int j = 0; j < vetorDeRoteamentoList.get(0).getColunasFila().size(); j++) {
                ColunaVetorRoteamento c = vetorDeRoteamentoList.get(i).getColunasFila().get(j);
                if (verticesImpares.contains(c.getVertice())) {
                    matrizD[i][cont] = c;
                    cont++;
                }
            }
        }
        return matrizD;
    }

    public List<Vertice> getVerticesImpares() {
        List<Vertice> verticesImpares = new ArrayList<>();
        System.out.print("V�rtices �mpares: ");
        for (Vertice v : vertices) {
            if (v.getGrau() % 2 != 0) {
                System.out.print(v.getNome() + "  ");
                verticesImpares.add(v);
            }
        }
        if (verticesImpares.isEmpty()) {
            System.out.println("NULL");
        }
        return verticesImpares;
    }

    public VetorDeRoteamento dijkstra(Vertice inicio) {
        VetorDeRoteamento vetorDeRoteamento = this.initializeSingleSource(inicio);
        List<ColunaVetorRoteamento> fila = vetorDeRoteamento.getColunasFilaNaoPercorrido();

        while (!fila.isEmpty()) {
            ColunaVetorRoteamento u = vetorDeRoteamento.getMin(fila);
            if (u == null) {
                throw new RuntimeException("Grafo desconexo!");
            }
            u.setPercorrido(true);
            fila.remove(u);
            for (Aresta v : u.getVertice().getArestas()) {
                ColunaVetorRoteamento coluna = vetorDeRoteamento.getColuna(v.getDestino());
                if (coluna != null) {
                    Integer distanciaV = coluna.getDistancia();
                    Integer distaciaU = u.getDistancia();
                    Integer custo = v.getPeso();
                    if (distanciaV > (distaciaU + custo)) {
                        coluna.setDistancia(distaciaU + custo);
                        coluna.setPai(u);
                    }
                }
            }
        }
        return vetorDeRoteamento;
    }

    private VetorDeRoteamento initializeSingleSource(Vertice verticeInicial) {
        VetorDeRoteamento vetorDeRoteamento = new VetorDeRoteamento(verticeInicial);
        for (Vertice v : vertices) {
            if (Objects.equals(v, verticeInicial)) {
                vetorDeRoteamento.addColunaVetorDeRoteamento(v, 0);
            } else {
                vetorDeRoteamento.addColunaVetorDeRoteamento(v);
            }
        }
        return vetorDeRoteamento;
    }

    private void imprimirParesMinimos(List<ColunaVetorRoteamento[]> paresMinimos) {
        for (int i = 0; i < paresMinimos.size(); i++) {
            System.out.print(paresMinimos.get(i)[1].getVertice().getNome());
            ColunaVetorRoteamento pai = paresMinimos.get(i)[1].getPai();
            while (pai != null) {
                System.out.print(" -> " + pai.getVertice().getNome());
                pai = pai.getPai();
            }
            System.out.println(" Dist�ncia final: " + paresMinimos.get(i)[1].getDistancia());
        }
        System.out.println();
    }

    private void imprimirMatrizD(ColunaVetorRoteamento[][] matrizD, List<Vertice> verticesImpares) {
        String strVertices = "";
        for (Vertice v : verticesImpares) {
            strVertices += "\t" + v.getNome();
        }
        String strValores = "";
        for (int i = 0; i < matrizD.length; i++) {
            strValores += "\n" + verticesImpares.get(i).getNome();
            for (int j = 0; j < matrizD[i].length; j++) {
                strValores += "\t" + matrizD[i][j].getDistancia();
            }
        }
        System.out.println(strVertices + strValores);
    }

    @Override
    public String toString() {
        String str = "";
        for (Vertice v : vertices) {
            str += v.toString() + "\n";
        }
        return str;
    }

    public static void main(String[] args) {
        // V?rtices
        Vertice v1 = new Vertice("V1");
        Vertice v2 = new Vertice("V2");
        Vertice v3 = new Vertice("V3");
        Vertice v4 = new Vertice("V4");
        Vertice v5 = new Vertice("V5");
        Vertice v6 = new Vertice("V6");
        Vertice v7 = new Vertice("V7");
        Vertice v8 = new Vertice("V8");
        Vertice v9 = new Vertice("V9");
        Vertice v10 = new Vertice("V10");
        Vertice v11 = new Vertice("V11");
        Vertice v12 = new Vertice("V12");
        Vertice v13 = new Vertice("V13");
        Vertice v14 = new Vertice("V14");
        Vertice v15 = new Vertice("V15");
        Vertice v16 = new Vertice("V16");
        Vertice v17 = new Vertice("V17");
        Vertice v18 = new Vertice("V18");

        // Arestas
        v1.addAresta(v2, 73);

        v2.addAresta(v1, 73);
        v2.addAresta(v3, 39);

        v3.addAresta(v2, 39);
        v3.addAresta(v4, 64);
        v3.addAresta(v6, 147);

        v4.addAresta(v3, 64);
        v4.addAresta(v5, 174);

        v5.addAresta(v4, 174);
        v5.addAresta(v6, 69);
        v5.addAresta(v7, 195);

        v6.addAresta(v3, 147);
        v6.addAresta(v5, 69);
        v6.addAresta(v8, 64);
        v6.addAresta(v15, 244);

        v7.addAresta(v5, 195);
        v7.addAresta(v15, 70);

        v8.addAresta(v6, 64);
        v8.addAresta(v9, 52);
        v8.addAresta(v10, 90);

        v9.addAresta(v8, 52);
        v9.addAresta(v11, 58);

        v10.addAresta(v8, 90);

        v11.addAresta(v9, 58);
        v11.addAresta(v13, 35);
        v11.addAresta(v14, 115);

        v12.addAresta(v13, 68);

        v13.addAresta(v11, 35);
        v13.addAresta(v12, 68);
        v13.addAresta(v14, 98);

        v14.addAresta(v13, 98);
        v14.addAresta(v11, 115);
        v14.addAresta(v18, 79);

        v15.addAresta(v7, 70);
        v15.addAresta(v17, 66);
        v15.addAresta(v6, 244);

        v16.addAresta(v17, 110);

        v17.addAresta(v15, 66);
        v17.addAresta(v16, 110);
        v17.addAresta(v18, 67);

        v18.addAresta(v14, 79);
        v18.addAresta(v17, 67);

        // Criando a lista
        List<Vertice> vertices = new ArrayList<Vertice>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        vertices.add(v6);
        vertices.add(v7);
        vertices.add(v8);
        vertices.add(v9);
        vertices.add(v10);
        vertices.add(v11);
        vertices.add(v12);
        vertices.add(v13);
        vertices.add(v14);
        vertices.add(v15);
        vertices.add(v16);
        vertices.add(v17);
        vertices.add(v18);

        Grafo grafo = new Grafo(vertices);
        grafo.carteiroChines(v1);
    }
}