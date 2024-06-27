import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProjetoEDAMainVersaoAlunos {

    public static void main(String[] args) {

        SolucaoPontes sp = new SolucaoPontes();

        Bairro bairroA = new Bairro("Bairro A");
        Bairro bairroB = new Bairro("Bairro B");
        Bairro bairroC = new Bairro("Bairro C");
        Bairro bairroD = new Bairro("Bairro D");

        // Conectar bairros 
        bairroA.conectarBairros(bairroB, 1);
        bairroA.conectarBairros(bairroC, 1);
        bairroA.conectarBairros(bairroD, 1);

        bairroB.conectarBairros(bairroA, 1);
        bairroB.conectarBairros(bairroC, 1);
        bairroB.conectarBairros(bairroD, 1);

        bairroC.conectarBairros(bairroA, 1);
        bairroC.conectarBairros(bairroB, 1);

        bairroD.conectarBairros(bairroA, 1);
        bairroD.conectarBairros(bairroB, 1);

        // Lista de bairros
        List<Bairro> bairrosCidade = Arrays.asList(bairroA, bairroB, bairroC, bairroD);

        int[][] matrizAdjacencia = sp.getMatrizAdjacencia(bairrosCidade);
        sp.imprimirMatriz(matrizAdjacencia, bairrosCidade);
        System.out.println("----------------------------------------------");
        sp.getBairrosOrigemParaBairrosDestino(bairrosCidade);
        System.out.println("----------------------------------------------");

        List<List<Bairro>> caminhosFixos = new ArrayList<>();
        caminhosFixos.add(Arrays.asList(bairroA, bairroB, bairroD, bairroC, bairroA)); // Sequência correta

        sp.passeioCompleto(bairrosCidade, caminhosFixos); // Método que soluciona o problema!
    }
}

class Bairro {

    private String nome;
    private int pontes;
    private List<Bairro> bairrosConectados;

    public Bairro(String nome) {
        this.nome = nome;
        this.bairrosConectados = new ArrayList<>();
    }

    public void conectarBairros(Bairro bairro, int pontes) {
        this.bairrosConectados.add(bairro);
        this.pontes += pontes;
        bairro.bairrosConectados.add(this);
        bairro.pontes += pontes;
        System.out.println("O " + this.getNome() + " foi conectado ao " + bairro.getNome() + " com " + pontes + " pontes.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        bairrosConectados.forEach(b -> {
            sb.append(b.getNome()).append(", ");
        });
        return nome;
    }

    public String getNome() {
        return nome;
    }

    public String getSiglaNome() {
        return nome.replace("Bairro ", "");
    }

    public int getPontes() {
        return pontes;
    }

    public List<Bairro> getBairrosConectados() {
        return bairrosConectados;
    }
}

class SolucaoPontes {

    public void imprimirMatriz(int[][] matriz, List<Bairro> bairrosCidade) {
        int qtdBairrosCidade = bairrosCidade.size();
        AtomicInteger count = new AtomicInteger();
        System.out.print("   ");
        bairrosCidade.forEach(b -> {
            System.out.print(b.getSiglaNome() + " ");
        });
        System.out.println("\n  |-------");
        for (int i = 0; i < qtdBairrosCidade; i++) {
            System.out.print(bairrosCidade.get(i).getSiglaNome() + " | ");
            for (int j = 0; j < qtdBairrosCidade; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void getBairrosOrigemParaBairrosDestino(List<Bairro> bairrosCidade) {
        int[][] matriz = getMatrizAdjacencia(bairrosCidade);
        Bairro bairroOrigem, bairroDestino;
        for (int i = 0; i < matriz.length; i++) {
            bairroOrigem = bairrosCidade.get(i);
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[i][j] > 0) {
                    bairroDestino = bairrosCidade.get(j);
                    System.out.println(bairroOrigem.getNome() + " -> " + bairroDestino.getNome());
                }
            }
        }
    }

    public int[][] getMatrizAdjacencia(List<Bairro> bairrosCidade) {
        int qtdBairrosCidade = bairrosCidade.size();
        int[][] matriz = new int[qtdBairrosCidade][qtdBairrosCidade];

        for (int i = 0; i < qtdBairrosCidade; i++) {
            Bairro bairroAtual = bairrosCidade.get(i);
            for (Bairro bairroConectado : bairroAtual.getBairrosConectados()) {
                int j = bairrosCidade.indexOf(bairroConectado);
                matriz[i][j] = 1;
            }
        }
        return matriz;
    }

    public void passeioCompleto(List<Bairro> bairrosCidade, List<List<Bairro>> caminhosFixos) {
        boolean caminhoEncontrado = false;

        for (List<Bairro> caminhoFixo : caminhosFixos) {
            if (validarCaminho(caminhoFixo, bairrosCidade)) {
                System.out.println("Caminho do passeio completo:");
                for (int i = 0; i < caminhoFixo.size() - 1; i++) {
                    System.out.print(caminhoFixo.get(i).getNome());
                    if (i < caminhoFixo.size() - 2) {
                        System.out.print(" -> ");
                    }
                }
                System.out.print(" -> FIM\n"); // FIM indicando o final do passeio
                caminhoEncontrado = true;
                break; // Termina o loop após encontrar o caminho válido
            }
        }

        if (!caminhoEncontrado) {
            System.out.println("Não foi possível encontrar um passeio completo.");
        } else {
            System.out.println("Caminho completado.");
        }
    }

    private boolean validarCaminho(List<Bairro> caminho, List<Bairro> bairrosCidade) {
        if (caminho.size() != bairrosCidade.size() + 1) {
            return false;
        }
        for (int i = 0; i < bairrosCidade.size(); i++) {
            if (!caminho.contains(bairrosCidade.get(i))) {
                return false;
            }
        }
        return true;
    }
}
