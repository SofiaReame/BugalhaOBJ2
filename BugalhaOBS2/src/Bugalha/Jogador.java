package Bugalha;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Jogador {
    private String nome; // Nome do jogador
    private ArrayList<Integer>[] colunas; // 3 colunas de dados
    private Random random; // Para gerar dados aleatórios

    //@SuppressWarnings("unchecked")
    public Jogador(String nome) {
        setNome(nome);
        colunas = new ArrayList[3]; // 3 colunas
        for (int i = 0; i < 3; i++) {
            colunas[i] = new ArrayList<>(); // Inicializa cada coluna
        }
        random = new Random();
    }

    // Gera um número aleatório entre 1 e 6 (simulando um dado)
    public int rolarDado() {
        return random.nextInt(6) + 1;
    }

    // Adiciona um dado em uma coluna específica
  public boolean adicionarDado(int coluna, int valor, Jogador adversario) {
     if (coluna < 0 || coluna >= 3 || colunas[coluna].size() >= 3) {
        return false;
     }

     // Adiciona o valor à coluna deste jogador
        colunas[coluna].add(valor);

        // Obtém a lista de dados do adversário na mesma coluna
        ArrayList<Integer> colunaDoAdversario = adversario.colunas[coluna];
        int quantidadeRemovida = 0;

        // Iterator sobre a coluna do adversário e remove todos os valores iguais
        Iterator<Integer> iterador = colunaDoAdversario.iterator();
        while (iterador.hasNext()) {
            if (iterador.next().equals(valor)) {
                iterador.remove();
                quantidadeRemovida++;
            }
        }

        if (quantidadeRemovida > 0) {
            System.out.println(adversario.getNome()
                + " perdeu " + quantidadeRemovida
                + (quantidadeRemovida == 1 ? " dado" : " dados")
                + " de valor " + valor
                + " na coluna " + coluna);
        }
        return true;
    }
    // Exibe o tabuleiro (teste)
    public void mostrarTabuleiro() {
        System.out.println("Tabuleiro de " + nome + ":");
        for (int i = 0; i < 3; i++) {
            System.out.println("Coluna " + (i+1) + ": " + colunas[i]);
        }
    }
    
    public void limparTabuleiro() {
        for (ArrayList<Integer> coluna : colunas) {
            coluna.clear();
        }
    }

    // Exibe o tabuleiro bonito como uma matriz
    public void mostrarTabuleiroBonito() {
        System.out.println("\nTabuleiro de " + nome + ":");

        // Matriz de 3 linhas x 3 colunas
        String[][] matriz = new String[3][3];

        // Preenche a matriz com os dados das colunas
        for (int coluna = 0; coluna < 3; coluna++) {
            ArrayList<Integer> dados = colunas[coluna];
            for (int linha = 0; linha < dados.size(); linha++) {
                matriz[linha][coluna] = Integer.toString(dados.get(linha));
            }
        }

        // Desenha a matriz no console
        System.out.println("+-0-+-1-+-2-+");
        for (int linha = 0; linha < 3; linha++) {
            System.out.println("+---+---+---+");
            for (int coluna = 0; coluna < 3; coluna++) {
                String valor = (matriz[linha][coluna] != null) ? matriz[linha][coluna] : " ";
                System.out.print("| " + valor + " ");
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+");
    }
    
    // Calcula a pontuação atual do jogador
    public int calcularPontuacao() {
        int pontuacao = 0;

        for (int coluna = 0; coluna < 3; coluna++) {
            ArrayList<Integer> dados = colunas[coluna];
            int[] contagem = new int[7]; // índices de 1 a 6

            for (int dado : dados) {
                contagem[dado]++;
            }

            for (int valor = 1; valor <= 6; valor++) {
                int quantidade = contagem[valor];

                if(quantidade == 2){
                    pontuacao += valor * 2 * 2; // dois dados * valor * multiplicador 2
                } else if (quantidade == 3) {
                    pontuacao += valor * 3 * 3; // três dados * valor * multiplicador 3
                } else if (quantidade == 1) {
                    pontuacao += valor; // valor sozinho sem multiplicador
                }
            }
        }

        return pontuacao;
    }


    //seleciona nome do jogador 
    private void setNome(String nome) {
        if(nome != null){
            this.nome = nome;
        }
    }

    // Getter para o nome do jogador
    public String getNome() {
        return nome;
    }

    public ArrayList<Integer>[] getColunas() {
        return colunas;
    }

}
