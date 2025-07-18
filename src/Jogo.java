import java.util.Scanner;

/**
 * Gerencia toda a lógica do jogo Bugalha entre dois jogadores.
 */
public class Jogo {
    private Jogador jogador1;
    private Jogador jogador2;
    private Scanner scanner;

    public Jogo(String nome1, String nome2) {
        this.jogador1 = new Jogador(nome1);
        this.jogador2 = new Jogador(nome2);
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        Jogador[] jogadores = { jogador1, jogador2 };
        int turno = 0;

        while (!jogador1.estaComTabuleiroCompleto() && !jogador2.estaComTabuleiroCompleto()) {
            Jogador atual = jogadores[turno % 2];
            System.out.println("\nVez de: " + atual.getNome());

            Dado dado = new Dado();
            System.out.println("Dado rolado: " + dado.getValor());

            atual.mostrarTabuleiro();

            boolean posicionado = false;
            while (!posicionado) {
                System.out.print("Escolha a linha (0-2): ");
                int linha = scanner.nextInt();
                System.out.print("Escolha a coluna (0-2): ");
                int coluna = scanner.nextInt();

                posicionado = atual.posicionarDado(linha, coluna, dado.getValor());
                if (!posicionado) {
                    System.out.println("Posição inválida ou ocupada. Tente novamente.");
                }
            }

            atual.mostrarTabuleiro();
            turno++;
        }

        encerrarJogo();
    }

    private void encerrarJogo() {
        System.out.println("\nFim do jogo!");
        jogador1.mostrarTabuleiro();
        jogador2.mostrarTabuleiro();

        int pontos1 = jogador1.calcularPontuacao();
        int pontos2 = jogador2.calcularPontuacao();

        System.out.println(jogador1.getNome() + " fez " + pontos1 + " pontos.");
        System.out.println(jogador2.getNome() + " fez " + pontos2 + " pontos.");

        if (pontos1 > pontos2) {
            System.out.println(jogador1.getNome() + " venceu!");
        } else if (pontos2 > pontos1) {
            System.out.println(jogador2.getNome() + " venceu!");
        } else {
            System.out.println("Empate!");
        }

        scanner.close();
    }
}
