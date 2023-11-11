package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grafo {

    private List<Vertice> vertices = new ArrayList<>();

    public Grafo() {}

    public Grafo(List<Vertice> vertices) {
        this.vertices = vertices;
    }

    public void addVertice(Vertice v) {
        this.vertices.add(v);
    }

    public void carteiroChines(Vertice inicio) {
        List<Vertice> verticesImpares = getVerticesImpares();
        System.out.println("\n");

        List<VetorDeRoteamento> vetorDeRoteamentoList = new ArrayList<>();
        for (Vertice v : verticesImpares) {
            VetorDeRoteamento vetorDeRoteamento = dijkstra(v);
            System.out.println("Aplicando Dijkstra no vétice " + v.getNome() + "\n" + vetorDeRoteamento + "\n");
            vetorDeRoteamentoList.add(vetorDeRoteamento);
        }

        ColunaVetorRoteamento[][] matrizD = getMatrizD(vetorDeRoteamentoList, verticesImpares);
        imprimirVetorD(matrizD, verticesImpares);

        List<ColunaVetorRoteamento[]> paresMinimos = getParesMinimos(matrizD);
        System.out.println();
        imprimirParesMinimos(paresMinimos);
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
                    if ((controle[j] == null || controle[j] == 0) && i != j && vetorD[i][j].getDistancia() < min.getDistancia()) {
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

    private ColunaVetorRoteamento[][] getMatrizD(List<VetorDeRoteamento> vetorDeRoteamentoList, List<Vertice> verticesImpares) {
        List<VetorDeRoteamento> discardList = new ArrayList<>(vetorDeRoteamentoList);
        ColunaVetorRoteamento[][] vetorD = new ColunaVetorRoteamento[verticesImpares.size()][verticesImpares.size()];
        for (int i = 0; i < verticesImpares.size(); i++) {
            for (int j = 0; j < verticesImpares.size(); j++) {
                ColunaVetorRoteamento c = discardList.get(i).getColunasFila().get(j);
                if (verticesImpares.contains(c.getVertice())) {
                    vetorD[i][j] = c;
                } else {
                    discardList.get(i).getColunasFila().remove(c);
                    j--;
                }
            }
        }
        return vetorD;
    }

    public List<Vertice> getVerticesImpares() {
        List<Vertice> verticesImpares = new ArrayList<>();
        System.out.print("Vértices ímpares: ");
        for (Vertice v : vertices) {
            if (v.getGrau() % 2 != 0) {
                System.out.print(v.getNome() + "  ");
                verticesImpares.add(v);
            }
        }
        return verticesImpares;
    }

    public VetorDeRoteamento dijkstra(Vertice inicio) {
        VetorDeRoteamento vetorDeRoteamento = this.initializeSingleSource(inicio);
        List<ColunaVetorRoteamento> fila = vetorDeRoteamento.getColunasFilaNaoPercorrido();

        while (!fila.isEmpty()) {
            ColunaVetorRoteamento u = vetorDeRoteamento.getMin(fila);
            u.setPercorrido(true);
            fila.remove(u);
           // boolean change = false;
            for (Aresta v : u.getVertice().getArestas()) {
                ColunaVetorRoteamento coluna = vetorDeRoteamento.getColuna(v.getDestino());
                Integer distanciaV = coluna.getDistancia();
                Integer distaciaU = u.getDistancia();
                Integer custo = v.getPeso();
                if (distanciaV > (distaciaU + custo)) {
                    coluna.setDistancia(distaciaU + custo);
                    coluna.setPai(u.getVertice());
                    //System.out.println(vetorDeRoteamento + "\n");
                    //change = true;
                }
            }
//            if (!change) {
//                System.out.println(vetorDeRoteamento + "\n");
//            }
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
            System.out.println(
                    paresMinimos.get(i)[0].getVertice().getNome() + " distância atual: " + paresMinimos.get(i)[0].getDistancia() + " -> " +
                            paresMinimos.get(i)[1].getVertice().getNome() + " distância final: " + paresMinimos.get(i)[1].getDistancia());
        }
    }

    private void imprimirVetorD(ColunaVetorRoteamento[][] vetorD, List<Vertice> verticesImpares) {
        String strVertices = "";
        for (Vertice v : verticesImpares) {
            strVertices += "\t" + v.getNome();
        }
        String strValores = "";
        for (int i = 0; i < vetorD.length; i++) {
            strValores += "\n" + verticesImpares.get(i).getNome();
            for (int j = 0; j < vetorD[i].length; j++) {
                strValores += "\t" + vetorD[i][j].getDistancia();
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
        // Vértices
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
        v11.addAresta(v14,115);
    
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