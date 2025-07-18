/**
 * Representa um jogador com nome e um tabuleiro 3x3 de dados.
 */
public class Jogador {
    private String nome;
    private Integer[][] tabuleiro;

    public Jogador(String nome) {
        this.nome = nome;
        this.tabuleiro = new Integer[3][3]; // Tabuleiro 3x3
    }

    public String getNome() {
        return nome;
    }

    public boolean posicionarDado(int linha, int coluna, int valor) {
        if (linha < 0 || linha >= 3 || coluna < 0 || coluna >= 3 || tabuleiro[linha][coluna] != null) {
            return false;
        }
        tabuleiro[linha][coluna] = valor;
        return true;
    }

    public boolean estaComTabuleiroCompleto() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void mostrarTabuleiro() {
        System.out.println("Tabuleiro de " + nome + ":");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print((tabuleiro[i][j] != null ? tabuleiro[i][j] : "_") + " ");
            }
            System.out.println();
        }
    }

    public int calcularPontuacao() {
        int total = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] != null) {
                    int valor = tabuleiro[i][j];
                    int repeticoes = contarOcorrencias(valor);
                    total += valor * repeticoes;
                }
            }
        }
        return total;
    }

    private int contarOcorrencias(int valor) {
        int cont = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] != null && tabuleiro[i][j] == valor) {
                    cont++;
                }
            }
        }
        return cont;
    }
}
